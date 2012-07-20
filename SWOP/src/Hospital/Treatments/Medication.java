package Hospital.Treatments;

import Hospital.Argument.Argument;
import Hospital.Argument.BooleanArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Utils;
import Hospital.WareHouse.ItemInfo;

/**
 * Medication to treat a patient with
 */
public class Medication extends Treatment {

    /**
     * The description of how to take the medication
     */
    private String description;
    /**
     * Indicates whether the medication is sensitive
     */
    private Boolean sensitive;
    /**
     * Indicates whether the treated patient had an abnormal reaction to the medication
     */
    private Boolean reaction;
    /**
     * Report about the treatment
     */
    private String report;
    //   /**
    //   * The medication items that need to be taken for this treatment
    //   */
    //  private ArrayList<MedicationItem> medicationItems;
    /**
     * The medication items needed for the treatment.
     */
    private ItemInfo[] items;

    /**
     * Constructor
     * @param description the description of how to take the medication
     * @param sensitive is the medication sensitive
     * @param items the medication items that need to be taken for this treatment
     */
    public Medication(String description, Boolean sensitive, ItemInfo[] items) {
        this.description = description;
        this.sensitive = sensitive;
        this.items = items;
    }

    /**
     * Returns the description of the medication.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether the medication is sensitive.
     * @return true if sensitive.
     */
    public Boolean getSensitive() {
        return sensitive;
    }

    /**
     * @see Hospital.Factory.Result#getResultString()
     */
    public String getResultString() {
        return "Abnormal reaction: " + reaction + "\n"
                + "Report: " + report;
    }

    /**
     * @see Hospital.Treatments.Treatment#toString()
     */
    @Override
    public String toString() {
        return "MedicationTreatment: " + description
                + (items.length > 0 ? medItemsToString() : "\nNo medication items!")
                + "\nSensitive: " + sensitive
                + (isResultEntered() ? "\n" + getResultString() : "")
                + (getAppointment()!= null ? "\n"+ getAppointment().toString() : "");
    }

    /**
     * A string description of all the medication items in this medication treatment
     * @return string description of all medication items
     */
    private String medItemsToString() {
        String string = "\nMedication Items:";
        for (int i = 0; i < items.length; i++) {
            string = string + " " + items[i].toString();
        }
        return string;
    }

    /**
     * @return 10 minutes, 20 if the medication is sensitive
     * @see Hospital.Treatments.Treatment#getLength()
     */
    public int getLength() {
        return sensitive ? 20 : 10;
    }

    /**
     * Gets the arguments for Medication 
     * @see Hospital.Factory.Result#getResultArguments()
     */
    public Argument[] getEmptyResultArgumentList() {
        PublicArgument[] out = new PublicArgument[2];
        out[0] = new BooleanArgument("Did an abnormal reaction occur?: ");
        out[1] = new StringArgument("Please enter the report: ");
        return out;
    }

    /**
     * @see Hospital.Factory.Result#enterResult(Hospital.Argument.PublicArgument[])
     */
    public void enterResult(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        validateResults(args);
        reaction = (Boolean) Utils.getAnswer(BooleanArgument.class, "Abnormal Reaction", args[0]);
        report = (String) Utils.getAnswer(StringArgument.class, "Report", args[1]);
        setResultEntered();
    }

    public boolean validateResults(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("No arguments given");
        }
        if (args.length != 2) {
            throw new WrongArgumentListException("Wrong argument length: " + args.length + " should be 2");
        }
        Utils.getAnswer(BooleanArgument.class, "Abnormal Reaction", args[0]);
        Utils.getAnswer(StringArgument.class, "Report", args[1]);
        return true;
    }

    /**
     * @see Hospital.Treatments.Treatment#getNeededItems()
     */
    @Override
    public ItemInfo[] getNeededItems() {
        return items;
    }
//    /**
//     * Sets what medication needs to be taken for this treatment
//     * @param medicationItems the items to set
//     */
//    public void setMedicationItems(ArrayList<MedicationItem> medicationItems) {
//        this.medicationItems = medicationItems;
//    }
}
