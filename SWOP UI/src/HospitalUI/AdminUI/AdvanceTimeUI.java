package HospitalUI.AdminUI;

import java.util.List;
import java.util.Scanner;

import Hospital.Argument.PublicArgument;
import Hospital.Argument.TimeArgument;
import Hospital.Controllers.AdministratorController;
import Hospital.Controllers.ArgumentList;
import Hospital.Controllers.MedicalTestInfo;
import Hospital.Controllers.MedicalTestResultController;
import Hospital.Controllers.NurseController;
import Hospital.Controllers.TreatmentInfo;
import Hospital.Controllers.TreatmentResultController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.IllegalInfo;
import Hospital.Exception.InvalidTimeException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotLoggedInException;
import Hospital.People.LoginInfo;
import Hospital.World.Time;
import Hospital.World.TimeUtils;
import HospitalUI.MainUI.UtilsUI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdvanceTimeUI {

    AdministratorController ac;
    WorldController wc;

    public AdvanceTimeUI(AdministratorController ac, WorldController wc) {
        this.ac = ac;
        this.wc = wc;
    }

    public void run(Scanner sc) {
        System.out.println("Current Time: " + wc.getTime());
        TimeArgument arg = ac.getTimeArgument();
        PublicArgument[] args = new PublicArgument[1];
        args[0] = arg;
        UtilsUI.answerArguments(sc, args);
        try {
            ac.advanceTime(wc, arg);
        } catch (InvalidTimeException e) {
            System.out.println("You must enter a time in the future!");
            return;
        }
        //This code is illogical. That's why it is enforced in the UI and is not part of the system.
        List<LoginInfo> logins = wc.getLogins();
        for (LoginInfo loginInfo : logins) {
            if (loginInfo.getRole().equals("Nurse")) {
                try {
                    NurseController nc = (NurseController) wc.login(wc.getCampuses().get(0), loginInfo);
                    enterMedicalTestResults(sc, nc);
                    enterTreatmentResults(sc, nc);
                } catch (InvalidArgumentException ex) {
                    Logger.getLogger(AdvanceTimeUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotLoggedInException ex) {
                    Logger.getLogger(AdvanceTimeUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalInfo ex) {
                    Logger.getLogger(AdvanceTimeUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoPersonWithNameAndRoleException ex) {
                    Logger.getLogger(AdvanceTimeUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void enterTreatmentResults(Scanner sc, NurseController nc) throws NotLoggedInException, IllegalInfo, InvalidArgumentException {
        ArgumentList args2;
        Time time = wc.getTime();
        Time startDay = TimeUtils.getStartOfDay(time);
        TreatmentResultController mc = new TreatmentResultController(wc, nc);
        TreatmentInfo[] infos = mc.getOpenTreatments();
        for (int i = 0; i < infos.length; i++) {
            if (infos[i].compareTimeAppointment(startDay) < 0) {
                UtilsUI.space();
                System.out.println("Enter information of treatment: ");
                System.out.println(infos[i]);
                System.out.println("Press enter");

                args2 = mc.getArguments(infos[i]);
                UtilsUI.answerArguments(sc, args2.getPublicArguments());
                try {
                    String str = mc.enterResult(infos[i], args2);
                    System.out.println(str);
                } catch (ArgumentConstraintException ex) {
                    System.out.println("ArgumentConstraintException: " + ex);
                    i--;
                }
            }
        }
    }

    private void enterMedicalTestResults(Scanner sc, NurseController nc) throws InvalidArgumentException, NotLoggedInException, IllegalInfo {
        ArgumentList args2;
        Time time = wc.getTime();
        Time startDay = TimeUtils.getStartOfDay(time);
        MedicalTestResultController mc = new MedicalTestResultController(wc, nc);
        MedicalTestInfo[] medicalTests = mc.getOpenMedicalTests();
        for (int i = 0; i < medicalTests.length; i++) {
            if (medicalTests[i].compareTimeAppointment(startDay) <= 0) {
                UtilsUI.space();
                System.out.println("Enter information of medicalTest: ");
                System.out.println(medicalTests[i].advancedString());
                System.out.println("Press enter");

                args2 = mc.getArguments(medicalTests[i]);
                UtilsUI.answerArguments(sc, args2.getPublicArguments());

                try {
                    String str = mc.enterResult(medicalTests[i], args2);
                    System.out.println(str);
                } catch (ArgumentConstraintException ex) {
                    System.out.println("ArgumentConstraintException: " + ex);
                    i--;
                }
            }
        }
    }
}
