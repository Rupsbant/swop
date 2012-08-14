package Hospital.Treatments;

import Hospital.Factory.Command;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.WrongTreatmentException;
import Hospital.Patient.DiagnosisInfo;
import Hospital.Patient.Diagnosis;
import Hospital.Patient.Patient;
import Hospital.Schedules.AppointmentCommand;
import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.ScheduleGroups.SingleSchedulableGroup;
import Hospital.WareHouse.ItemReservationCommand;
import Hospital.World.World;
import java.util.Collections;

/**
 * A command for creating and adding a Treatment to a Diagnosis, and undoing that
 */
public class TreatmentCommand implements Command {

    /**
     * The Treatment to add to the diagnosis
     */
    private Treatment treatment;
    /**
     * The diagnosis the treatment should be added to
     */
    private Diagnosis diagnosis;
    /**
     * Indicates whether this command has been executed
     */
    private boolean done;

    /**
     * Constructor
     * @param patient the patient to which teh diagnosis belongs
     * @param diagnosis the diagnosis to which to add the new treatment
     * @param world the world in which this takes place
     * @param treat The prepared treatment that this Command must execute.
     * @param p The priority this treatment must be scheduled with.
     * @throws InvalidDiagnosisException diagnosis does not point to an existing diagnosis
     * @throws InvalidArgumentException if a argument is not valid, null, or doesn't satisfy the constraints
     */
    public TreatmentCommand(Patient patient, DiagnosisInfo diagnosis, World world, Treatment treat, Priority p)
            throws InvalidDiagnosisException, InvalidArgumentException {
        this.diagnosis = patient.isValidDiagnosisInfo(diagnosis);
        this.treatment = treat;
        ScheduleGroup core = new SingleSchedulableGroup(patient);
        AppointmentCommand appC = new AppointmentCommand(world, treatment, Collections.singletonList(core), p);

        ItemReservationCommand itemReservationCommand = new ItemReservationCommand(treatment);
        appC.setNext(itemReservationCommand);
        treatment.setItemReservationCommand(itemReservationCommand);
        treatment.setDelayedCommand(appC);
    }

    /**
     * Executes this command by adding the treatment to the diagnosis
     * @throws CannotDoException the command was already executed or the diagnosis already had a treatment
     * @see Hospital.Factory.Command#execute()
     */
    public String execute() throws CannotDoException {
        if (done) {
            throw new CannotDoException("Treatment was already executed");
        }
        try {
            String s = diagnosis.setTreatment(treatment);
            done = true;
            return treatment.toString() + "\n" + s;
        } catch (ArgumentIsNullException ex) {
            throw new Error("Treatment was made, thus not null");
        } catch (CannotChangeException ex) {
            throw new CannotDoException("A Treatment was already made for the Diagnosis");
        }
    }

    /**
     * @throws CannotDoException the command was not yet executed or the treatment is already done
     * @see Hospital.Factory.Command#undo()
     */
    public String undo() throws CannotDoException {
        if (!done) {
            throw new CannotDoException("Treatment was not executed");
        }
        if (treatment.isResultEntered()) {
            throw new CannotDoException("TreatmentResults already entered, cannot undo!");
        }
        try {
            String s = diagnosis.removeTreatment(treatment);
            done = false;
            return "Undone:\n" + this.toString() + "\n" + s;
        } catch (WrongTreatmentException ex) {
            throw new Error("If this was executed, diagnosis should have same treatment");
        }
    }

    /**
     * @see Hospital.Factory.Command#isDone()
     */
    public boolean isDone() {
        return done;
    }

    /**
     * @return the details of the treatment and it's diagnosis
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.treatment.toString() + "\n" + this.diagnosis.toString();
    }
}
