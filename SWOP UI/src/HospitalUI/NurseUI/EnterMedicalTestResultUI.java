package HospitalUI.NurseUI;

import Hospital.Controllers.ArgumentList;
import Hospital.Controllers.MedicalTestInfo;
import Hospital.Controllers.MedicalTestResultController;
import Hospital.Controllers.NurseController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.IllegalInfo;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import HospitalUI.MainUI.UtilsUI;
import java.util.Scanner;

public class EnterMedicalTestResultUI {

    MedicalTestResultController mc;
    NurseController nc;
    WorldController wc;

    public EnterMedicalTestResultUI(WorldController wc, NurseController nc) {
        this.nc = nc;
        this.wc = wc;
        this.mc = new MedicalTestResultController(wc, nc);
    }

    public void run(Scanner sc) throws NotLoggedInException {
        MedicalTestInfo[] infos = mc.getOpenMedicalTests();
        int chosen = UtilsUI.selectCommand(sc, infos);
        if (chosen == 0) {
            return;
        }

        ArgumentList args;
        try {
            args = mc.getArguments(infos[chosen - 1]);
        } catch (IllegalInfo ex) {
            throw new Error("This should not happen, UI should be build solid, check code:\n" + ex);
        }
        UtilsUI.answerArguments(sc, args.getPublicArguments());

        try {
            String str = mc.enterResult(infos[chosen - 1], args);
            System.out.println(str);
        } catch (WrongArgumentListException ex) {
            ex.printStackTrace();
            throw new Error("This should not happen, UI should be build solid, check code:\n" + ex);
        } catch (InvalidArgumentException ex) {
            throw new Error("This should not happen, UI should be build solid, check code:\n" + ex);
        } catch (IllegalInfo ex) {
            throw new Error("This should not happen, UI should be build solid, check code:\n" + ex);
        }
    }
}
