// PathFinder finds the path from where the app is running from at runtime
//for portability, previously ishara app would use hardcode file paths
package system;

import java.io.File;
import java.net.URISyntaxException;

public class PathFinder {
    private static PathFinder friend; //Apex legends reference
    private final String rootDir;
                                     //root directory for isharaApp


    private PathFinder(){
        rootDir = findRootDir();
        System.out.println("[PathFinder] Root dir: " + rootDir);
    }

    public static PathFinder getInstance(){// makes sure only one
        if (friend == null) {               //pathfinder exists
            friend = new PathFinder();
        }
        return friend;
    }
    private String findRootDir(){
        try {
            // Get the directory of class files
    File ClassDir = new File(PathFinder.class.getProtectionDomain().getCodeSource().getLocation().toURI());

    //basically exits the directory till it reaches IsharaApp folder
    File root = ClassDir.getParentFile().getParentFile().getParentFile(); 
    return root.getAbsolutePath();

        } catch (URISyntaxException e){
            System.out.println("[PathFinder] Directory not found");
            return System.getProperty("user.dir");
            //returning current directory if not found
        }
    }

   //for a file in config folder
    public String getConfigPath(String name){
        return rootDir  +"\\"+"config"+"\\"+ name;
    }
    //for a file in python folder
    public String getPythonPath(String name){
        return rootDir  +"\\"+"Python"+"\\"+ name;
    }
    // Returns the root directory (IsharaApp)
    public String getRootDir(){return rootDir;}
}