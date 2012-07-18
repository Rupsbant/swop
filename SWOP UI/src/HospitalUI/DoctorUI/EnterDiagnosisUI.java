package HospitalUI.DoctorUI;

import Hospital.Controllers.ArgumentList;
import Hospital.Controllers.DiagnosisController;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.Exception.Arguments.WrongArgumentListException;
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
        String[] names = diagnC.getAvailableDiagnosisFactories();
        int factoryPos = UtilsUI.selectCommand(sc, names);
        if (factoryPos == 0) {
            return;
        }
        String factoryName = names[factoryPos - 1];
        try {
            ArgumentList args = diagnC.getDiagnosisArguments(factoryName);
            UtilsUI.answerArguments(sc, args.getPublicArguments());
            LoginInfo doctorInfo = null;
            if (factoryPos == 2) { //TODO fix this
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
            String out = diagnC.enterDiagnosis(factoryName, args, doctorInfo);
            System.out.println("Diagnosis entered: \n" + out);
        } catch (WrongArgumentListException e) {
            System.out.println("Wrong argument list exception.");
        } catch (InvalidArgumentException e) {
            Logger.getLogger(OrderMedicalUI.class.getName()).log(Level.SEVERE, null, e);
        } catch (NoPersonWithNameAndRoleException e) {
            System.out.println("No person with name and role exception.");
        } catch (NotAFactoryException e) {
            //This should not happen.
            //The factory was before the world was, and will be after it is destroyed...
            Logger.getLogger(EnterDiagnosisUI.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
