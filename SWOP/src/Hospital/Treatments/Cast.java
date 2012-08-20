package Hospital.Treatments;

import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Utils;
import Hospital.WareHouse.ItemInfo;

/**
 * A plaster cast on a bodypart
 */
public class Cast extends Treatment {

    /**
     * The bodypart to cast
     */
    private String bodyPart;
    /**
     * The duration the cast should stay on in days
     */
    private int duration;
    /**
     * The result of the cast
     */
    private String report;

    /**
     * Constructor
     * @param bodyPart the bodypart to cast
     * @param duration the duration the cast should stay on in days
     * @throws ArgumentConstraintException duraction was negative or zero
     */
    public Cast(String bodyPart, int duration) throws ArgumentConstraintException {
        checkDuration(duration);
        this.bodyPart = bodyPart;
        this.duration = duration;
    }

    /**
     * Checks whether a given duration is valid 
     * @param duration the duration to check
     * @throws ArgumentConstraintException duration was zero or less
     */
    private void checkDuration(int duration) throws ArgumentConstraintException {
        if (duration <= 0) {
            throw new ArgumentConstraintException("Duration cannot be <= 0");
        }
    }

    /**
     * Returns the bodyPart of the Cast.
     * @return BodyPart
     */
    public String getBodyPart() {
        return this.bodyPart;
    }

    /**
     * Returns the duration the Cast must stay on.
     * @return duration(days)
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Returns the empty arguments for the result.
     * @return Arguments to enter the result.
     */
    public PublicArgument[] getEmptyResultArgumentList() {
        PublicArgument[] out = new PublicArgument[1];
        out[0] = new StringArgument("Insert the report: ");
        return out;
    }

    /**
     * @see Hospital.Factory.Result#enterResult(Hospital.Argument.PublicArgument[])
     */
    public void enterResult(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException {
        validateResults(args);
        report = (String) Utils.getAnswer(StringArgument.class, "Report", args[0]);
        setResultEntered();
    }

    public boolean validateResults(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("No arguments given");
        }
        if (args.length != 1) {
            throw new WrongArgumentListException("Wrong argument length: " + args.length + " should be 1");
        }
        Utils.getAnswer(StringArgument.class, "Report", args[0]);
        return true;
    }

    /**
     * @see Hospital.Factory.Result#getResultString()
     */
    public String getResultString() {
        return "Report: " + report;
    }

    /**
     * Gives a description of the cast
     * @see Hospital.Treatments.Treatment#toString()
     */
    @Override
    public String toString() {
        return "Cast:"
                + "\nBody Part: " + getBodyPart()
                + "\nDuration in days: " + getDuration()
                + (isResultEntered() ? "\n" + getResultString() : "")
                + (getAppointment() != null ? "\n" + getAppointment().toString() : "\n");
    }

    /**
     * Returns the length an appointment for a cast takes
     * @return 120
     * @see Hospital.Treatments.Treatment#getLength()
     */
    @Override
    public int getLength() {
        return 120;
    }

    /**
     * A cast requires plaster.
     * @return {"Plaster"}
     */
    @Override
    public ItemInfo[] getNeededItems() {
        return new ItemInfo[]{new ItemInfo("Plaster", 1)};
    }
}
