package HospitalUI.DoctorUI;

import Hospital.Exception.Patient.NoOpenedPatientFileException;
import java.util.Scanner;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.PatientFile;
import Hospital.Controllers.WorldController;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotLoggedInException;
import HospitalUI.MainUI.UtilsUI;

public class OpenPatientFileUI {

    private DoctorController dc;
    private WorldController wc;

    public OpenPatientFileUI(DoctorController dc, WorldController wc) {
        this.dc = dc;
        this.wc = wc;
    }

    public void run(Scanner sc) throws NotLoggedInException, NoOpenedPatientFileException {
        String[] patients = wc.getNotDischargedPatients().toArray(new String[0]);
        int chosenInt = UtilsUI.selectCommand(sc, patients);
        if (chosenInt == 0) {
            return;
        }
        String chosenPatient = patients[chosenInt - 1];
        PatientFile patientfile = null;
        try {
            patientfile = dc.consultPatientFile(chosenPatient, wc);
        } catch (NoPersonWithNameAndRoleException e) {
            System.out.println("No patient with that name found, should not happen.");
        }
        String[] results = patientfile.getPatientFileList();
        if(results.length == 0){
            System.out.println("Empty patientFile.");
            return;
        }
        while(true){
            int chosen = UtilsUI.selectCommand(sc, results);
            if(chosen == 0){
                break;
            }
            System.out.println(patientfile.getAdvancedInformation(chosen-1));
        }
    }
}
