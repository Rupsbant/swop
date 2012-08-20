package Hospital.Argument;

import Hospital.Exception.CannotChangeException;

/**
 * Integer version of Public Argument, used to ask an integer value of the user.
 */
public class IntegerArgument extends BasicArgument<Integer> implements PublicArgument<Integer> {

    /**
     * Constructor.
     * @param question The question to answer
     */
    public IntegerArgument(String question) {
        super(question);
    }

    /**
     * This parses a String to an Integer as answer
     * @param ans The number to parse
     * @throws CannotChangeException if the answer was already set
     * @throws IllegalArgumentException if the answer was invalid
     */
    @Override
    public void enterAnswer(String ans) throws CannotChangeException, IllegalArgumentException {
        Integer answer = Integer.parseInt(ans);
        setAnswer(answer);
    }
}
