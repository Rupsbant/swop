package Hospital.Argument;

import java.util.HashMap;
import java.util.Map;

import Hospital.SystemAPI;
import Hospital.Exception.CannotChangeException;

/**
 * Boolean version of Public Argument, used to ask a boolean value of the user.
 */
public class BooleanArgument extends BasicArgument<Boolean> implements PublicArgument<Boolean> {

    /**
     * The map with possible answers
     */
    private static final Map<String, Boolean> booleanMap = getBooleanMap();

    /**
     * This creates an exact copy of the map with possible answers.
     * All answers should be lowercase.
     * @return A map with all accepted answers
     */
    @SystemAPI
    public static Map<String, Boolean> getBooleanMap() {
        Map<String, Boolean> out = new HashMap<String, Boolean>();
        out.put("true", Boolean.TRUE);
        out.put("yes", Boolean.TRUE);
        out.put("no", Boolean.FALSE);
        out.put("false", Boolean.FALSE);
        return out;
    }

    /**
     * Creates a BooleanArgument
     * @param question The question to ask the user.
     */
    public BooleanArgument(String question) {
        super(question);
    }

    /**
     * This parses the answer to a boolean. All accepted answers can be found in getBooleanMap().
     * The accepted answers are case insensitive.
     * @param ans The answer to enter.
     * @throws CannotChangeException if the answer was already set.
     * @throws IllegalArgumentException if the answer was invalid, not found in the map.
     */
    @Override
    public void enterAnswer(String ans) throws CannotChangeException, IllegalArgumentException {
        Boolean answer = booleanMap.get(ans.toLowerCase());
        if (answer == null) {
            throw new IllegalArgumentException();
        }
        setAnswer(answer);
    }
}
