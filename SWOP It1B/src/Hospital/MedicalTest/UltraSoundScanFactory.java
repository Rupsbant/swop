package Hospital.MedicalTest;

import Hospital.Utils;
import Hospital.Argument.Argument;
import Hospital.Argument.BooleanArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;

/**
 * A factory for creating ultrasound scan tests
 */
public class UltraSoundScanFactory implements MedicalTestFactory {

    public static final String ULTRA_SOUND_SCAN_FACTORY = "New UltraSoundScan";

	/**
     * @see Hospital.Factory.Factory#getArguments()
     */
    @Override
    public Argument[] getEmptyArgumentList() {
        PublicArgument[] out = new PublicArgument[3];
        out[0] = new StringArgument("Enter the focus: ");
        out[1] = new BooleanArgument("Record video?: ");
        out[2] = new BooleanArgument("Record images?: ");
        return out;
    }

    /**
     * @see Hospital.Factory.Factory#getName()
     */
    @Override
    public String getName() {
        return ULTRA_SOUND_SCAN_FACTORY;
    }

    /**
     * Creates an UltraSoundScan object
     * @see Hospital.Factory.Factory#make(Hospital.Argument.Argument[])
     */
    public UltraSoundScan make(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("ArgumentList is null");
        }
        if (args.length != 3) {
            throw new WrongArgumentListException("Length of ArgumentList is wrong, should be 3");
        }
        String focus = (String) Utils.getAnswer(StringArgument.class, "Focus", args[0]);
        boolean recordVideo = (Boolean) Utils.getAnswer(BooleanArgument.class, "Record Video", args[1]);
        boolean recordImages = (Boolean) Utils.getAnswer(BooleanArgument.class, "Record Images", args[2]);
        return new UltraSoundScan(focus, recordVideo, recordImages);
    }

    public boolean validate(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("ArgumentList is null");
        }
        if (args.length != 3) {
            throw new WrongArgumentListException("Length of ArgumentList is wrong, should be 3");
        }
        Utils.getAnswer(StringArgument.class, "Focus", args[0]);
        Utils.getAnswer(BooleanArgument.class, "Record Video", args[1]);
        Utils.getAnswer(BooleanArgument.class, "Record Images", args[2]);
        return true;
    }
}
