package HospitalUI.DoctorUI;

import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Patient.DiagnosisInfo;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.TreatmentController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NotLoggedInException;
import HospitalUI.DoctorUI.TreatmentUIs.MakeCast;
import HospitalUI.DoctorUI.TreatmentUIs.RunnableUI;
import Hospital.Exception.Warehouse.StockException;
import HospitalUI.MainUI.UtilsUI;

import java.util.Scanner;

public class TreatmentUI {

    private DoctorController dc;
    private WorldController wc;
    private TreatmentController tc;
    private RunnableUI[] treatments = new RunnableUI[]{
        new MakeCast()
    };

    public TreatmentUI(DoctorController dc, WorldController wc) {
        this.dc = dc;
        this.wc = wc;
        this.tc = new TreatmentController(wc, dc);
    }

    public void run(Scanner sc) throws NotLoggedInException, NoOpenedPatientFileException {
        DiagnosisInfo[] infos = null;
        try {
            infos = tc.getUntreatedDiagnoses();
        } catch (NoOpenedPatientFileException ex) {
            System.out.println("No opened patientFile, aborting!");
            return;
        }
        int diagnosisNumber = UtilsUI.selectCommand(sc, infos);
        if (diagnosisNumber == 0) {
            return;
        }

        int chosenInt = UtilsUI.selectCommand(sc, treatments);
        if (chosenInt == 0) {
            return;
        }
        RunnableUI chosen = treatments[chosenInt - 1];
        try {
            chosen.run(sc, tc, infos[diagnosisNumber]);
        } catch (InvalidDiagnosisException ex) {
            System.out.println("The diagnosisinfo was invalid, this should not happen.");
        } catch (InvalidArgumentException ex) {
            System.out.println("You didn't answer an argument correctly:");
            System.out.println(ex.getMessage());
        }
    }
}