package HospitalUI.AdminUI;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import Hospital.Argument.PublicArgument;
import Hospital.Controllers.AdministratorController;
import Hospital.Controllers.ArgumentList;
import Hospital.Controllers.StaffController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import HospitalUI.MainUI.UtilsUI;

public class AddHospitalStaffUI {

    AdministratorController ac;
    WorldController wc;
    private StaffController staff;

    public AddHospitalStaffUI(AdministratorController ac, WorldController wc) {
        this.ac = ac;
        this.wc = wc;
        try {
            this.staff = new StaffController(wc, ac);
        } catch (ArgumentIsNullException e) {
            System.out.println("Argument is null exception");
        }
    }

    public void run(Scanner sc) throws NotLoggedInException {
        String[] tests = staff.getAvailableStaffFactories();
        int chosenInt = UtilsUI.selectCommand(sc, tests);
        if (chosenInt == 0) {
            return;
        }
        String chosen = tests[chosenInt - 1];
        try {
            ArgumentList argL = staff.getStaffArguments(chosen);
            PublicArgument[] args = argL.getPublicArguments();
            UtilsUI.answerArguments(sc, args);
            String newTest = staff.makeStaffMember(chosen, argL);
            System.out.println("The staff member is added:");
            System.out.println(newTest);
        } catch (InvalidArgumentException ex) {
            Logger.getLogger(AddHospitalStaffUI.class.getName()).log(Level.SEVERE, "Never happens, its the same ArgumentList and always fully answered", ex);
        } catch (NotAFactoryException ex) {
            //This should not happen.
            //The factory was before the world was, and will be after it is destroyed...
            Logger.getLogger(AddHospitalStaffUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SchedulableAlreadyExistsException e) {
            System.out.println("Name already exists!");
        } catch (CannotChangeException e) {
            System.out.println("Cannot change campus exception, should not happen" + e.getMessage());
        }
    }
}
