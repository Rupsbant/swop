package HospitalUI.AdminUI;

import java.util.Scanner;

import Hospital.Controllers.AdministratorController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.NotLoggedInException;
import HospitalUI.MainUI.UtilsUI;

public class AdministratorUI {

    AdministratorController ac;
    WorldController wc;

    public AdministratorUI(AdministratorController ac, WorldController wc) {
        this.ac = ac;
        this.wc = wc;
    }

    public void run(Scanner sc) throws NotLoggedInException {
        int pos = -1;
        while (pos != 0) {
            String[] commands = new String[]{
                "Add Hospital Staff",
                "Add Hospital Equipment",
                "Advance Time"
            };
            pos = UtilsUI.selectCommand(sc, commands);
            switch (pos) {
                case 0:
                    ac.logout();
                    break;
                case 1:
                    AddHospitalStaffUI temp = new AddHospitalStaffUI(ac, wc);
                    temp.run(sc);
                    break;
                case 2:
                    AddHospitalEquipmentUI equip = new AddHospitalEquipmentUI(ac, wc);
                    equip.run(sc);
                    break;
                case 3:
                    AdvanceTimeUI adv = new AdvanceTimeUI(ac, wc);
                    adv.run(sc);
                    break;
            }
        }
    }
}
