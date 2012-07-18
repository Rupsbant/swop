package HospitalUI.DoctorUI;

import Hospital.Argument.ListArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.CannotFindException;
import Hospital.Exception.NotLoggedInException;
import HospitalUI.MainUI.UtilsUI;
import java.util.Scanner;

public class SelectPreferenceUI {

    DoctorController dc;
    WorldController wc;

    public SelectPreferenceUI(DoctorController dc, WorldController wc) {
        this.dc = dc;
        this.wc = wc;
    }

    public void run(Scanner sc) throws NotLoggedInException {
        while (true) {
            ListArgument<String> arg= wc.getPreferences();
            UtilsUI.answerArguments(sc, new PublicArgument[]{arg});
            try {
                dc.setPreference(wc, arg);
                return;
            } catch (CannotFindException ex) {
                System.out.println("Unknown error, preference could not be found");
                System.out.println("Try again");
            }
        }
    }
}
