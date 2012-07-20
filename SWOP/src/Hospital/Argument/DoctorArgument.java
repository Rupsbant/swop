package Hospital.Argument;

import Hospital.Exception.CannotChangeException;
import Hospital.People.Doctor;

/**
 * This class supports the adding of a Doctor to the argumentList
 * Care should be taken that this argument doesn't get exposed.
 * This class is for internal use only!
 */
public class DoctorArgument extends BasicArgument<Doctor> {

    /**
     * Constructor
     * @param question The question to ask for the Argument
     *                  For information purposes only.
     */
    public DoctorArgument(String question) {
        super(question);
    }

    /**
     * This enters a doctor as answer
     * @param d The doctor
     * @return this argument
     * @throws CannotChangeException if the Doctor was already set.
     */
    @Override
    public DoctorArgument setAnswer(Doctor d) throws CannotChangeException {
        super.setAnswer(d);
        return this;
    }
}
