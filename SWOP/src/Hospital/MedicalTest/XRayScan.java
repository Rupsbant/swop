package Hospital.MedicalTest;

import Hospital.Argument.IntegerArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Machine.Machine;
import Hospital.Machine.XRayMachine;
import Hospital.Schedules.Constraints.XRayConstraint.XRayConstraint;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Utils;
import java.util.List;

/**
 * X-ray scan medical test
 */
public class XRayScan extends MedicalTest {

    /**
     * Maximum allowed amount of xray images at once
     */
    public static final int MAX_XRAY_COUNT = 10;
    /**
     * The zoom used for this scan
     */
    private int zoom;
    /**
     * The number of images that need to be taken
     */
    private int numberOfImagesToTake;
    /**
     * The body part of which to take images
     */
    private String bodyPart;
    /**
     * Abnormalities that were discovered on the scans
     */
    private String abnormalities;
    /**
     * The number of images that were actually taken
     */
    private Integer numberOfImagesTaken;

    /**
     * Constructor
     * @param zoom the zoom used for this scan
     * @param numberOfImagesToTake the number of images that need to be taken
     * @param bodyPart the body part of which to take images
     * @throws ArgumentConstraintException zoom was not in the 1-3 range or numberOfImagesToTake was not in the 1-MAX_XRAY_COUNT range
     */
    public XRayScan(int zoom, int numberOfImagesToTake, String bodyPart) throws ArgumentConstraintException {
        checkZoom(zoom);
        checkNumberImages(numberOfImagesToTake);
        this.zoom = zoom;
        this.numberOfImagesToTake = numberOfImagesToTake;
        this.bodyPart = bodyPart;
    }

    /**
     * @see Hospital.Factory.Result#getResultArguments()
     */
    @Override
    public PublicArgument[] getEmptyResultArgumentList() {
        PublicArgument[] out = new PublicArgument[2];
        out[0] = new IntegerArgument("Enter the number of images taken: ");
        out[1] = new StringArgument("Please enter any abnormalities: ");
        return out;
    }

    /**
     * @see Hospital.Factory.Result#enterResult(Hospital.Argument.PublicArgument[])
     */
    @Override
    public void enterResult(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("No arguments given");
        }
        if (args.length != 2) {
            throw new WrongArgumentListException("Wrong argument length: " + args.length + " should be 2");
        }
        setNumberOfImagesTaken(args[0]);
        abnormalities = Utils.getAnswer(StringArgument.class, "Abnormalities.", args[1]);
        this.setResultEntered(true);
    }


    @Override
    public boolean validateResults(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("No arguments given");
        }
        if (args.length != 2) {
            throw new WrongArgumentListException("Wrong argument length: " + args.length + " should be 2");
        }
        int imagesTaken = (Integer) Utils.getAnswer(IntegerArgument.class, "The number of images", args[0]);
        if (!isValidNumberImages(imagesTaken)) {
            throw new ArgumentConstraintException("Number of images taken must be > 0 !");
        }
        Utils.getAnswer(StringArgument.class, "Abnormalities.", args[1]);
        return true;
    }

    /**
     * Sets the number of images that needs to be taken
     * @param arg an argument providing the number of images that needs to be taken
     * @throws WrongArgumentListException the argument was not an IntegerArgument
     * @throws ArgumentNotAnsweredException the argument was not answered
     * @throws ArgumentConstraintException the number of images was not in the 1-MAX_XRAY_COUNT range
     */
    private void setNumberOfImagesTaken(PublicArgument arg) throws WrongArgumentListException, InvalidArgumentException {
        numberOfImagesTaken = (Integer) Utils.getAnswer(IntegerArgument.class, "The number of images", arg);
        if (!isValidNumberImages(numberOfImagesTaken)) {
            throw new ArgumentConstraintException("Number of images taken must be > 0 !");
        }
    }

    /**
     * Returns a textual representation of the XRay object.
     * @return "XRayScan: $bodypart, $numberOfImagesToTake, $zoom"
     */
    @Override
    public String toString() {
        return "XRayTest: " + bodyPart + ", " + numberOfImagesToTake + ", " + zoom;
    }

    /**
     * Returns a textual representation of the entered result.
     * @return "No result entered" if nothing was entered. <br> "Number of images taken: $numberOfImagesTaken <br> Abnormalities: $abnormalities" if a result was entered
     */
    @Override
    public String getResultString() {
        if (!isResultEntered()) {
            return "No result entered";
        }
        return "Number of images taken: " + numberOfImagesTaken + "\n"
                + "Abnormalities: " + abnormalities;
    }

    /**
     * Checks whether a given zoom is a valid zoom
     * @param zoom the zoom to check
     * @throws ArgumentConstraintException the zoom was invalid (not in the 1-3 range)
     */
    private void checkZoom(int zoom) throws ArgumentConstraintException {
        if (!isValidZoom(zoom)) {
            throw new ArgumentConstraintException("Zoom must be between 1 and 3");
        }
    }

    /**
     * Checks whether a given zoom is a valid zoom
     * @param zoom the zoom to check
     * @return true if the zoom was valid (in 1-3 range), false otherwise
     */
    private boolean isValidZoom(int zoom) {
        return zoom >= 1 && zoom <= 3;
    }

    /**
     * Checks whether a given amount of images is valid
     * @param amount the amount to check
     * @throws ArgumentConstraintException the amount was invalid (not in the 1-MAX_XRAY_COUNT range)
     */
    private void checkNumberImages(int amount) throws ArgumentConstraintException {
        if (!isValidNumberImages(amount)) {
            throw new ArgumentConstraintException("Number of images must be between 1 and " + MAX_XRAY_COUNT);
        }
    }

    /**
     * Checks whether a given amount of images is valid
     * @param amount the amount to check
     * @return true if the amount was valid (in the 1-10 range), false otherwise
     */
    private boolean isValidNumberImages(int amount) {
        return amount > 0 && amount <= MAX_XRAY_COUNT;
    }

    /**
     * Get the amount of images that was taken or probably will be taken
     * @return amount of images
     */
    public int getXRayCount() { // if result was entered, we have a better estimate
        return (isResultEntered() ? numberOfImagesTaken : numberOfImagesToTake);
    }

    /**
     * This returns the length it takes to complete the scan.
     * This is needed to plan the Appointment.
     * @return 15 minutes
     */
    @Override
    public int getLength() {
        return 15;
    }

    /**
     * Returns the machineType needed for the appointment.
     * @param <S> The type of the required class.
     * @return XRayMachine.class
     */
    @Override
    public <S extends Machine> Class<S> getRequiredMachine() {
        return (Class<S>) XRayMachine.class;
    }

    /**
     * Returns the constraints this MedicalTest needs to satisfy: There is a maximum of 10 per year.
     * @return a new XRayConstraint to satisfy the Appointment.
     */
    @Override
    public List<TimeFrameConstraint> getConstraints() {
        try {
            List<TimeFrameConstraint> temp = super.getConstraints();
            temp.add(new XRayConstraint(numberOfImagesToTake));
            return temp;
        } catch (ArgumentConstraintException ex) {
            throw new Error("Number of images to take is never negative");
        }
    }
}
