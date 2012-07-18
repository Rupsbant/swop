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
     * @param ans not important
     * @return this
     * @throws CannotChangeException never
     * @throws IllegalArgumentException never
     */
    public PublicArgument enterAnswer(String ans) throws CannotChangeException, IllegalArgumentException {
        return this;
    }
}
