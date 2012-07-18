package Hospital.MedicalTest;

import Hospital.Argument.Argument;
import Hospital.Argument.IntegerArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Utils;

/**
 * Factory for creating X-ray scan tests
 */
public class XRayFactory implements MedicalTestFactory {

    public static final String X_RAY_SCAN_FACTORY = "New XRayScan";

	/**
     * Creates a new XRayScan object
     * @see Hospital.Factory.Factory#make(Hospital.Argument.Argument[])
     */
    public XRayScan make(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        validate(args);
        String bodyPart = (String) Utils.getAnswer(StringArgument.class, "BodyPart", args[0]);
        Integer numberOfImages = (Integer) Utils.getAnswer(IntegerArgument.class, "Number of images", args[1]);
        Integer zoom = (Integer) Utils.getAnswer(IntegerArgument.class, "Zoom", args[2]);
        return new XRayScan(zoom, numberOfImages, bodyPart);
    }

    /**
     * @see Hospital.Factory.Factory#getArguments()
     */
    public Argument[] getEmptyArgumentList() {
        Argument[] out = new Argument[3];
        out[0] = new StringArgument("Enter the body part: ");
        out[1] = new IntegerArgument("Enter number of images: ");
        out[2] = new IntegerArgument("Enter zoom: ");
        return out;
    }

    /**
     * @see Hospital.Factory.Factory#getName()
     */
    public String getName() {
        return X_RAY_SCAN_FACTORY;
    }

    public boolean validate(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("ArgumentList is null");
        }
        if (args.length != 3) {
            throw new WrongArgumentListException("Length of ArgumentList is wrong, should be 3");
        }
        Utils.getAnswer(StringArgument.class, "BodyPart", args[0]);
        Utils.getAnswer(IntegerArgument.class, "Number of images", args[1]);
        Utils.getAnswer(IntegerArgument.class, "Zoom", args[2]);
        return true;
    }
}
