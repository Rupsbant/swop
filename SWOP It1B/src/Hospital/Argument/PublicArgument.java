package Hospital.Argument;

import Hospital.SystemAPI;
import Hospital.Exception.CannotChangeException;

/**
 * Public arguments contain a method to parse the string put in by the user.
 * @param <T> the type of the answer to this PublicArgument
 */
@SystemAPI
public interface PublicArgument<T extends Object> extends Argument<T> {
	
    /**
     * This method parses a String input to the correct ArgumentType
     * @param ans String to be parsed
     * @return this object as an object of the type PublicArgument
     * @throws CannotChangeException if the answer was already set
     * @throws IllegalArgumentException if the answer was invalid
     */
	@SystemAPI
    PublicArgument enterAnswer(String ans) throws CannotChangeException, IllegalArgumentException;
}
