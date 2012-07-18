package Hospital.Treatments;

import Hospital.Argument.Argument;
import Hospital.Argument.BooleanArgument;
import Hospital.Argument.ItemArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Utils;
import Hospital.WareHouse.ItemInfo;

/**
 * Creates medication treatments
 */
public class MedicationFactory implements TreatmentFactory {

    public static final String MEDICATION_FACTORY = "Medication";

    /**
     * Creates a Medication object
     * @see Hospital.Factory.Factory#make(Hospital.Argument.Argument[])
     */
    public Medication make(Argument[] args) throws InvalidArgumentException, WrongArgumentListException {
        validate(args);
        String description = Utils.getAnswer(StringArgument.class, "description", args[0]);
        Boolean sens = Utils.getAnswer(BooleanArgument.class, "sensitivity", args[1]);
        String[] medItems = (Utils.getAnswer(StringArgument.class, "medication", args[2])).split("[, ]+");
        ItemInfo[] items = ItemInfo.getItemInfo(medItems);
        return new Medication(description, sens, items);
    }

    /**
     * @see Hospital.Factory.Factory#getArguments()
     */
    public Argument[] getEmptyArgumentList() {
        Argument[] out = new Argument[3];
        out[0] = new StringArgument("Please enter a description: ");
        out[1] = new BooleanArgument("Is the medication sensitive?: ");
        out[2] = new ItemArgument("The medication");
        return out;
    }

    /**
     * @return "Medication"
     * @see Hospital.Factory.Factory#getName()
     */
    public String getName() {
        return MEDICATION_FACTORY;
    }

    /**
     * @see Hospital.Treatments.TreatmentFactory#needsMedication()
     */
    public boolean needsMedication() {
        return true;
    }

    public boolean validate(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("ArgumentList was null");
        }
        if (args.length != 3) {
            throw new WrongArgumentListException("Length of argumentList was wrong, it should be 3");
        }
        Utils.getAnswer(StringArgument.class, "description", args[0]);
        Utils.getAnswer(BooleanArgument.class, "sensitivity", args[1]);
        Utils.getAnswer(StringArgument.class, "medication", args[2]);
        return true;
    }
}
