package HospitalUI.NurseUI;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import java.util.Scanner;
import Hospital.Controllers.NurseController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.NotLoggedInException;
import HospitalUI.MainUI.UtilsUI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NurseUI {

    NurseController nc;
    WorldController wc;

    public NurseUI(NurseController nc, WorldController wc) throws ArgumentIsNullException {
        setNurseController(nc);
        setWorldController(wc);
    }

    private void setWorldController(WorldController wc) throws ArgumentIsNullException {
        if(wc == null){
            throw new ArgumentIsNullException("WorldController was null");
        }
        this.wc = wc;
    }

    private void setNurseController(NurseController nc) throws ArgumentIsNullException {
        if(nc == null){
            throw new ArgumentIsNullException("NurseController was null");
        }
        this.nc = nc;
    }

    public void run(Scanner sc) throws NotLoggedInException {
        int pos = -1;
        while (pos != 0) {
            try {
                String[] commands = new String[]{"Register Patient", "Enter Medical Test Result", "Enter Treatment Result"};
                pos = UtilsUI.selectCommand(sc, commands);
                switch (pos) {
                    case 0:
                        nc.logout();
                        break;
                    case 1:
                        RegisterPatientUI patientUI = new RegisterPatientUI(nc, wc);
                        patientUI.run(sc);
                        break;
                    case 2:
                        EnterMedicalTestResultUI medResultUI = new EnterMedicalTestResultUI(wc, nc);
                        medResultUI.run(sc);
                        break;
                    case 3:
                        EnterTreatmentResultUI treatmentResultUI = new EnterTreatmentResultUI(wc, nc);
                        treatmentResultUI.run(sc);
                        break;
                }
            } catch (ArgumentIsNullException ex) {
                Logger.getLogger(NurseUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
