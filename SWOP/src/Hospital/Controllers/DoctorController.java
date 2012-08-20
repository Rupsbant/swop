package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Argument.ListArgument;
import Hospital.Exception.CannotFindException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Patient.DiagnosisInfo;
import java.util.ArrayList;

import Hospital.Exception.Patient.CannotDischargeException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Factory.Command;
import Hospital.Patient.DiagnosisSecondOpinion;
import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.Schedules.Constraints.Preference.Preference;

/**
 * As the LoginController for a Doctor, this class represents the Doctor
 * when he/she is logged in and provides access to functionality related to
 * this Doctor.
 * 
 * This controller enables the following usecases : 
 * Open patientfile.
 * Set preference.
 * Undo and redo actions.
 */
@SystemAPI
public class DoctorController extends LoginController<Doctor> {

    /**
     * Constructor
     * @param d The Doctor to log in
     * @param cc the Campus from where is logged in
     */
    @SystemAPI
    public DoctorController(Doctor d, CampusController cc) {
        super(d, cc);
    }

    /**
     * Returns true if the doctor has opened a patientfile.
     * @return Returns true if the doctor has opened a patientfile.
     */
    @SystemAPI
    public boolean hasOpenedPatientFile() {
        return super.getUser().hasOpenedPatient();
    }

    /**
     * Returns true if the opened patient was not discharged.
     * @return Returns true if the patient was not discharged.
     * @throws NoOpenedPatientFileException if there was no opened patientfile
     */
    @SystemAPI
    public boolean openedPatientFileNotDischarged() throws NoOpenedPatientFileException {
        return !super.getUser().getOpenedPatient().isDischarged();
    }

    /**
     * Returns the name of the opened patient
     * @return Returns the name of the opened patient
     * @throws NoOpenedPatientFileException if no patientfile was opened
     */
    @SystemAPI
    public String getOpenedPatientName() throws NoOpenedPatientFileException {
        return super.getUser().getOpenedPatient().getName();
    }

    /**
     * This opens a patientFile in the system and returns an information-object of it.
     * @param chosenPatient The name of the patient.
     * @param wc The WorldController of the world this patient and doctor belong to.
     * @return An information-object about the patient
     * @throws NoPersonWithNameAndRoleException if no patient was found with the given name
     * @throws NotLoggedInException if this controller was logged out
     */
    @SystemAPI
    public PatientFile consultPatientFile(String chosenPatient, WorldController wc) throws NoPersonWithNameAndRoleException, NotLoggedInException {
        super.checkLoggedIn();
        Patient patient = wc.getWorld().getPersonByName(Patient.class, chosenPatient);
        super.getUser().openPatient(patient);
        try {
            return super.getUser().getOpenedPatient().getPatientFile();
        } catch (NoOpenedPatientFileException ex) {
            throw new Error("A patient was just opened, this can't happen");
        }
    }

    /**
     * Closes the currently opened patientfile
     */
    @SystemAPI
    public void closePatientFile() {
        super.getUser().closePatient();
    }

    /**
     * Discharges the patient whose file the doctor currently has opened.
     * @throws CannotDischargeException if the patient has unfinished test or treatments, or an unapproved diagnosis
     * @throws NoOpenedPatientFileException if the doctor has no patient opened at the time
     */
    @SystemAPI
    public void dischargePatient() throws CannotDischargeException, NoOpenedPatientFileException {
        if (getUser().getOpenedPatient() == null) {
            throw new NoOpenedPatientFileException();
        }
        super.getUser().getOpenedPatient().dischargePatient();
    }

    /**
     * This method returns true if the current patient has untreated diagnoses
     * @return true if opened patient has untreated diagnoses, false otherwise
     * @throws NoOpenedPatientFileException if no patientfile is viewed
     */
    @SystemAPI
    public boolean openedPatientHasUntreatedDiagnosis() throws NoOpenedPatientFileException {
        return getUser().getOpenedPatient().hasUntreatedDiagnoses();
    }

    /**
     * This method returns a description-object of all unapproved second opinions for the current doctor
     * @return Array of unapproved secondopinions
     */
    @SystemAPI
    public DiagnosisInfo[] getUnapprovedSecondOpinions() {
        ArrayList<DiagnosisSecondOpinion> secondOpinionList = getUser().getSecondOpinions();
        ArrayList<DiagnosisInfo> listOfSecondOpinions = new ArrayList<DiagnosisInfo>();
        for (DiagnosisSecondOpinion diagnosis : secondOpinionList) {
            if (!diagnosis.isApproved()) {
                listOfSecondOpinions.add(new DiagnosisInfo(diagnosis));
            }
        }
        return listOfSecondOpinions.toArray(new DiagnosisInfo[0]);
    }

    /**
     * get recent commands
     * @return recent commands
     */
    @SystemAPI
    public CommandInfo[] getRecentCommands() {
        return this.getUser().getHistory().getRecentCommands();
    }

    /**
     * get undone commands
     * @return undone commands
     */
    @SystemAPI
    public CommandInfo[] getUndoneCommands() {
        return this.getUser().getHistory().getUndoneCommands();
    }

    /**
     * adds a command
     * @param comm command to be added
     */
    protected String addCommand(Command comm) throws CannotDoException {
        return this.getUser().getHistory().addCommand(comm);
    }

    /**
     * undo's a command
     * @param commInfo commandinfo to undo
     * @throws CannotDoException when command cannot be undone
     * @return the result of undoing the command
     */
    @SystemAPI
    public String undoCommand(CommandInfo commInfo) throws CannotDoException {
        Command command = commInfo.getCommand();
        return this.getUser().getHistory().undoCommand(commInfo, command);
    }

    /**
     * redo a command
     * @param commInfo commandinfo to redo
     * @throws CannotDoException when command cannot be redone
     * @return the result of redoing the command 
     */
    @SystemAPI
    public String redoCommand(CommandInfo commInfo) throws CannotDoException {
        Command command = commInfo.getCommand();
        return this.getUser().getHistory().redoCommand(commInfo, command);
    }

    /**
     * Sets the preference for handling moving between two Campuses
     * @param wc the WorldController to get the preferences from.
     * @param arg the description of the preference
     * @throws CannotFindException if the preference description was not found
     */
    @SystemAPI
    public void setPreference(WorldController wc, ListArgument<String> arg) throws CannotFindException {
        Preference pref = wc.getWorld().getPreference(arg.getAnswer());
        pref.makeThisAsPreference(this.getUser());
    }
}
