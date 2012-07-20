package Hospital.Argument;

import Hospital.SystemAPI;
import Hospital.Exception.CannotChangeException;

/**
 * String version of Public Argument, used to ask a String of the user.
 */
public class StringArgument extends BasicArgument<String>  implements PublicArgument<String>{
    
	/**
     * Constructor.
     * @param question The question to answer.
     */
	@SystemAPI
    public StringArgument(String question) {
        super(question);
    }

    /**
     * This enters a String  as answer
     * @param ans The answer to enter
     * @return This object
     * @throws CannotChangeException if the answer was already set
     */
    @Override
    public StringArgument enterAnswer(String ans) throws CannotChangeException {
        setAnswer(ans);
        return this;
    }

}
