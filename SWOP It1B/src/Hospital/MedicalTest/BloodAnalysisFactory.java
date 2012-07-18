package Hospital.MedicalTest;

import Hospital.Utils;
import Hospital.Argument.Argument;
import Hospital.Argument.IntegerArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;

/**
 * Creates blood analysis tests
 */
public class BloodAnalysisFactory implements MedicalTestFactory {

    private static final String BLOOD_ANALYSIS_FACTORY = "New BloodAnalysis";

	/**
     * @see Hospital.Factory.Factory#getArguments()
     */
    @Override
    public Argument[] getEmptyArgumentList() {
        Argument[] out = new Argument[2];
        out[0] = new StringArgument("Enter the focus: ");
        out[1] = new IntegerArgument("Enter number of analyses: ");
        return out;
    }

    /**
     * @see Hospital.Factory.Factory#getName()
     */
    @Override
    public String getName() {
        return BLOOD_ANALYSIS_FACTORY;
    }

    /**
     * Creates a BloodAnalysis object
     * @see Hospital.Factory.Factory#make(Hospital.Argument.Argument[])
     */
    @Override
    public BloodAnalysis make(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        validate(args);
        String focus = Utils.getAnswer(StringArgument.class, "Focus", args[0]);
        Integer numberAnalyses = Utils.getAnswer(IntegerArgument.class, "Number of analyses", args[1]);
        return new BloodAnalysis(focus, numberAnalyses);
    }

    public boolean validate(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("ArgumentList is null");
        }
        if (args.length != 2) {
            throw new WrongArgumentListException("Length of ArgumentList is wrong, should be 2");
        }
        Utils.getAnswer(StringArgument.class, "Focus", args[0]);
        Utils.getAnswer(IntegerArgument.class, "Number of analyses", args[1]);
        return true;
    }
}
