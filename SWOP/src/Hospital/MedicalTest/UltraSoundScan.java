package Hospital.MedicalTest;

import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Machine.Machine;
import Hospital.Machine.UltraSoundMachine;
import Hospital.Utils;
import java.util.HashSet;
import java.util.Set;

/**
 * Ultrasound scan medical test
 */
public class UltraSoundScan extends MedicalTest {

    /**
     * a Set of possible strings allowed to be used for <i>nature</i>
     */
    private static final Set<String> accepted = getAcceptedNatures();
    /**
     * the focus of this test
     */
    private String focus;
    /**
     * indicates whether video should be recorded
     */
    private boolean recordVideo;
    /**
     * indicates whether images should be taken
     */
    private boolean recordImages;
    /**
     * information about the scan
     */
    private String scanInformation;
    /**
     * the nature of the scanned mass
     */
    private String nature;

    /**
     * Constructor
     * @param focus the focus of this test
     * @param recordvideo indicates whether video should be recorded
     * @param recordimages indicates whether images should be taken
     * @throws ArgumentIsNullException focus was a null string
     */
    UltraSoundScan(String focus, boolean recordvideo, boolean recordimages) throws ArgumentIsNullException {
        this.setFocus(focus);
        this.setRecordVideo(recordvideo);
        this.setRecordImages(recordimages);
    }

    /**
     * @see Hospital.Factory.Result#getResultArguments()
     */
    @Override
    public PublicArgument[] getEmptyResultArgumentList() {
        PublicArgument[] out = new PublicArgument[2];
        out[0] = new StringArgument("Enter the scan information: ");
        out[1] = new StringArgument("Nature of scanned mass: benign, unknown or malignant");
        return out;
    }

    /**
     * @see Hospital.Factory.Result#enterResult(Hospital.Argument.PublicArgument[])
     */
    @Override
    public void enterResult(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException {
        validateResults(args);
        scanInformation = (String) Utils.getAnswer(StringArgument.class, "ScanInformation", args[0]);
        setNature(args[1]);
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
        Utils.getAnswer(StringArgument.class, "ScanInformation", args[0]);
        String nat = (String) Utils.getAnswer(StringArgument.class, "The nature of the mass", args[1]);
        if (!checkNature(nat)) {
            throw new ArgumentConstraintException("Nature can't be " + nat);
        }
        return true;
    }

    /**
     * Sets the nature of the scanned mass
     * @param arg a PublicArgument to which the answer is the nature of the scanned mass
     * @throws WrongArgumentListException the argument was not a StringArgument
     * @throws ArgumentNotAnsweredException the argument was not answered
     * @throws ArgumentConstraintException the answer to the argument was not an allowed value nature
     */
    private void setNature(PublicArgument arg) throws WrongArgumentListException, InvalidArgumentException {
        nature = (String) Utils.getAnswer(StringArgument.class, "The nature of the mass", arg);
        if (!checkNature(nature)) {
            throw new ArgumentConstraintException("Nature can't be " + nature);
        }
    }

    /**
     * Checks whether a given string is an allowed nature
     * @param nature the string to check
     * @return true if the string was an allowed nature, false otherwise
     */
    public boolean checkNature(String nature) {
        return accepted.contains(nature);
    }

    /**
     * Sets the focus of this test
     * @param focus a string detailing the focus of this test
     * @throws ArgumentIsNullException focus was null
     */
    private void setFocus(String focus) throws ArgumentIsNullException {
        if (focus == null) {
            throw new ArgumentIsNullException("focusString can't be null");
        }
        this.focus = focus;
    }

    /**
     * Gets the focus of this test
     * @return a string detailing the focus of this test
     */
    public String getFocus() {
        return focus;
    }

    /**
     * Sets whether video should be recorded
     * @param recordVideo true if video should be recorded, false if not
     */
    private void setRecordVideo(boolean recordVideo) {
        this.recordVideo = recordVideo;
    }

    /**
     * Checks whether video should be recorded
     * @return true if video should be recorded, false if not
     */
    public boolean isRecordingVideo() {
        return recordVideo;
    }

    /**
     * Sets whether images should be taken
     * @param recordImages true if images should be taken, false if not
     */
    private void setRecordImages(boolean recordImages) {
        this.recordImages = recordImages;
    }

    /**
     * Checks whether images should be taken
     * @return true if images should be taken, false if not
     */
    public boolean isRecordImages() {
        return recordImages;
    }

    /**
     * Returns a description of the results, "No result entered" if there are none.
     * @return "Scan information: $scanInformation <br>  Nature of scanned mass: $nature"
     */
    @Override
    public String getResultString() {
        if (!isResultEntered()) {
            return "No result entered";
        }
        return "Scan information: " + scanInformation + "\n"
                + "Nature of scanned mass: " + nature;
    }

    /**
     * Returns a description of this Ultrasound
     * @return "UltraSoundScan: $focus, $recordVideo, $recordImages"
     */
    @Override
    public String toString() {
        return "UltraSoundScan: " + focus + ", " + recordVideo + ", " + recordImages;
    }

    /**
     * Gets all accepted natures that are allowed to be used
     * @return a set of strings containing allowed natures
     */
    private static Set<String> getAcceptedNatures() {
        Set<String> out = new HashSet<String>();
        out.add("benign");
        out.add("malignant");
        out.add("unknown");
        return out;
    }

    /**
     * Returns the length of the appointment this MedicalTest needs.
     * @return 30 minutes
     */
    @Override
    public int getLength() {
        return 30;
    }

    /**
     * Returns the machine needed for this MedicalTest
     * @param <S> The type op the machine.
     * @return UltraSoundMachine.class
     */
    @Override
    public <S extends Machine> Class<S> getRequiredMachine() {
        return (Class<S>) UltraSoundMachine.class;
    }
}
