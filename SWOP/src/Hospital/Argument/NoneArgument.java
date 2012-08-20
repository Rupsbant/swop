package Hospital.Argument;

import Hospital.Exception.CannotChangeException;

/**
 * a Argument that does nothing
 * @author SWOP-12
 */
public class NoneArgument extends BasicArgument<Object> implements PublicArgument<Object> {

    /**
     * Constructor
     * @param question the question to ask the user
     */
    public NoneArgument(String question) {
        super(question);
    }

    /**
     * Does nothing
     * @param ans is unused
     * @throws CannotChangeException never
     * @throws IllegalArgumentException never
     */
    public void enterAnswer(String ans) throws CannotChangeException, IllegalArgumentException {
    }
}
