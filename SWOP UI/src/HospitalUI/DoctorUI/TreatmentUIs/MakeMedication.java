package HospitalUI.DoctorUI.TreatmentUIs;

import Hospital.Argument.BooleanArgument;
import Hospital.Argument.ItemArgument;
import Hospital.Argument.PriorityArgument;
import Hospital.Controllers.TreatmentController;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Patient.DiagnosisInfo;
import HospitalUI.MainUI.BasicAnswerer;
import HospitalUI.MainUI.ItemArgumentAnswerer;
import java.util.Scanner;

public class MakeMedication implements RunnableUI {

    public void run(Scanner sc, TreatmentController tc, DiagnosisInfo diagnosisInfo) 
            throws NoOpenedPatientFileException, NotLoggedInException, InvalidDiagnosisException, InvalidArgumentException {
        System.out.println("Enter the bodypart that must be put in a cast");
        String description = sc.nextLine();
        System.out.println("Enter the time the cast must stay on");
        BooleanArgument arg = new BooleanArgument("Is the patient sensitive to the medication?");
        BasicAnswerer.singleton.answer(arg, sc);
        PriorityArgument arg2 = new PriorityArgument("Enter the priority of the medication-treatment");
        BasicAnswerer.singleton.answer(arg2, sc);
        ItemArgument arg3 = tc.getItemArgument();
        ItemArgumentAnswerer.singleton.answer(arg, sc);
        String items = arg3.getAnswer();
        tc.makeMedication(diagnosisInfo, description, arg.getAnswer(), items, arg2.getAnswer());
    }
    
    @Override
    public String toString(){
        return "Order medication";
    }
}
