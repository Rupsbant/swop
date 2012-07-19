package Hospital.MedicalTest;

import Hospital.Argument.Argument;
import Hospital.Argument.PriorityArgument;
import Hospital.Factory.Command;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Patient.Patient;
import Hospital.Schedules.AppointmentCommand;
import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.ScheduleGroups.SingleSchedulableGroup;
import Hospital.Utils;
import Hospital.World.World;
import java.util.Arrays;
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
     * the command for creating the appointment for the medical test created by this command 
     */
    private AppointmentCommand appC;

    /**
     * Constructor
     * @param world the world in which this command must be executed
     * @param openedPatient the patient for whom the test must be created
     * @param medicalTestName the type of test to be created
     * @param args arguments to the creation of this test
     * @throws WrongArgumentListException the given arguments do not match the test to be created
     * @throws ArgumentNotAnsweredException one or more of the arguments are not answered
     * @throws ArgumentConstraintException one or more of the arguments are incorrect
     * @throws ArgumentIsNullException the array of PublicArguments is null
     * @throws NotAFactoryException the given type of medical test does not exist in the world
     */
    public MedicalTestCommand(World world, Patient openedPatient, String medicalTestName, Argument[] args)
            throws WrongArgumentListException, InvalidArgumentException, NotAFactoryException {
        MedicalTestFactory factory = world.getFactory(MedicalTestFactory.class, medicalTestName);
        if(args == null){
            throw new ArgumentIsNullException("ArgumentList is null");
        }
        made = factory.make(Arrays.copyOf(args, args.length-1));
        ScheduleGroup pat = new SingleSchedulableGroup(openedPatient);
        
        Priority p = Utils.getAnswer(PriorityArgument.class, "the priority", args[args.length-1]);
        appC = new AppointmentCommand(world, made, Collections.singletonList(pat), p);

        toAdd = openedPatient;
    }

    /**
     * Executes the command by creating the medical test
     * @return the details of the created medical test
     * @throws CannotDoException the command was already executed
     */
    public String execute() throws CannotDoException {
        if (done) {
            throw new CannotDoException("Cannot undo command, not done yet.");
        }
        if (made.isResultEntered()) {
            throw new CannotDoException("Results already entered, cannot undo.");
        }
        appC.execute();
        toAdd.addMedicalTest(made);
        done = true;
        return made.advancedString();
    }

    /**
     * Undoes the execution of this command
     * @throws CannotDoException the command was not yet executed
     * @see Hospital.Factory.Command#undo()
     */
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
