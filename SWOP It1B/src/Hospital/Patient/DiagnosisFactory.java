package Hospital.Patient;

import Hospital.Argument.Argument;
import Hospital.Argument.DoctorArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Factory.Factory;
import Hospital.People.Doctor;
import Hospital.Utils;

/**
 * Creates simple diagnoses (without second opinion)
 */
public class DiagnosisFactory implements Factory<Diagnosis> {

    public static final String DIAGNOSIS_FACTORY = "Diagnosis";

	/**
     * This method makes a Diagnosis with the given arguments.
     * @param args An ordered list of Arguments.
     * @return Returns the new Diagnosis.
     * @throws WrongArgumentListException the given list of arguments did not meet the requirements
     * @throws ArgumentNotAnsweredException one of the given arguments was not answered
     * @throws ArgumentIsNullException the given list of arguments was null
     */
    public Diagnosis make(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        validate(args);
        String content = (String) Utils.getAnswer(StringArgument.class, "name", args[0]);
        Doctor doctor = (Doctor) Utils.getAnswer(DoctorArgument.class, "name", args[1]);
        Diagnosis diag = new Diagnosis(content, doctor);
        return diag;
    }

    /**
     * Gets the arguments needed for the creation of aa diagnosis in this factory
     * @return an array of Arguments which, when answered, can be used in make()
     */
    public Argument[] getEmptyArgumentList() {
        Argument[] out = new Argument[1];
        out[0] = new StringArgument("Enter the diagnosisdetails: ");
        return out;
    }

    /**
     * Returns the name of this factory
     * @return "Diagnosis"
     */
    public String getName() {
        return DIAGNOSIS_FACTORY;
    }

    /**
     * Returns whether this factory generates simple diagnoses without second-opinion
     * @return true
     */
    public boolean isSimpleDiagnosis() {
        return true;
    }

    public boolean validate(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("ArgumentList was null");
        }
        if (args.length != 2) {
            throw new WrongArgumentListException("Length should be 2");
        }
        Utils.getAnswer(StringArgument.class, "name", args[0]);
        Utils.getAnswer(DoctorArgument.class, "name", args[1]);
        return true;
    }
}
