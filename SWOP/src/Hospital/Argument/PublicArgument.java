package Hospital.Argument;

import Hospital.Exception.CannotChangeException;
import Hospital.SystemAPI;

/**
 * This class holds the answer to some information that is needed.
 * A question is given for information purposes.
 * @author SWOP-12
 * @param <T> The type of the argument to be answered
 */
@SystemAPI
public interface PublicArgument<T extends Object> {

    /**
     * This method parses a String input to the correct ArgumentType
     * @param ans String to be parsed
     * @throws CannotChangeException if the answer was already set
     * @throws IllegalArgumentException if the answer was invalid
     */
    @SystemAPI
    void enterAnswer(String ans) throws CannotChangeException, IllegalArgumentException;

    /**
     * This returns the answer that was entered, null if no answer was given.
     * @return answer to the question
     */
    @SystemAPI
    T getAnswer();

    /**
     * This returns the question to be answered. This can be translated with a map if necessary.
     * @return The question.
     */
    @SystemAPI
    String getQuestion();
}
