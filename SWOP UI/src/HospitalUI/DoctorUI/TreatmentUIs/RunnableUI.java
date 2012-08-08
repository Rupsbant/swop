package HospitalUI.DoctorUI.TreatmentUIs;

import Hospital.Controllers.TreatmentController;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Patient.DiagnosisInfo;
import java.util.Scanner;

public interface RunnableUI {
    
    public void run(Scanner sc, TreatmentController tc, DiagnosisInfo diagnosisInfo) 
            throws NoOpenedPatientFileException, NotLoggedInException, InvalidDiagnosisException, InvalidArgumentException;
    
}
