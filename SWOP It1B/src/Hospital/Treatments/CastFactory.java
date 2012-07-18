package Hospital.Treatments;

import Hospital.Utils;
import Hospital.Argument.Argument;
import Hospital.Argument.IntegerArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;

/**
 * Creates cast treatments
 */
public class CastFactory implements TreatmentFactory {

    public static final String CAST_FACTORY = "Cast";

	/**
     * Creates a Cast-object
     * @see Hospital.Factory.Factory#make(Hospital.Argument.Argument[])
     */
    public Cast make(Argument[] args) throws InvalidArgumentException, WrongArgumentListException {
        validate(args);
        String description = Utils.getAnswer(StringArgument.class, "body part", args[0]);
        Integer sens = Utils.getAnswer(IntegerArgument.class, "duration in days", args[1]);
        return new Cast(description, sens);
    }

    /**
     * @see Hospital.Factory.Factory#getArguments()
     */
    public Argument[] getEmptyArgumentList() {
        Argument[] out = new Argument[2];
        out[0] = new StringArgument("Please enter the body part: ");
        out[1] = new IntegerArgument("Please enter the duration in days: ");
        return out;
    }

    /**
     * @return "Cast"
     * @see Hospital.Factory.Factory#getName()
     */
    public String getName() {
        return CAST_FACTORY;
    }

    /**
     * @see Hospital.Treatments.TreatmentFactory#needsMedication()
     */
    public boolean needsMedication() {
        return false;
    }

    public boolean validate(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("ArgumentList was null");
        }
        if (args.length != 2) {
            throw new WrongArgumentListException("Length of argumentList was wrong, it should be 2");
        }
        Utils.getAnswer(StringArgument.class, "body part", args[0]);
        Utils.getAnswer(IntegerArgument.class, "duration in days", args[1]);
        return true;
    }
}
