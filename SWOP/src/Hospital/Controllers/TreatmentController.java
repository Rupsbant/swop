package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Patient.DiagnosisInfo;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Patient.Patient;
import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.Treatments.TreatmentMakers;
import Hospital.World.World;

/**
 * Used for treatment-related actions
 */
@SystemAPI
public class TreatmentController {

    /**
     * The doctor which performs these actions
     */
    private DoctorController dc;
    /**
     * The world in which these actions are performed
     */
    private WorldController wc;

    /**
     * Constructor
     * @param wc the world in which these actions are performed
     * @param dc the doctor which performs these actions
     */
    @SystemAPI
    public TreatmentController(WorldController wc, DoctorController dc) {
        this.dc = dc;
        this.wc = wc;
    }

    /**
     * Gets a list of untreated diagnoses for the currently opened patient
     * @return an array of DiagnosisInfos representing untreated diagnoses
     * @throws NotLoggedInException the doctor is not logged in
     * @throws NoOpenedPatientFileException the doctor has no patientfile opened
     */
    @SystemAPI
    public DiagnosisInfo[] getUntreatedDiagnoses() throws NotLoggedInException, NoOpenedPatientFileException {
        dc.checkLoggedIn();
        return dc.getUser().getOpenedPatient().getUntreatedDiagnoses();
    }
    
    public String makeSurgery(DiagnosisInfo diagnosis, String description, Priority p) 
            throws NoOpenedPatientFileException, NotLoggedInException, InvalidDiagnosisException, InvalidArgumentException{
        dc.checkLoggedIn();
        Patient openedPatient = dc.getUser().getOpenedPatient();
        World world = wc.getWorld();
        return TreatmentMakers.SINGLETON.makeSurgery(dc.getUser(), description, openedPatient, diagnosis, world, p);
    }
    
    public String makeCast(DiagnosisInfo diagnosis, String description, int duration, Priority p) 
            throws NoOpenedPatientFileException, NotLoggedInException, InvalidDiagnosisException, InvalidArgumentException{
        dc.checkLoggedIn();
        Patient openedPatient = dc.getUser().getOpenedPatient();
        World world = wc.getWorld();
        return TreatmentMakers.SINGLETON.makeCast(dc.getUser(), description, duration, openedPatient, diagnosis, world, p);
    }
    
    public String makeMedication(DiagnosisInfo diagnosis, String description, boolean sensitivity, String items, Priority p) 
            throws NoOpenedPatientFileException, NotLoggedInException, InvalidDiagnosisException, InvalidArgumentException{
        dc.checkLoggedIn();
        Patient openedPatient = dc.getUser().getOpenedPatient();
        World world = wc.getWorld();
        return TreatmentMakers.SINGLETON.makeMedication(dc.getUser(), description, sensitivity, items, openedPatient, diagnosis, world, p);
    }
}