package HospitalUI.AdminUI;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import Hospital.Controllers.AdministratorController;
import Hospital.Controllers.StaffController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.People.StaffRole;
import Hospital.World.CampusInfo;
import HospitalUI.MainUI.UtilsUI;
import java.util.List;

public class AddHospitalStaffUI {

    private AdministratorController ac;
    private WorldController wc;
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
        StaffRole[] roles = {StaffRole.Doctor, StaffRole.Nurse, StaffRole.WarehouseManager};
        int chosenInt = UtilsUI.selectCommand(sc, roles);
        if (chosenInt == 0) {
            return;
        }
        StaffRole chosenRole = roles[chosenInt - 1];
        System.out.println("Please enter the name: ");
        String name = sc.nextLine();
        CampusInfo info = null;
        if(chosenRole != StaffRole.Doctor){
            List<CampusInfo> infos = wc.getCampuses();
            chosenInt = UtilsUI.selectCommand(sc, infos.toArray());
            info = infos.get(chosenInt);
        }
        try {
            String newTest = staff.makeStaffMember(chosenRole, name, info);
            System.out.println("The staff member is added:");
            System.out.println(newTest);
        } catch (InvalidArgumentException ex) {
            System.out.println("The name cannot be empty");
        } catch (SchedulableAlreadyExistsException e) {
            System.out.println("Name already exists!");
        }
    }
}
