package Hospital.Argument;

import Hospital.SystemAPI;
import Hospital.Exception.CannotChangeException;

/**
 * A BasicArgument has a question to ask the user, the answer of which
 * must be able to be parsed to a given type.
 *
 * Children of this class are used to communicate between the UI and the SystemLayer.
 * Eg.:  Treatments sometimes needs different arguments: String/Integer/Boolean.
 * 
 * @param <T> the type of the answer to this Argument
 */
@SystemAPI
public abstract class BasicArgument<T extends Object> implements Argument<T> {

    /**
     * the question this BasicArgument provides an answer to
     */
    private String question;
    /**
     * the answer to the question of this BasicArgument
     */
    private T answer;

    /**
     * This creates the Argument with a given question
     * @param question the question to which this BasicArgument provides an answer
     */
    public BasicArgument(String question) {
        this.question = question;
    }

    /**
     * This sets the answer to the question.
     * @param ans The answer to set
     * @return this object as an object of type Argument 
     * @throws CannotChangeException the question was already answered
     */
    @SystemAPI
    Argument setAnswer(T ans) throws CannotChangeException {
        if (answer != null) {
            throw new CannotChangeException();
        }
        answer = ans;
        return this;
    }

    /**
     * Returns the parsed answer of the argument.
     * @return the parsed answer
     */
    @SystemAPI
    public T getAnswer() {
        return this.answer;
    }

    /**
     * Returns the question accompanied with this argument.
     * @return String: question
     */
    @SystemAPI
    public String getQuestion() {
        return question;
    }
}
