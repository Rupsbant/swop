package Hospital.MedicalTest;

import Hospital.Argument.IntegerArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Machine.BloodAnalyzer;
import Hospital.Machine.Machine;
import Hospital.Utils;

/**
 * Blood analysis medical test
 */
public class BloodAnalysis extends MedicalTest {

    /**
     * the focus of this test
     */
    private String focus;
    /**
     * the number of analyses that need to be done
     */
    private int nbOfAnalyses;
    /**
     * how much blood was withdrawn for the analysis
     */
    private Integer bloodWithdrawn;
    /**
     * the amount of red bloodcells in the blood
     */
    private Integer redCellCount;
    /**
     * the amount of white bloodcells in the blood
     */
    private Integer whiteCellCount;
    /**
     * the amount of platelets in the blood
     */
    private Integer plateletCount;

    /**
     * Constructor
     * @param focus the focus of this test
     * @param nb the number of analyses that need to be done (must be a strictly positive integer)
     * @throws ArgumentConstraintException the number was not strictly positive
     */
    BloodAnalysis(String focus, int nb) throws ArgumentConstraintException {
        setFocus(focus);
        setNbOfAnalyses(nb);
    }

    /**
     * sets how many analyses need to be done
     * @param nb the amount (must be a strictly positive integer)
     * @throws ArgumentConstraintException the amount was zero or less
     */
    private void setNbOfAnalyses(int nb) throws ArgumentConstraintException {
        if (nb <= 0) {
            throw new ArgumentConstraintException("number of analyses cannot be 0 or less");
        }
        this.nbOfAnalyses = nb;
    }

    /**
     * Sets the focus of the test
     * @param focus the focus of the test
     */
    private void setFocus(String focus) {
        this.focus = focus;
    }

    /**
     * @see Hospital.Factory.Result#getResultArguments()
     */
    @Override
    public PublicArgument[] getEmptyResultArgumentList() {
        PublicArgument[] out = new PublicArgument[4];
        out[0] = new IntegerArgument("Enter the amount of blood withdrawn: ");
        out[1] = new IntegerArgument("Enter the red cell count: ");
        out[2] = new IntegerArgument("Enter the white cell count: ");
        out[3] = new IntegerArgument("Enter the platelet count: ");
        return out;
    }

    /**
     * @see Hospital.Factory.Result#enterResult(Hospital.Argument.PublicArgument[])
     */
    @Override
    public void enterResult(PublicArgument[] args)
            throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("No arguments given");
        }
        if (args.length != 4) {
            throw new WrongArgumentListException("Wrong argument length: " + args.length + " should be 4");
        }
        bloodWithdrawn = getPositiveInteger(args[0]);
        redCellCount = getPositiveInteger(args[1]);
        whiteCellCount = getPositiveInteger(args[2]);
        plateletCount = getPositiveInteger(args[3]);
        this.setResultEntered(true);
    }

    public boolean validateResults(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("No arguments given");
        }
        if (args.length != 4) {
            throw new WrongArgumentListException("Wrong argument length: " + args.length + " should be 4");
        }
        getPositiveInteger(args[0]);
        getPositiveInteger(args[1]);
        getPositiveInteger(args[2]);
        getPositiveInteger(args[3]);
        return true;
    }

    /**
     * Sets the amount of white bloodcells found in the blood
     * @param publicArgument the answer to this argument is the amount
     * @throws WrongArgumentListException the given argument was not an IntegerArgument
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     * @throws ArgumentConstraintException the amount was zero or less
     */
    private int getPositiveInteger(PublicArgument publicArgument) throws WrongArgumentListException, InvalidArgumentException {
        int temp = (Integer) Utils.getAnswer(IntegerArgument.class, "The white bloodcell count.", publicArgument);
        if (temp <= 0) {
            throw new ArgumentConstraintException("white cell count must be > 0 !");
        }
        return temp;
    }

    /**
     * Returns the resultString if results were entered, "No result entered" otherwise.
     * @return "Amount of blooc withdrawn: $bloodWithdrawn <br> Red cell count: $redCellCount <br> Whit cell count: $whiteCellCount <br> Platelet count: $plateletCount"
     */
    @Override
    public String getResultString() {
        if (!isResultEntered()) {
            return "No result entered";
        }
        return "Amount of blood withdrawn: " + bloodWithdrawn + "\n"
                + "Red cell count: " + redCellCount + "\n"
                + "White cell count: " + whiteCellCount + "\n"
                + "Platelet count: " + plateletCount;
    }

    /**
     * Returns a representation of this MedicalTest
     * @return "BloodAnalysis: $focus, $numberOfAnalysis"
     */
    @Override
    public String toString() {
        return "BloodAnalysis: " + focus + ", " + nbOfAnalyses;
    }

    /**
     * Returns the length this MedicalTest takes.
     * @return 45
     */
    public int getLength() {
        return 45;
    }

    /**
     * Returns the class of machines this test requires.
     * @param <S> The machineType
     * @return BloocAnalyze.class
     */
    public <S extends Machine> Class<S> getRequiredMachine() {
        return (Class<S>) BloodAnalyzer.class;
    }
}
