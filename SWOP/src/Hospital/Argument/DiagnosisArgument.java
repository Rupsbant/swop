package Hospital.Argument;

import Hospital.Exception.CannotChangeException;
import Hospital.Patient.Diagnosis;

/**
 * This class supports the adding of a Diagnosis to the argumentList
 * Care should be taken that this arguments doesn't get exposed.
 * This class is for internal use only!
 */
public class DiagnosisArgument extends BasicArgument<Diagnosis> {

    /**
     * Constructor
     * @param question The question to ask for the Argument
     *                  For information purposes only.
     */
    public DiagnosisArgument(String question) {
        super(question);
    }

    /**
     * Enters a diagnosis as answer
     * @param d The diagnosis to enter
     * @throws CannotChangeException if a diagnosis was already entered.
     * @return This argument
     */
    @Override
    public DiagnosisArgument setAnswer(Diagnosis d) throws CannotChangeException {
        super.setAnswer(d);
        return this;
    }
}
