package Hospital.Argument;

import Hospital.SystemAPI;

/**
 * This class holds the answer to some information that is needed.
 * A question is given for information purposes.
 * @author SWOP-12
 * @param <T> The type of the argument to be answered
 */
@SystemAPI
public interface Argument<T extends Object> {

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
