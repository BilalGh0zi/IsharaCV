
package gestures; 

public abstract class Gesture{
     String name;
     public Gesture(String name){
        this.name = name;
     }

     public String getname(){
        return name;
     }
     public abstract void execute();
}