package HospitalUI.DoctorUI.TreatmentUIs;

import Hospital.Argument.PriorityArgument;
import Hospital.Controllers.TreatmentController;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Patient.DiagnosisInfo;
import HospitalUI.MainUI.BasicAnswerer;
import java.util.Scanner;

public class MakeCast implements RunnableUI {

    public void run(Scanner sc, TreatmentController tc, DiagnosisInfo diagnosisInfo) 
            throws NoOpenedPatientFileException, NotLoggedInException, InvalidDiagnosisException, InvalidArgumentException {
        System.out.println("Enter the bodypart that must be put in a cast");
        String bodyPart = sc.nextLine();
        System.out.println("Enter the time the cast must stay on");
        int duration = sc.nextInt();
        PriorityArgument arg = new PriorityArgument("Enter the priority of the cast");
        BasicAnswerer.singleton.answer(arg, sc);
        tc.makeCast(diagnosisInfo, bodyPart, duration, arg.getAnswer());
    }
    
    @Override
    public String toString(){
        return "Make cast";
    }
    
}
