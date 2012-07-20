package Hospital.Treatments;

import Hospital.Argument.Argument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Utils;

/**
 * Surgery, the treatment of operating on patients
 */
public class Surgery extends Treatment {

    /**
     * description of the surgery
     */
    private String description;
    /**
     * special care that needs to be taken after the surgery
     */
    private String specialAfterCare;
    /**
     * the surgery report
     */
    private String report;

    /**
     * Constructor
     * @param description description of the surgery
     */
    public Surgery(String description) {
        this.description = description;
    }

    /**
     * Returns the description of the Surgery
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the arguments for Surgery
     * @see Hospital.Factory.Result#getResultArguments()
     */
    public Argument[] getEmptyResultArgumentList() {
        PublicArgument[] out = new PublicArgument[2];
        out[0] = new StringArgument("Enter the report: ");
        out[1] = new StringArgument("Enter the special aftercare: ");
        return out;
    }

    /**
     * @see Hospital.Factory.Result#enterResult(Hospital.Argument.PublicArgument[])
     */
    public void enterResult(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        validateResults(args);
        report = (String) Utils.getAnswer(StringArgument.class, "Report", args[0]);
        specialAfterCare = (String) Utils.getAnswer(StringArgument.class, "Special aftercare", args[1]);
        setResultEntered();
    }


    public boolean validateResults(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("No arguments given");
        }
        if (args.length != 2) {
            throw new WrongArgumentListException("Wrong argument length: " + args.length + " should be 2");
        }
        Utils.getAnswer(StringArgument.class, "Report", args[0]);
        Utils.getAnswer(StringArgument.class, "Special aftercare", args[1]);
        return true;
    }

    /**
     * @see Hospital.Factory.Result#getResultString()
     */
    public String getResultString() {
        return "Report: " + report + "\n"
                + "Special Aftercare: " + specialAfterCare;
    }

    /**
     * @see Hospital.Treatments.Treatment#toString()
     */
    @Override
    public String toString() {
        return "Surgery:\nDescription: " + description + "\n"
                + (isResultEntered() ? "\n" + getResultString() : "")
                + (getAppointment()!= null ? "\n"+ getAppointment().toString() : "\n");
    }

    /**
     * @return 180 minutes
     * @see Hospital.Treatments.Treatment#getLength()
     */
    @Override
    public int getLength() {
        return 180;
    }
}
