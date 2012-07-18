package HospitalUI.DoctorUI;

import Hospital.Controllers.DoctorController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Patient.CannotDischargeException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import HospitalUI.MainUI.UtilsUI;

import java.util.ArrayList;
import java.util.Scanner;

public class DoctorUI {

    DoctorController dc;
    WorldController wc;

    public DoctorUI(DoctorController dc, WorldController wc) {
        this.dc = dc;
        this.wc = wc;
    }

    public void run(Scanner sc) throws NotLoggedInException {
        int pos = -1;
        while (pos != 0) {
            ArrayList<String> commands = new ArrayList<String>();
            commands.add("Select Preference");
            commands.add("Consult Patient File");
            commands.add("Undo Previous Action");
            commands.add("Approve diagnosis");
            if (dc.hasOpenedPatientFile()) {
                commands.add("Close Patient File");
                try {
                    if (dc.openedPatientFileNotDischarged()) {
                        commands.add("Order Medical Test");
                        commands.add("Enter Diagnosis");
                        commands.add("Discharge Patient");
                        if (dc.openedPatientHasUntreatedDiagnosis()) {
                            commands.add("Prescribe Treatment");
                        }
                    }
                } catch (NoOpenedPatientFileException ex) {
                }
            }
            String[] commands1 = commands.toArray(new String[0]);
            pos = UtilsUI.selectCommand(sc, commands1);
            switch (pos) {
                case 0:
                    break;
                case 1:
                    new SelectPreferenceUI(dc, wc).run(sc);
                    break;
                case 2:
                    try {
                        new OpenPatientFileUI(dc, wc).run(sc);
                    } catch (NoOpenedPatientFileException ex) {
                        throw new RuntimeException("Patientfile should be open, check UI");
                    }
                    break;
                case 3:
                    UndoPreviousActionUI undo = new UndoPreviousActionUI(dc, wc);
                    undo.run(sc);
                    break;
                case 4:
                    ApproveDiagnosisUI diagnosisUI2 = new ApproveDiagnosisUI(dc, wc);
                    diagnosisUI2.run(sc);
                    break;
                case 5:
                    dc.closePatientFile();
                    break;
                case 6:
                    OrderMedicalUI temp = new OrderMedicalUI(dc, wc);
                    try {
                        temp.run(sc);
                    } catch (NoOpenedPatientFileException ex) {
                        throw new RuntimeException("Patientfile should be open, check UI");
                    }
                    break;
                case 7:
                    EnterDiagnosisUI diagnosisUI = new EnterDiagnosisUI(dc, wc);
                    try {
                        diagnosisUI.run(sc);
                    } catch (NoOpenedPatientFileException e) {
                        throw new RuntimeException("Patientfile should be open, check UI");
                    } catch (PatientIsDischargedException e) {
                        throw new RuntimeException("Patient should be discharged, check UI");
                    }
                    break;
                case 8:
                    try {
                        dc.dischargePatient();
                    } catch (NoOpenedPatientFileException ex) {
                        throw new RuntimeException("No patientfile was opened, check UI and concurrency");
                    } catch (CannotDischargeException ex) {
                        System.out.println("The patient has unfinished tests or treatments or an unapproved diagnosis");
                    }
                    break;
                case 9:
                    TreatmentUI treatmentUI = new TreatmentUI(dc, wc);
                    try {
                        treatmentUI.run(sc);
                    } catch (NoOpenedPatientFileException ex) {
                        throw new RuntimeException("Patientfile should be open, check UI");
                    }
                    break;
            }
        }
        dc.logout();
    }
}
