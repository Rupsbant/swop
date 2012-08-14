package HospitalUI.DoctorUI.MedicalTests;

import Hospital.Argument.PriorityArgument;
import Hospital.Controllers.MedicalTestController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Schedules.Constraints.Priority.Priority;
import java.util.Scanner;

public class OrderBloodAnalysis implements RunnableUI {

    public void run(Scanner sc, MedicalTestController med) {
        System.out.println("Enter the focus of the analysis:");
        String focus = sc.nextLine();
        System.out.println("Enter the number of analyses there must be made");
        int numberOfAnalyses = sc.nextInt();
        PriorityArgument priorityArgument = new PriorityArgument("Enter the priority of the appointment");
        Priority priority = priorityArgument.getAnswer();
        try {
            String out = med.makeBloodAnalysis(focus, numberOfAnalyses, priority);
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
        return "Order new BloodAnalysis";
    }

    public String toString() {
        return getName();
    }
}
