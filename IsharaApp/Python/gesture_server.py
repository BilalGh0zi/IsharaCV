#Main gesture detection will happen here, with the help of Media Pipe 
#gestures will be detected and a corresponding signal will be sent to 
#the Java part of the program using TCP sockets

#imports:
import cv2
import socket
import mediapipe as mp
from mediapipe.tasks.python import vision
from mediapipe.tasks import python
import math

import json
try:
    with open(r"C:\Users\Bilal\Desktop\IsharaApp\config\settings.json") as f:
        cfg = json.load(f)
        FRAMES_REQUIRED = int(cfg.get("__framesRequired", 4))
except:
    FRAMES_REQUIRED = 4
#TCP Socket setup
serverPy = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
serverPy.bind(("localhost", 6767)) #snake cuz python and port no is arbitrarily funny number
serverPy.listen(1)
print("Waiting for Java...(o_O)")
javaPipe, javaAddress = serverPy.accept()
print("Java connected, lookin out for gestures!")

#setting up camera
Cam = cv2.VideoCapture(0)

## MediaPipe, AI model from hand_landmarker.task
AImodelLoc= python.BaseOptions(model_asset_path=r"C:\Users\Bilal\Desktop\IsharaApp\Python\hand_landmarker.task")

DetectionSettings = vision.HandLandmarkerOptions(
    base_options=AImodelLoc,
    running_mode=vision.RunningMode.VIDEO,
    num_hands=1,
    min_hand_detection_confidence=0.6,
    min_tracking_confidence=0.5
)
HANDdetector = vision.HandLandmarker.create_from_options(DetectionSettings)

# Gesture needs to be held for this many frames before sending
FRAMES_REQUIRED = 4
gesture_counter = {}

#\\\\\\\\\\\\\\ GESTURE DETECTION /////////////////////////

#Important landmarks:
#   Finger tips:  4(thumb), 8(index), 12(middle), 16(ring), 20(pinky)
#   Finger bases: 3(thumb), 6(index), 10(middle), 14(ring), 18(pinky)

#-------------------------------------------------
# future Bilal note: 
# So basically, Media Pipe marks 21 points on a hand as 
# a landmark, each with an X,Y,Z co ordinate
# we're using the positions of these landmarks relative to 
# each other do determine the shape of the hand or the "gesture"
# being prformed at a given moment
# for any finger if the tip is above the base then it is considered open
# Y co ordinate will be zero at the top and increase downwards
#-----------------------------------------------------------

def isFingerOpen(landmarks, tip, base): #returns Boolean (personal note im bad at python)
    return landmarks[tip].y < landmarks[base].y

def getDistance(landmarks, a, b):#returns int
    dx = landmarks[a].x - landmarks[b].x
    dy = landmarks[a].y - landmarks[b].y
    return math.sqrt(dx*dx + dy*dy)

def detect_gesture(landmarks):
    # Check pos of each finger
    index_open  = isFingerOpen(landmarks, 8, 6)
    middle_open = isFingerOpen(landmarks, 12, 10)
    ring_open   = isFingerOpen(landmarks, 16, 14)
    pinky_open  = isFingerOpen(landmarks, 20, 18)
    thumb_extended = landmarks[4].y < landmarks[3].y
    thumb_up = landmarks[4].y < landmarks[2].y

    # [GESTURE 1] ONE FINGER: only index open
    if (index_open and not middle_open and not ring_open and not pinky_open):
        return "ONE_FINGER"
    # [GESTURE 2] 2 FINGERS: index+middle   
    if (index_open and middle_open and not ring_open and not pinky_open):
        return "TWO_FINGER"
    # [GESTURE 3] 3 FINGERS: index+middle+ring   
    if (index_open and middle_open and ring_open and not pinky_open):
        return "THREE_FINGER"
    # [GESTURE 4] closed fist
    if (not index_open and not middle_open and not ring_open and not pinky_open):
        return "FIST"
    # [GESTURE 5] PINCH: thumb tip and index tip close together
    pinch_distance = getDistance(landmarks, 4, 8)
    if (pinch_distance < 0.05):
        return "PINCH"
    # [GESTURE 6] ROCK HAND: index + pinky extended, middle + ring closed
    if (index_open and not middle_open and not ring_open and pinky_open):
        return "ROCK_HAND"
    # No recognizable gesture
    return "NONE"

#\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

#/////////////[Main Loop]//////////////
timestamp = 0
while True:
    caughtFrame, CurrentFrame = Cam.read()
    if not caughtFrame:
        continue

    rgb = cv2.cvtColor(CurrentFrame, cv2.COLOR_BGR2RGB)
    mp_image = mp.Image(image_format=mp.ImageFormat.SRGB, data=rgb)

    timestamp += 33
    try:
        result = HANDdetector.detect_for_video(mp_image, timestamp)
    except Exception as e:
        print(f"Detection error: {e}")
        continue

    Ishara = "NONE"
    if result.hand_landmarks:
        landmarks = result.hand_landmarks[0]
        detected = detect_gesture(landmarks)

        # Count consecutive frames of same gesture
        gesture_counter[detected] = gesture_counter.get(detected, 0) + 1

        # Reset counter for all other gestures
        for g in list(gesture_counter.keys()):
            if g != detected:
                gesture_counter[g] = 0

        # Only send if held long enough
        if gesture_counter[detected] >= FRAMES_REQUIRED:
            Ishara = detected
            print(f"Gesture: {Ishara}")
    else:
        # No hand detected - reset all counters
        gesture_counter.clear()

    try:
        javaPipe.send((Ishara + "\n").encode())
    except:
        print("WHERE IS JAVA???")
        break

    cv2.putText(CurrentFrame, Ishara, (10, 40),
                cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
    cv2.imshow("IsharaApp - Camera", CurrentFrame)

    if cv2.waitKey(1) == 27:
        break

Cam.release()
cv2.destroyAllWindows()
javaPipe.close()
serverPy.close()