package Hospital.Argument;

import java.util.logging.Level;
import java.util.logging.Logger;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.World.Time;

/**
 * Time version of Public Argument, used to ask a Time of the user.
 */
public class TimeArgument extends BasicArgument<Time> implements PublicArgument<Time>{

    /**
     * Constructor
     * @param question the question to aks the user
     */
    public TimeArgument(String question) {
        super(question);
    }

    /**
     * Takes a string and parses it to a time
     * @param ans A string containing year,month,day of month,hour,minutes with <i>[ ,/:]</i> as valid separators
     * @see Hospital.Argument.PublicArgument#enterAnswer(java.lang.String)
     */
    public TimeArgument enterAnswer(String ans) throws CannotChangeException, IllegalArgumentException {
        String[] args = ans.split("[ ,/:]");
        int[] numbers = new int[args.length];
        for(int i = 0; i < numbers.length; i++){
            numbers[i] = Integer.parseInt(args[i]);
        }
        try {
            setAnswer(new Time(numbers));
        } catch (ArgumentIsNullException ex) {
        	throw new Error("This cannot happen");
        } catch (WrongArgumentListException ex) {
                throw new IllegalArgumentException(ex);
        }
        return this;
    }

}
