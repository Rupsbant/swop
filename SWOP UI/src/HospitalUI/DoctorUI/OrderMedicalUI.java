package HospitalUI.DoctorUI;

import Hospital.Controllers.ArgumentList;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.MedicalTestController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import HospitalUI.MainUI.UtilsUI;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderMedicalUI {

    DoctorController doc;
    WorldController wc;
    private MedicalTestController med;

    public OrderMedicalUI(DoctorController dc, WorldController wc) {
        this.doc = dc;
        this.wc = wc;
        this.med = new MedicalTestController(wc, dc);
    }

    public void run(Scanner sc) throws NotLoggedInException, NoOpenedPatientFileException {
        String[] tests = med.getAvailableMedicalTests();
        System.out.println("number: " + tests.length);
        int chosenInt = UtilsUI.selectCommand(sc, tests);
        if (chosenInt == 0) {
            return;
        }
        String chosen = tests[chosenInt - 1];
        try {
            ArgumentList args = med.getMedicalTestArguments(chosen);
            UtilsUI.answerArguments(sc, args.getPublicArguments());
            String newTest = med.makeMedicalTest(chosen, args);
            System.out.println("The test was made and scheduled:");
            System.out.println(newTest);
        } catch (ArgumentConstraintException ex) {
            System.out.println("Argument didn't satisfy constraints.");
            System.out.println(ex.toString());
        } catch (InvalidArgumentException ex) {
            Logger.getLogger(OrderMedicalUI.class.getName()).log(Level.SEVERE, "Never happens, same ArgumentList and fully answered", ex);
        } catch (NotAFactoryException ex) {
            //This should not happen.
            //The factory was before the world was, and will be after it is destroyed...
            Logger.getLogger(OrderMedicalUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
