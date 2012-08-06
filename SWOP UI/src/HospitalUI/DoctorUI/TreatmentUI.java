package HospitalUI.DoctorUI;

import Hospital.Controllers.ArgumentList;
import Hospital.Patient.DiagnosisInfo;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.TreatmentController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.Warehouse.ItemNotFoundException;
import Hospital.Exception.Warehouse.ItemNotReservedException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Warehouse.StockException;
import HospitalUI.MainUI.UtilsUI;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TreatmentUI {

    private DoctorController dc;
    private WorldController wc;
    private TreatmentController tc;

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

        String[] tests = tc.getAvailableTreatments();
        int chosenInt = UtilsUI.selectCommand(sc, tests);
        if (chosenInt == 0) {
            return;
        }
        String chosen = tests[chosenInt - 1];

        try {
            ArgumentList argL = tc.getTreatmentArguments(chosen);
            UtilsUI.answerArguments(sc, argL.getPublicArguments());
            String newTest = tc.makeTreatment(chosen, argL, infos[diagnosisNumber - 1]);
            System.out.println("The test was made and scheduled:");
            System.out.println(newTest);
        } catch (ArgumentIsNullException ex) {
            throw new Error("Nothing was changed in the argumentlist");
        } catch (InvalidArgumentException ex) {
            Logger.getLogger(OrderMedicalUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotAFactoryException ex) {
            //This should not happen.
            //The factory was before the world was, and will be after it is destroyed...
            Logger.getLogger(OrderMedicalUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidDiagnosisException ex) {
            System.out.println("Oops, something went wrong!");
        } catch (StockException e) {
            System.out.println("stock not available exception");
        } catch (ItemNotReservedException e) {
            System.out.println("item not reserved exception, should not happen");
        } catch (ItemNotFoundException e) {
            System.out.println("item not found exception, should not happen");
        }
    }
}
