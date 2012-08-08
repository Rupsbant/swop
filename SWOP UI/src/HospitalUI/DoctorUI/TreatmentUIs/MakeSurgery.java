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

public class MakeSurgery implements RunnableUI {

    public void run(Scanner sc, TreatmentController tc, DiagnosisInfo diagnosisInfo) throws NoOpenedPatientFileException, NotLoggedInException, InvalidDiagnosisException, InvalidArgumentException {
        System.out.println("Enter the description of the surgery");
        String description = sc.nextLine();
        PriorityArgument arg = new PriorityArgument("Enter the priority of the surgery");
        BasicAnswerer.singleton.answer(arg, sc);
        tc.makeSurgery(diagnosisInfo, description, arg.getAnswer());
    }
    
}
