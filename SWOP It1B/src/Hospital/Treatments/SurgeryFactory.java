package Hospital.Treatments;

import Hospital.Utils;
import Hospital.Argument.Argument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;

/**
 * Creates surgery treatments
 */
public class SurgeryFactory implements TreatmentFactory {

    public static final String SURGERY_FACTORY = "Surgery";

    /**
     * Creates a Surgery object
     * @see Hospital.Factory.Factory#make(Hospital.Argument.Argument[])
     */
    public Surgery make(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        validate(args);
        String description = Utils.getAnswer(StringArgument.class, "description", args[0]);
        return new Surgery(description);
    }

    /**
     * @see Hospital.Factory.Factory#getArguments()
     */
    public Argument[] getEmptyArgumentList() {
        Argument[] out = new Argument[1];
        out[0] = new StringArgument("Please enter a description: ");
        return out;
    }

    /**
     * @return "Surgery"
     * @see Hospital.Factory.Factory#getName()
     */
    public String getName() {
        return SURGERY_FACTORY;
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
        if (args.length != 1) {
            throw new WrongArgumentListException("Length of argumentList was wrong, it should be 1");
        }
        Utils.getAnswer(StringArgument.class, "description", args[0]);
        return true;
    }
}
