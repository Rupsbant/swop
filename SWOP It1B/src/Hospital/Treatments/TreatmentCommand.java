package Hospital.Treatments;

import Hospital.Factory.Command;
import Hospital.Argument.Argument;
import Hospital.Argument.PriorityArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.WrongTreatmentException;
import Hospital.Patient.DiagnosisInfo;
import Hospital.Patient.Diagnosis;
import Hospital.Patient.Patient;
import Hospital.Schedules.AppointmentCommand;
import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.Schedules.ScheduleGroups.SingleSchedulableGroup;
import Hospital.Utils;
import Hospital.WareHouse.ItemReservationCommand;
import Hospital.World.Campus;
import Hospital.World.World;
import java.util.Arrays;
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
     * @param treatmentName the type of treatment to create
     * @param args the arguments to the creation of this treatment
     * @throws NotAFactoryException the given type of treatment does not exist
     * @throws InvalidDiagnosisException diagnosis does not point to an existing diagnosis
     * @throws ArgumentIsNullException one or more of the parameters was null
     * @throws WrongArgumentListException the argument list did not match the creation type
     * @throws ArgumentNotAnsweredException one or more of the given arguments was not answered
     * @throws ArgumentConstraintException one or more of the arguments was incorrect
     */
    public TreatmentCommand(Patient patient, DiagnosisInfo diagnosis, World world, String treatmentName, Argument[] args)
            throws NotAFactoryException, InvalidDiagnosisException,
            WrongArgumentListException, InvalidArgumentException {
        TreatmentFactory factory = world.getFactory(TreatmentFactory.class, treatmentName);
        if (args == null) {
            throw new ArgumentIsNullException("ArgumentList is null");
        }
        this.diagnosis = patient.isValidDiagnosisInfo(diagnosis);
        this.treatment = factory.make(Arrays.copyOf(args, args.length - 1));
        SingleSchedulableGroup core = new SingleSchedulableGroup(patient);

        Priority p = Utils.getAnswer(PriorityArgument.class, "the priority", args[args.length - 1]);

        AppointmentCommand appC = new AppointmentCommand(world, treatment, Collections.singletonList(core), p);

        //TODO FIX ME!!!!
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
            throw new RuntimeException("Treatment was made, thus not null");
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
            throw new RuntimeException("If this was executed, diagnosis should have same treatment");
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
