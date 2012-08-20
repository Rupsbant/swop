package Hospital.Controllers;

import Hospital.Argument.ItemArgument;
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
 * This controller enables the following usecases: 
 * Order Treatment: returns a list of all untreated diagnoses of the current patient.
 *                  returns an ItemArgument to select the items from.
 *                  makes the right treatment.
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
    
    /**
     * makes a new surgery treatment. 
     * @param diagnosis The diagnosis to add the treatment to.
     * @param description The description of the surgery
     * @param p The priority to schedule this treatment with
     * @return a description of the created surgery, with the time of the appointment if it could be scheduled
     * @throws NoOpenedPatientFileException : if no patientfile was opened
     * @throws NotLoggedInException : if the doctor logged out or something
     * @throws InvalidDiagnosisException : if the diagnosis was not found in the current patient's file
     * @throws InvalidArgumentException : if some parameter was null.
     */
    public String makeSurgery(DiagnosisInfo diagnosis, String description, Priority p) 
            throws NoOpenedPatientFileException, NotLoggedInException, InvalidDiagnosisException, InvalidArgumentException{
        dc.checkLoggedIn();
        Patient openedPatient = dc.getUser().getOpenedPatient();
        World world = wc.getWorld();
        return TreatmentMakers.SINGLETON.makeSurgery(dc.getUser(), description, openedPatient, diagnosis, world, p);
    }
    /**
     * make a new cast-treatment
     * @param diagnosis the diagnosis to add the treatment to
     * @param description the description of the treatment
     * @param duration the length the cast must stay on
     * @param p the priority to schedule the treatment with
     * @return a description of the generated treatment optionally with the appointment
     * @throws NoOpenedPatientFileException : if no patientfile was opened
     * @throws NotLoggedInException : if the doctor logged out or something
     * @throws InvalidDiagnosisException : if the diagnosis was not found in the current patient's file
     * @throws InvalidArgumentException : if some parameter was null.
     */
    public String makeCast(DiagnosisInfo diagnosis, String description, int duration, Priority p) 
            throws NoOpenedPatientFileException, NotLoggedInException, InvalidDiagnosisException, InvalidArgumentException{
        dc.checkLoggedIn();
        Patient openedPatient = dc.getUser().getOpenedPatient();
        World world = wc.getWorld();
        return TreatmentMakers.SINGLETON.makeCast(dc.getUser(), description, duration, openedPatient, diagnosis, world, p);
    }
    
    /**
     *  make a new MedicationTreatment
     * @param diagnosis the diagnosis to add the treatment to
     * @param description the description of the treatment
     * @param sensitivity if the patient is sensitive to medication
     * @param items the medicine to give to the patient as treatment
     * @param p the priority to schedule the treatment with
     * @return a description of the generated treatment, optionally with a appointmentdescription
     * @throws NoOpenedPatientFileException : if no patientfile was opened
     * @throws NotLoggedInException : if the doctor logged out or something
     * @throws InvalidDiagnosisException : if the diagnosis was not found in the current patient's file
     * @throws InvalidArgumentException : if some parameter was null.
     */
    public String makeMedication(DiagnosisInfo diagnosis, String description, boolean sensitivity, String items, Priority p) 
            throws NoOpenedPatientFileException, NotLoggedInException, InvalidDiagnosisException, InvalidArgumentException{
        dc.checkLoggedIn();
        Patient openedPatient = dc.getUser().getOpenedPatient();
        World world = wc.getWorld();
        return TreatmentMakers.SINGLETON.makeMedication(dc.getUser(), description, sensitivity, items, openedPatient, diagnosis, world, p);
    }
    
    /**
     * returns a ItemsArgument filled with the items this hospital has
     * @return a new ItemArgument to select items from.
     */
    public ItemArgument getItemArgument(){
        ItemArgument itemArgument = new ItemArgument("Select the items you wish:");
        itemArgument.setWorld(wc.getWorld());
        return itemArgument;
    }
}