package HospitalUI.DoctorUI.MedicalTests;

import Hospital.Argument.BooleanArgument;
import Hospital.Argument.PriorityArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Controllers.MedicalTestController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Schedules.Constraints.Priority.Priority;
import HospitalUI.MainUI.UtilsUI;
import java.util.Scanner;

public class OrderUltraSoundScan implements RunnableUI {

    public void run(Scanner sc, MedicalTestController med) {
        System.out.println("Enter the focus of the analysis:");
        String focus = sc.nextLine();
        BooleanArgument videoA = new BooleanArgument("Should video be taken?:");
        BooleanArgument imageA = new BooleanArgument("Should images be taken?:");
        PriorityArgument priorityArgument = new PriorityArgument("Enter the priority of the appointment");
        UtilsUI.answerArguments(sc, new PublicArgument[]{videoA, imageA, priorityArgument});
        Priority priority = priorityArgument.getAnswer();
        try {
            String out = med.makeUltraSound(focus, videoA.getAnswer(), 
                                            imageA.getAnswer(), priority);
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
        return "Order new UltraSoundScan";
    }
    
    public String toString(){
        return getName();
    }
    
}
