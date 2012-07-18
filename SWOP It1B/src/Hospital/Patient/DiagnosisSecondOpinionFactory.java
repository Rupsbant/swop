package Hospital.Patient;

import Hospital.Argument.Argument;
import Hospital.Argument.DoctorArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.People.Doctor;
import Hospital.Utils;

/**
 * Creates diagnoses which require a second opinion
 */
public class DiagnosisSecondOpinionFactory extends DiagnosisFactory {

    public static final String DIAGNOSIS_WITH_SECOND_OPINION_FACTORY = "Diagnosis with SecondOpinion";

	/**
     * This method creates a Diagnosis that needs a second-opinion with the given arguments.
     * @param args An ordered list of arguments.
     * @return The diagnosis that was created using the arguments.
     */
    @Override
    public Diagnosis make(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("ArgumentList was null");
        }
        if (args.length != 3) {
            throw new WrongArgumentListException("ArgumentList should have length 3");
        }
        String content = (String) Utils.getAnswer(StringArgument.class, "name", args[0]);
        Doctor doctor = (Doctor) Utils.getAnswer(DoctorArgument.class, "doctor", args[1]);
        Doctor second = (Doctor) Utils.getAnswer(DoctorArgument.class, "secondOpinionDoctor", args[2]);
        DiagnosisSecondOpinion diagsecond = new DiagnosisSecondOpinion(content, doctor, second);
        return diagsecond;
    }

    /**
     * @return The needed arguments to create this Diagnsosis
     */
    @Override
    public Argument[] getEmptyArgumentList() {
        Argument[] out = new Argument[3];
        out[0] = new StringArgument("Enter the diagnosisdetails: ");
        out[1] = new DoctorArgument("doctor, for Controller");
        out[2] = new DoctorArgument("secondOpinionDoctor, for Controller");
        return out;
    }

    /**
     * Returns whether this factory is simple and does not need a second opinion.
     * @return false, it needs a second opinion.
     */
    @Override
    public boolean isSimpleDiagnosis() {
        return false;
    }

    /**
     * Returns the name of this factory
     * @return "Diagnosis with SecondOpinion"
     */
    @Override
    public String getName() {
        return DIAGNOSIS_WITH_SECOND_OPINION_FACTORY;
    }
}
