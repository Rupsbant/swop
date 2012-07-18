package HospitalUI.AdminUI;


import java.util.Scanner;

import Hospital.Controllers.AdministratorController;
import Hospital.Controllers.ArgumentList;
import Hospital.Controllers.MachineController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import HospitalUI.MainUI.UtilsUI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddHospitalEquipmentUI {

    AdministratorController ac;
    WorldController wc;
    MachineController mc;

    public AddHospitalEquipmentUI(AdministratorController ac, WorldController wc) {
        this.ac = ac;
        this.wc = wc;
        try {
            this.mc = new MachineController(wc, ac);
        } catch (ArgumentIsNullException ex) {
            Logger.getLogger(AddHospitalEquipmentUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run(Scanner sc) throws NotLoggedInException {
        String[] tests = mc.getAvailableMachineFactories();
        int chosenInt = UtilsUI.selectCommand(sc, tests);
        if (chosenInt == 0) {
            return;
        }
        String chosen = tests[chosenInt - 1];
        try {
            ArgumentList args = mc.getMachineArguments(chosen);
            UtilsUI.answerArguments(sc, args.getPublicArguments());
            String newTest = mc.makeMachine(chosen, args);
            System.out.println("The machine is added:");
            System.out.println(newTest);
        } catch (ArgumentConstraintException ex) {
            System.out.println("Argument didn't satisfy constraints.");
            System.out.println(ex);
        } catch (InvalidArgumentException ex) {
            Logger.getLogger(AddHospitalStaffUI.class.getName()).log(Level.SEVERE, "Gave wrong argumentList", ex);
        } catch (NotAFactoryException ex) {
            //This should not happen.
            //The factory was before the world was, and will be after it is destroyed...
            Logger.getLogger(AddHospitalStaffUI.class.getName()).log(Level.SEVERE, "Factory should always exist", ex);
        } catch (SchedulableAlreadyExistsException e) {
            System.out.println("ID already exists!");
        } catch (CannotChangeException e) {
			System.out.println("Cannot change campus exception, should not happen." + e.getMessage());
		}
    }
}
