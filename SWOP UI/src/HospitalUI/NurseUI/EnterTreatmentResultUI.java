package HospitalUI.NurseUI;

import Hospital.Argument.PublicArgument;
import Hospital.Controllers.TreatmentResultController;
import Hospital.Controllers.NurseController;
import Hospital.Controllers.TreatmentInfo;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.IllegalInfo;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import HospitalUI.MainUI.UtilsUI;
import java.util.Scanner;

public class EnterTreatmentResultUI {

    TreatmentResultController mc;
    NurseController nc;
    WorldController wc;

    public EnterTreatmentResultUI(WorldController wc, NurseController nc) throws ArgumentIsNullException {
        setNurseController(nc);
        setWorldController(wc);
        this.mc = new TreatmentResultController(wc, nc);

    }

    private void setNurseController(NurseController nc) throws ArgumentIsNullException {
        if (nc == null) {
            throw new ArgumentIsNullException("NurseController was null");
        }
        this.nc = nc;
    }

    private void setWorldController(WorldController wc) throws ArgumentIsNullException {
        if (wc == null) {
            throw new ArgumentIsNullException("WorldController was null");
        }
        this.wc = wc;
    }

    public void run(Scanner sc) throws NotLoggedInException {
        TreatmentInfo[] infos = mc.getOpenTreatments();
        int chosen = UtilsUI.selectCommand(sc, infos);
        if (chosen == 0) {
            return;
        }

        PublicArgument[] args;
        try {
            args = mc.getArguments(infos[chosen - 1]);
        } catch (IllegalInfo ex) {
            throw new Error("This should not happen, UI should be build solid, check code:\n" + ex);
        }
        UtilsUI.answerArguments(sc, args);
        
        String str;
        try {
                str = mc.enterResult(infos[chosen - 1], args);
        } catch (ArgumentConstraintException ex) {
                str = "Arguments didn't satisfy contstraints: "+ ex.getMessage();
        } catch (WrongArgumentListException ex) {
            throw new Error("This should not happen, UI should be build solid, check code:\n" + ex);
        } catch (InvalidArgumentException ex) {
            throw new Error("This should not happen, UI should be build solid, check code:\n" + ex);
        } catch (IllegalInfo ex) {
            throw new Error("This should not happen, UI should be build solid, check code:\n" + ex);
        }
        System.out.println(str);

    }
}
