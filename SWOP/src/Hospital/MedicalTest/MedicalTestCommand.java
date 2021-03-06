package Hospital.MedicalTest;

import Hospital.Interfaces.Command;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Patient.Patient;
import Hospital.Schedules.AppointmentCommand;
import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.ScheduleGroups.SingleSchedulableGroup;
import Hospital.World.World;
import java.util.Collections;

/**
 * A command to create a medical test
 */
public class MedicalTestCommand implements Command {

    /**
     * the medical test resulting from executing this test
     */
    private MedicalTest made;
    /**
     * the patient to which this test must be added
     */
    private Patient toAdd;
    /**
     * indicates whether this command is already executed
     */
    private boolean done;
    /**
     * the command for creating the appointment for the medical test created by
     * this command
     */
    private AppointmentCommand appC;

    /**
     * Constructor
     *
     * @param world the world in which this command must be executed
     * @param openedPatient the patient for whom the test must be created
     * @param med The medicaltest to add to the patient
     * @param priority The priority at which the Appointment must be made
     * @throws InvalidArgumentException If some parameter was not as expected:
     * null or some constraint
     */
    public MedicalTestCommand(World world, Patient openedPatient, MedicalTest med, Priority priority)
            throws InvalidArgumentException {
        this.made = med;
        ScheduleGroup pat = new SingleSchedulableGroup(openedPatient);
        appC = new AppointmentCommand(world, made, Collections.singletonList(pat), priority);
        toAdd = openedPatient;
    }

    /**
     * Executes the command by creating the medical test
     *
     * @return the details of the created medical test
     * @throws CannotDoException the command was already executed
     */
    @Override
    public String execute() throws CannotDoException {
        if (done) {
            throw new CannotDoException("Cannot undo command, not done yet.");
        }
        if (made.isResultEntered()) {
            throw new CannotDoException("Results already entered, cannot undo.");
        }
        String out = appC.execute();
        toAdd.addMedicalTest(made);
        done = true;
        return out + "\n" + made.advancedString();
    }

    /**
     * Undoes the execution of this command
     *
     * @throws CannotDoException the command was not yet executed
     * @see Hospital.Factory.Command#undo()
     */
    @Override
    public String undo() throws CannotDoException {
        if (!done) {
            throw new CannotDoException("Cannot undo Command");
        }
        appC.undo();
        toAdd.removeMedicalTest(made);
        done = false;
        return "Undone:\n" + this.toString();
    }

    /**
     * @see Hospital.Factory.Command#isDone()
     */
    @Override
    public boolean isDone() {
        return done;
    }

    /**
     * @return the details of the medical test and the patient it was made for
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return made.toString() + "\nPatient: " + toAdd.toString();
    }
}
