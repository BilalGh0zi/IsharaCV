#Main gesture detection will happen here
#gestures will be detected and a corresponding signal will be sent to 
#the Java part of the program using TCP sockets

import cv2
import socket
import mediapipe as mp
from mediapipe.tasks.python import vision
from mediapipe.tasks import python
import math
import json
import sys
import os

##pathfinding
if len(sys.argv) > 1:
    PYTHON_DIR = sys.argv[1].strip()
else:
    PYTHON_DIR = os.path.dirname(os.path.abspath(__file__))

ROOT_DIR   = os.path.dirname(PYTHON_DIR.rstrip("/\\"))
CONFIG_DIR = os.path.join(ROOT_DIR, "config")

print(f"[PathFinder] Python dir: {PYTHON_DIR}")
print(f"[PathFinder] Config dir: {CONFIG_DIR}")

FRAMES_REQUIRED = 4 
try:
    settings_path = os.path.join(CONFIG_DIR, "settings.json")
    with open(settings_path) as f:
        cfg = json.load(f)
        FRAMES_REQUIRED = int(cfg.get("__framesRequired", 4))
    print(f"[PathFinder] Frames required: {FRAMES_REQUIRED}")
except:
    print("[PathFinder] Could not read settings, using default frames required: 4")

#TCP Socket setup
serverPy = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
serverPy.bind(("localhost", 6767))
serverPy.listen(1)
print("Waiting for Java...(o_O)")
javaPipe, javaAddress = serverPy.accept()
print("Java connected, lookin out for gestures!")

#setting up camera
Cam = cv2.VideoCapture(0) # 0 is built in cam

# MediaPipe model path
model_path = os.path.join(PYTHON_DIR, "hand_landmarker.task")
AImodelLoc = python.BaseOptions(model_asset_path=model_path)

DetectionSettings = vision.HandLandmarkerOptions(
    base_options=AImodelLoc,
    running_mode=vision.RunningMode.VIDEO,
    num_hands=1,
    min_hand_detection_confidence=0.6,# how sure mediapipe should be that it sees a hand
    min_tracking_confidence=0.5
)
HANDdetector = vision.HandLandmarker.create_from_options(DetectionSettings)

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

def isFingerOpen(landmarks, tip, base):
    return landmarks[tip].y < landmarks[base].y

def getDistance(landmarks, a, b):
    Xdist = landmarks[a].x - landmarks[b].x
    Ydist = landmarks[a].y - landmarks[b].y
    return math.sqrt(Xdist*Xdist + Ydist*Ydist)

def detect_gesture(landmarks):
    Index  = isFingerOpen(landmarks, 8, 6)
    Middle = isFingerOpen(landmarks, 12, 10)
    Ring   = isFingerOpen(landmarks, 16, 14)
    Pinky  = isFingerOpen(landmarks, 20, 18)
    thumb_extended = landmarks[4].y < landmarks[3].y

    # [GESTURE 1] ONE FINGER: only index open
    if (Index and not Middle and not Ring and not Pinky):
        return "ONE_FINGER"
    # [GESTURE 2] TWO FINGERS: index + middle
    if (Index and Middle and not Ring and not Pinky):
        return "TWO_FINGER"
    # [GESTURE 3] THREE FINGERS: index + middle + ring
    if (Index and Middle and Ring and not Pinky):
        return "THREE_FINGER"
    # [GESTURE 4] FIST: all fingers closed
    if (not Index and not Middle and not Ring and not Pinky):
        return "FIST"
    # [GESTURE 5] PINCH: thumb tip and index tip close together
    pinch_distance = getDistance(landmarks, 4, 8)
    if (pinch_distance < 0.05):
        return "PINCH"
    # [GESTURE 6] ROCK HAND: index + pinky extended
    if (Index and not Middle and not Ring and Pinky):
        return "ROCK_HAND"

    return "NONE"

#/////////////[Main Loop]//////////////
timestamp=0 
while True:
    caughtFrame, CurrentFrame = Cam.read()
    if (not caughtFrame):
        continue

    rgb = cv2.cvtColor(CurrentFrame, cv2.COLOR_BGR2RGB)# changes color order
    MP_image = mp.Image(image_format=mp.ImageFormat.SRGB, data=rgb)
 
    timestamp += 33 #33 increase per loop, roughly 30 fps
    try:
        DetectionStuff = HANDdetector.detect_for_video(MP_image, timestamp)
    except Exception as e:
        print(f"Detection error: {e}")
        continue

    Ishara = "NONE"
    if (DetectionStuff.hand_landmarks):
        landmarks = DetectionStuff.hand_landmarks[0]
        detected = detect_gesture(landmarks)
        ##Makes sure gesture is held for req no of frames
        gesture_counter[detected] = gesture_counter.get(detected, 0) + 1

        for g in list(gesture_counter.keys()):
            if (g != detected):gesture_counter[g] = 0

        if (gesture_counter[detected] >= FRAMES_REQUIRED):
            Ishara = detected
            print(f"Gesture: {Ishara}")
    else:
        gesture_counter.clear()

    try:
        javaPipe.send((Ishara + "\n").encode())
    except:
        print("WHERE IS JAVA???")
        break

    cv2.putText(CurrentFrame, Ishara, (10, 40),cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
    cv2.imshow("IsharaCam", CurrentFrame)

    if (cv2.waitKey(1) == 27):
        break

Cam.release()
cv2.destroyAllWindows()
javaPipe.close()
serverPy.close()