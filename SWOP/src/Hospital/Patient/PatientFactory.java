package Hospital.Patient;

import Hospital.Argument.Argument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Factory.Factory;
import Hospital.Utils;

/**
 * Creates patients
 */
public class PatientFactory implements Factory<Patient> {

    private static final String PATIENT_FACTORY = "New Patient";

	/**
     * Creates a new Patient based on the information in args.
     * @param args An array of Arguments containing the name and complaints of the patient.
     * @return a Patient
     * @throws ArgumentConstraintException the name given in the arguments was empty
     * @throws WrongArgumentListException the given list of arguments did not meet the requirements
     * @throws ArgumentNotAnsweredException the given argument was not answered
     * @throws ArgumentIsNullException the given argument was null 
     */
    public Patient make(Argument[] args) throws ArgumentConstraintException, WrongArgumentListException, InvalidArgumentException {
        validate(args);
        String name = (String) Utils.getAnswer(StringArgument.class, "name", args[0]);
        return new Patient(name);
    }

    /**
     * Gets the patient's details.
     * @return an array of Arguments
     */
    public Argument[] getEmptyArgumentList() {
        Argument[] out = new Argument[1];
        out[0] = new StringArgument("Enter the patient's name: ");
        return out;
    }

    /**
     * Returns the name of this factory
     * @return "New Patient"
     */
    public String getName() {
        return PATIENT_FACTORY;
    }

    public boolean validate(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("Argumentlist is null");
        }
        if (args.length != 1) {
            throw new WrongArgumentListException("Argumentlist of the wrong length");
        }
        Utils.getAnswer(StringArgument.class, "name", args[0]);
        return true;
    }
}
