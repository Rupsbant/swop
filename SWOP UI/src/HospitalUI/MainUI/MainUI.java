package HospitalUI.MainUI;

import Hospital.Controllers.AdministratorController;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.LoginController;
import Hospital.Controllers.NurseController;
import Hospital.Controllers.WareHouseController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotLoggedInException;
import Hospital.People.LoginInfo;
import Hospital.People.StaffRole;
import Hospital.World.CampusInfo;
import HospitalUI.AdminUI.AdministratorUI;
import HospitalUI.DoctorUI.DoctorUI;
import HospitalUI.NurseUI.NurseUI;
import HospitalUI.WareHouseUI.WareHouseUI;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main user interface, this is where the program starts.
 * It is activated by calling the run(Scanner)-method.
 */
public class MainUI {

    WorldController w;

    /**
     * Constructor
     * @param w The WorldController of the world this interface will be used on.
     */
    public MainUI(WorldController w) {
        this.w = w;
    }

    /**
     * Starts the interface.
     * @param sc The scanner that takes input from the user.
     */
    public void run(Scanner sc) {
        int pos = -1;
        while (pos != 0) {
            String[] commands = new String[]{
                "Login"
            };
            pos = UtilsUI.selectCommand(sc, commands);
            switch (pos) {
                case 1:
                    loginAndRun(sc);
            }
        }
    }

    private LoginController login(Scanner sc) {
        List<LoginInfo> logins = w.getLogins();
        List<CampusInfo> campuses = w.getCampuses();

        int posLogin = -1;
        int posCampus = -1;

        while (true) {
            posLogin = UtilsUI.selectCommand(sc, logins.toArray());
            if (posLogin < 0 || posLogin > logins.size()) {
                System.out.println("Please enter a valid number!");
                continue;
            }
            if (posLogin == 0) {
                return null;
            }
            posCampus = UtilsUI.selectCommand(sc, campuses.toArray());
            if (posCampus < 0 || posCampus > campuses.size()) {
                System.out.println("Please enter a valid number!");
                continue;
            }
            if (posCampus == 0) {
                return null;
            }
            try {
                return w.login(campuses.get(posCampus - 1), logins.get(posLogin - 1));
            } catch (NoPersonWithNameAndRoleException ex) {
                System.out.println("Enter a valid name, and don't break the system!");
                continue;
            } catch (ArgumentIsNullException e) {
                System.out.println("The system has encountered an unexpected error");
                continue;
            } catch (ArgumentConstraintException e) {
                System.out.println("Enter a valid campus, and don't break the system!");
                continue;
            }
        }

    }

    private void loginAndRun(Scanner sc) {
        LoginController login = login(sc);
        if (login == null) {
            return;
        }
        System.out.println(login.getRole());
        if (login.getRole().equals(StaffRole.Doctor)) {
            DoctorController dc = (DoctorController) login;
            DoctorUI ui = new DoctorUI(dc, w);
            try {
                ui.run(sc);
            } catch (NotLoggedInException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (login.getRole().equals(StaffRole.HospitalAdministrator)) {
            AdministratorController ac = (AdministratorController) login;
            AdministratorUI ui = new AdministratorUI(ac, w);
            try {
                ui.run(sc);
            } catch (NotLoggedInException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (login.getRole().equals(StaffRole.Nurse)) {
            try {
                NurseController nc = (NurseController) login;
                NurseUI ui = new NurseUI(nc, w);
                ui.run(sc);
            } catch (ArgumentIsNullException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotLoggedInException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (login.getRole().equals(StaffRole.WarehouseManager)) {
            WareHouseController whc = (WareHouseController) login;
            WareHouseUI ui = new WareHouseUI(whc);
            try {
                ui.run(sc);
            } catch (NotLoggedInException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
