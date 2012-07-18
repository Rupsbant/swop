import Hospital.Controllers.WorldController;
import Hospital.World.BasicWorld;
import HospitalUI.MainUI.MainUI;

import java.util.Scanner;


/**
 * The application's entry point.
 * Initializes the interface and base world.
 */
public class Main {
	/**
	 * Application entry point
	 * @param args the commandline arguments
	 */
    public static void main(String[] args){
        MainUI m = new MainUI(WorldController.getBasicWorld());
        //MainUI m = new MainUI(BasicWorld.getEmptyBasicWorld());
    	Scanner sc = new Scanner(System.in);
        m.run(sc);
    }
}