package Hospital.People.PeopleFactories;

import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.People.Doctor;
import Hospital.Schedules.Constraints.Preference.ChangeLocationPreference;
import Hospital.Utils;

/**
 * Creates doctors to add to the world
 * used by HospitalAdministratorController
 */
public class DoctorFactory implements StaffFactory {

    public static final String DOCTOR_FACTORY = "New Doctor";

    /**
     * Creates a new Doctor-object
     * @see Hospital.Factory.Factory#make(Hospital.Argument.Argument[])
     */
    public Doctor make(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException {
        validate(args);
        String name = (String) Utils.getAnswer(StringArgument.class, "name", args[0]);
        Doctor out = new Doctor(name);
        out.setPreference(new ChangeLocationPreference(out));
        return out;
    }

    /**
     * @see Hospital.Factory.Factory#getArguments()
     */
    public PublicArgument[] getEmptyArgumentList() {
        PublicArgument[] args = new PublicArgument[1];
        args[0] = new StringArgument("Enter the name of the doctor: ");
        return args;
    }

    /**
     * @return "New doctor"
     * @see Hospital.Factory.Factory#getName()
     */
    public String getName() {
        return DOCTOR_FACTORY;
    }

    public boolean validate(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("Argumentlist is null");
        }
        if (args.length != 1) {
            throw new WrongArgumentListException("Argumentlist of the wrong length");
        }
        Utils.getAnswer(StringArgument.class, "name", args[0]);
        return true;
    }
}
