package Hospital.People;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Controllers.CampusController;
import Hospital.Controllers.DoctorController;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Patient.DiagnosisSecondOpinion;
import Hospital.Patient.Patient;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Constraints.Preference.HasPreference;
import Hospital.Schedules.Constraints.Preference.Preference;
import Hospital.Schedules.Constraints.Preference.PreferenceConstraint;
import Hospital.Schedules.SchedulableVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * A doctor, creates diagnoses, prescribes treatments and medical tests
 */
public class Doctor extends Staff implements HasPreference {

    /**
     * the patient whose file the doctor currently is viewing
     */
    private Patient openedPatient;
    /**
     * the diagnoses which require a second opinion from this doctor
     */
    private ArrayList<DiagnosisSecondOpinion> secondOpinions;
    /**
     * the preference-pattern of moving between campuses
     */
    private Preference preference;
    /**
     * the command-history of the doctor
     */
    private CommandHistory history;

    /**
     * Constructor
     * @param name the name of this doctor
     * @throws ArgumentConstraintException the name was empty
     * @throws ArgumentIsNullException the name was null
     */
    public Doctor(String name) throws ArgumentConstraintException, ArgumentIsNullException {
        super(name);
        this.openedPatient = null;
        this.secondOpinions = new ArrayList<DiagnosisSecondOpinion>();
        this.history = new CommandHistory();
    }

    /**
     * Creates a DoctorController of this person
     * @param cc the Campus from where is logged in
     * @return A DoctorController with this doctor as primary actor.
     */
    @Override
    public DoctorController login(CampusController cc) {
        return new DoctorController(this, cc);
    }

    /**
     * Returns this Doctor's role.
     * @return String The role of this Doctor.
     */
    @Override
    public StaffRole getRole() {
        return StaffRole.Doctor;
    }

    /**
     * Opens a given patient.
     * @param openPatient Patient to open.
     * @throws NullPointerException If given patient was null.
     */
    public void openPatient(Patient openPatient) throws NullPointerException {
        if (openPatient == null) {
            throw new NullPointerException("Can't open patient 'Null'");
        }
        this.openedPatient = openPatient;
    }

    /**
     * Closes the PatientFile.
     */
    public void closePatient() {
        this.openedPatient = null;
    }

    /**
     * Returns the opened patient.
     * @return The opened patient.
     * @throws NoOpenedPatientFileException If no patient was opened.
     */
    public Patient getOpenedPatient() throws NoOpenedPatientFileException {
        checkOpenedPatient();
        return openedPatient;
    }

    /**
     * Returns true if a patient was opened.
     * @return true if a patient was opened, false otherwise.
     */
    public boolean hasOpenedPatient() {
        return openedPatient != null;
    }

    /**
     * Throws an exception if no patient was opened, succeedes otherwise.
     * @throws NoOpenedPatientFileException If no Patient was opened.
     */
    public void checkOpenedPatient() throws NoOpenedPatientFileException {
        if (!hasOpenedPatient()) {
            throw new NoOpenedPatientFileException();
        }
    }

    /**
     * Adds a DiagnosisSecondOpinion to the todo-list.
     * @param secondOpinion Diagnosis to add.
     */
    public void addSecondOpinions(DiagnosisSecondOpinion secondOpinion) {
        this.secondOpinions.add(secondOpinion);
    }

    /**
     * Removes a DiagnosisSecondOpinion from the todo-list.
     * @param secondOpinion The Diagnosis to remove.
     * @return true if the Diagnosis was found.
     */
    public boolean removeSecondOpinion(DiagnosisSecondOpinion secondOpinion) {
        return this.secondOpinions.remove(secondOpinion);
    }

    /**
     * Returns the todo-list of second-opinions.
     * @return List of DiagnosisSecondOpinion.
     */
    public ArrayList<DiagnosisSecondOpinion> getSecondOpinions() {
        return (ArrayList<DiagnosisSecondOpinion>) secondOpinions.clone();
    }

    /**
     * This method makes this object visit the constraints to approve them as a Schedulable and as a Doctor.
     * @param tf The TimeFrame during which the constraints must be checked.
     * @param tfContstraints The list of constraints.
     * @return The constraints for simpler code : doctor.setValidTimeFrame(tf, tfc).isAccepted();.
     */
    @Override
    public void visitConstraint(SchedulableVisitor tfConstraints) {
        super.visitConstraint(tfConstraints);
        tfConstraints.setDoctor(this);
    }

    /**
     * Returns the constraints a appointment needs to satisfy for this doctor
     * @return a new PreferenceConstraint.
     */
    @Override
    public List<TimeFrameConstraint> getConstraints() {
        List<TimeFrameConstraint> constraints = super.getConstraints();
        constraints.add(new PreferenceConstraint());
        return constraints;
    }

    /**
     * sets the preference of this doctor
     */
    public Doctor setPreference(Preference preference) {
        this.preference = preference;
        return this;
    }

    /**
     * returns the preference of this doctor.
     * @return Prefecence, the current preference for handling moving between campuses
     */
    public Preference getPreference() {
        return preference;
    }

    /**
     * returns the history of the doctor's actions
     * @return CommandHistory, the full history of this doctor's handling
     */
    public CommandHistory getHistory() {
        return history;
    }
}
