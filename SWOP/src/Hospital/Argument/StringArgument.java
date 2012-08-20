package Hospital.Argument;

import Hospital.SystemAPI;
import Hospital.Exception.CannotChangeException;

/**
 * String version of Argument, used to ask a String of the user.
 */
public class StringArgument extends BasicArgument<String> implements PublicArgument<String> {

    /**
     * Constructor.
     * @param question The question to answer.
     */
    @SystemAPI
    public StringArgument(String question) {
        super(question);
    }

    /**
     * This enters a String as answer
     * @param ans The answer to enter
     * @throws CannotChangeException if the answer was already set
     */
    @Override
    public void enterAnswer(String ans) throws CannotChangeException {
        setAnswer(ans);
    }
}
