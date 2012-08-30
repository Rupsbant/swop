package HospitalUI.DoctorUI.MedicalTests;

import Hospital.Argument.PriorityArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Controllers.MedicalTestController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Schedules.Constraints.Priority.Priority;
import HospitalUI.MainUI.UtilsUI;
import java.util.Scanner;

public class OrderXRay implements RunnableUI {

    public void run(Scanner sc, MedicalTestController med) {
        System.out.println("Enter the bodypart that must be scanned:");
        String bodyPart = sc.nextLine();
        System.out.println("Enter the zoomlevel");
        int zoom = sc.nextInt();
        System.out.println("Enter the number of images there must be made");
        int images = sc.nextInt();
        PriorityArgument priorityArgument = new PriorityArgument("Enter the priority of the appointment");
        UtilsUI.answerArguments(sc, new PublicArgument[]{priorityArgument});
        Priority priority = priorityArgument.getAnswer();
        try {
            String out = med.makeXRayScan(bodyPart, zoom, images, priority);
            System.out.println(out);
        } catch (NoOpenedPatientFileException ex) {
            throw new Error(ex); // patientFile was opened
        } catch (ArgumentConstraintException ex) {
            System.out.println("Not all constraints were satisfied, try again. " + ex.getMessage());
        } catch (InvalidArgumentException ex) {
            throw new Error(ex); //nothing was null
        }
    }

    public String getName() {
        return "Order new XRayScan";
    }
    
    public String toString(){
        return getName();
    }
    
}
