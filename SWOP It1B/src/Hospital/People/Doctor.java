package Hospital.People;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Command.NotDoneException;
import Hospital.Controllers.CampusController;
import Hospital.Controllers.CommandInfo;
import Hospital.Controllers.DoctorController;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Factory.Command;
import Hospital.Patient.DiagnosisSecondOpinion;
import Hospital.Patient.Patient;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.Constraints.Preference.HasPreference;
import Hospital.Schedules.Constraints.Preference.Preference;
import Hospital.Schedules.Constraints.Preference.PreferenceConstraint;
import Hospital.Schedules.TimeFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: DOCTOR INJECTION!
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
     * commandinfos from recent done commands
     */
    private ArrayList<CommandInfo> recent = new ArrayList<CommandInfo>();
    /**
     * commandinfos from recent undone commands
     */
    private ArrayList<CommandInfo> undone = new ArrayList<CommandInfo>();
    /**
     * the preference-pattern of moving between campuses
     */
    private Preference preference;

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
    public String getRole() {
        return "Doctor";
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
     * getrecent
     * @return recent commands
     */
    public ArrayList<CommandInfo> getRecent() {
        return recent;
    }

    /**
     * getundone
     * @return undone commands
     */
    public ArrayList<CommandInfo> getUndone() {
        return undone;
    }

    /**
     * get recent commands
     * @return latest 20 recent commands
     */
    public CommandInfo[] getRecentCommands() {
        return Arrays.copyOf(recent.toArray(new CommandInfo[0]), Math.min(recent.size(), 20));
    }

    /**
     * getundonecommands
     * @return latest 5 undone commands
     */
    public CommandInfo[] getUndoneCommands() {
        return Arrays.copyOf(undone.toArray(new CommandInfo[0]), Math.min(undone.size(), 5));
    }

    /**
     * adds a command to recent commands
     * @param comm command to be added
     */
    public void addCommand(Command comm) {
        recent.add(new CommandInfo(comm));
    }

    /**
     * undo's a command
     * @param commInfo commandinfo to undo
     * @param command command to undo
     * @throws CannotDoException when command cannot be undone
     * @return the details of undoing the command
     */
    public String undoCommand(CommandInfo commInfo, Command command) throws CannotDoException {
        if (!recent.remove(commInfo)) {
            throw new IllegalArgumentException("CommandInfo not found");
        }
        try {
            String string = command.undo();
            undone.add(commInfo);
            return string;
        } catch (NotDoneException ex) {
            throw new RuntimeException("Command was done");
        }
    }

    /**
     * redo's a command
     * @param commInfo commandinfo to redo
     * @param command command to redo
     * @throws CannotDoException when command cannot be redone
     * @return the details of redoing the command
     */
    public String redoCommand(CommandInfo commInfo, Command command) throws CannotDoException {
        if (!undone.remove(commInfo)) {
            throw new IllegalArgumentException("CommandInfo not found");
        }
        String string = command.execute();
        recent.add(commInfo);
        return string;
    }
    
    /**
     * This method makes this object visit the constraints to approve them as a Schedulable and as a Doctor.
     * @param tf The TimeFrame during which the constraints must be checked.
     * @param tfContstraints The list of constraints.
     * @return The constraints for simpler code : doctor.setValidTimeFrame(tf, tfc).acceptAll();.
     */
    @Override
    public TimeFrameConstraint setValidTimeFrame(TimeFrame tf, TimeFrameConstraint tfConstraints){
        super.setValidTimeFrame(tf, tfConstraints);
        tfConstraints.setValidTimeFrameDoctor(tf, this);
        return tfConstraints;
    }

    /**
     * Returns the constraints a appointment needs to satisfy for this doctor
     * @return a new PreferenceConstraint.
     */
    @Override
    public TimeFrameConstraint getConstraints() {
        return super.getConstraints().addConstraintList(new PreferenceConstraint());
    }

    /**
     * sets the preference of this doctor
     */
    public Doctor setPreference(Preference preference){
        this.preference = preference;
        return this;
    }
 
    /**
     * returns the preference of this doctor.
     * @return
     */
    public Preference getPreference() { 
        return preference;
    }
}
