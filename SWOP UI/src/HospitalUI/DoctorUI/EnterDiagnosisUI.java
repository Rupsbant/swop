package HospitalUI.DoctorUI;

import Hospital.Controllers.DiagnosisController;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.People.LoginInfo;
import HospitalUI.MainUI.UtilsUI;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnterDiagnosisUI {

    DoctorController dc;
    WorldController wc;
    DiagnosisController diagnC;

    public EnterDiagnosisUI(DoctorController dc, WorldController wc) {
        this.dc = dc;
        this.wc = wc;
        this.diagnC = new DiagnosisController(wc, dc);
    }

    public void run(Scanner sc) throws NotLoggedInException, NoOpenedPatientFileException, PatientIsDischargedException {
        String[] names = {"Normal diagnosis", "Diagnosis that needs approval"};
        int factoryPos = UtilsUI.selectCommand(sc, names);
        if (factoryPos == 0) {
            return;
        }
        try {
            LoginInfo doctorInfo = null;
            if (factoryPos == 2) {
                LoginInfo[] secondOpinionAvailable = diagnC.getAvailableSecondOpinionDoctors();
                if (secondOpinionAvailable.length == 0) {
                    System.out.println("No doctors available. Aborting!");
                    return;
                }
                System.out.println("Chose which doctor needs to give the second opinion.");
                int secondOpinionPos = UtilsUI.selectCommand(sc, secondOpinionAvailable);
                if (secondOpinionPos == 0) {
                    return;
                }
                doctorInfo = secondOpinionAvailable[secondOpinionPos - 1];
            }
            System.out.println("Please enter the content of the diagnosis");
            String content = sc.nextLine();
            String out = diagnC.enterDiagnosis(content, doctorInfo);
            System.out.println("Diagnosis entered: \n" + out);
        } catch (InvalidArgumentException e) {
            Logger.getLogger(OrderMedicalUI.class.getName()).log(Level.SEVERE, null, e);
        } catch (NoPersonWithNameAndRoleException e) {
            System.out.println("No person with name and role exception.");
        }
    }
}
