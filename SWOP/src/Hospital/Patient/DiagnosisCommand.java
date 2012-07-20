package Hospital.Patient;

import Hospital.Argument.Argument;
import Hospital.Argument.DoctorArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Factory.Command;
import Hospital.People.Doctor;
import Hospital.World.World;
import java.util.Arrays;

/**
 * Creates a diagnosis (with or without second opinion), and undoes that later if necessary
 */
public class DiagnosisCommand implements Command {

    /**
     * the created diagnosis
     */
    private Diagnosis diagnosis;
    /**
     * the patient to which the diagnosis applies
     */
    private Patient toAdd;
    /**
     * the doctor to ask a second opinion from
     */
    private Doctor secondOpinion;
    /**
     * indicates whether this command is already executed
     */
    private boolean done = false;

    /**
     * Constructor
     * @param world the world to which this diagnosis applies
     * @param doctor the doctor making the diagnosis
     * @param factoryName the type of diagnosis to be created
     * @param secondDoc the doctor to ask a second opinion from (if needed)
     * @param args the arguments to the creation of the new diagnosis
     * @throws NoOpenedPatientFileException the creating doctor has no patient file opened
     * @throws PatientIsDischargedException the patient this diagnosis applies to is not checked in
     * @throws NotAFactoryException an invalid type of diagnosis was given
     * @throws WrongArgumentListException somehow the list of arguments used to create the diagnosis was wrong
     * @throws ArgumentNotAnsweredException the given argument was not answered
     * @throws ArgumentConstraintException the given argument was incorrect
     * @throws ArgumentIsNullException the given argument was null
     */
    public DiagnosisCommand(World world, Doctor doctor, String factoryName, Doctor secondDoc, Argument[] args)
            throws NoOpenedPatientFileException,
            PatientIsDischargedException,
            NotAFactoryException,
            WrongArgumentListException,
            InvalidArgumentException {
        secondOpinion = secondDoc;
        if (doctor == null) {
            throw new ArgumentIsNullException("Doctor was null");
        }
        doctor.checkOpenedPatient();
        if (doctor.getOpenedPatient().isDischarged()) {
            throw new PatientIsDischargedException();
        }
        DiagnosisFactory factory = null;
        try {
            factory = world.getFactory(DiagnosisFactory.class, factoryName);
        } catch (ArgumentIsNullException ex) {
            throw new Error("Class is not null!");
        }
        try {
            Argument[] out = new Argument[2];
            System.arraycopy(args, 0, out, 0, 1);
            out[1] = new DoctorArgument("Controller added").setAnswer(doctor);
            if (!factory.isSimpleDiagnosis()) {
                if (secondDoc == null) {
                    throw new ArgumentIsNullException("SecondOpinionDoctor needed!");
                }
                out = Arrays.copyOf(out, 3);
                out[2] = new DoctorArgument("Controller added").setAnswer(secondDoc);
            }

            diagnosis = factory.make(out);

            toAdd = doctor.getOpenedPatient();
        } catch (CannotChangeException e) {
            throw new Error("Can't set the answer of a newly created argument!");
        }
    }

    /**
     * Adds the created diagnosis to the patient and the second opinion doctor
     * @see Hospital.Factory.Command#execute()
     */
    public String execute() throws CannotDoException {
        if (done) {
            throw new CannotDoException("Already done!");
        }
        toAdd.addDiagnosis(diagnosis);
        if (secondOpinion != null) {
            secondOpinion.addSecondOpinions((DiagnosisSecondOpinion) diagnosis);
        }
        done = true;
        return diagnosis.toString();
    }

    /**
     * @see Hospital.Factory.Command#undo()
     */
    public String undo() throws CannotDoException {
        if (!done) {
            throw new CannotDoException("Not yet done!");
        }
        if (diagnosis.hasTreatment()) {
            throw new CannotDoException("Diagnosis has treatment, undo this one first");
        }
        if (!toAdd.removeDiagnosis(diagnosis)) {
            throw new CannotDoException("Diagnosis not found!");
        }
        done = false;
        return "Undone:\n" + this.toString();
    }

    /**
     * @see Hospital.Factory.Command#isDone()
     */
    public boolean isDone() {
        return done;
    }

    /**
     * @return the details of this diagnosis as string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (secondOpinion == null) {
            return diagnosis.toString() + "\nPatient: " + toAdd.toString();
        }
        return diagnosis.toString() + "\nPatient: " + toAdd.toString() + secondOpinion.toString();
    }
}