package HospitalUI.DoctorUI;

import java.util.Scanner;

import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Controllers.DiagnosisController;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.NotLoggedInException;
import Hospital.Patient.DiagnosisInfo;
import HospitalUI.MainUI.UtilsUI;

public class ApproveDiagnosisUI {

    DoctorController dc;
    WorldController wc;
    DiagnosisController diagnC;

    public ApproveDiagnosisUI(DoctorController dc, WorldController wc) {
        this.dc = dc;
        this.wc = wc;
        this.diagnC = new DiagnosisController(wc, dc);
    }
    
    
    public void run(Scanner sc) throws NotLoggedInException {
        DiagnosisInfo[] secop = dc.getUnapprovedSecondOpinions();
        if (secop.length == 0) {
            System.out.println("This doctor doesn't have any diagnoses to approve.");
            return;
        }
        int chosendiag = UtilsUI.selectCommand(sc, secop);
        if (chosendiag == 0) {
            return;
        }
        DiagnosisInfo diag = secop[chosendiag - 1];
        String diag2 = diag.toString();
        System.out.println(diag);
        String[] options = {"yes", "no"};
        System.out.println("Do you approve the diagnosis of your colleague?");
        int chosenopt = UtilsUI.selectCommand(sc, options);
        if (chosenopt == 0) {
            return;
        } else if (chosenopt == 1) {
            try {
                String x = diagnC.approveDiagnosis(diag);
                System.out.println("Diagnosis approved: \n" + x);
                if (diag.hasTreatment()) {
                    System.out.println("The following threatment was scheduled:\n" + diag.treatmentString());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.err.println(ex.getMessage());
                throw new Error("Don't break the system!");
            }
        } else if (chosenopt == 2) {
            try {
                PublicArgument[] questions = new PublicArgument[1];
                questions[0] = new StringArgument("Enter the new diagnosis: ");
                UtilsUI.answerArguments(sc, questions);
                String y = diagnC.disapproveDiagnosis(diag, (String) questions[0].getAnswer());
                System.out.println("Diagnosis disapproved. \nNew diagnosis created: " + y);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new Error("Don't break the system!");
            }
        }
    }
    
    
}
