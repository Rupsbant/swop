package Hospital.Argument;

import Hospital.SystemAPI;
import Hospital.Exception.CannotChangeException;
import Hospital.Schedules.Constraints.Priority.HighLowPriority;
import Hospital.Schedules.Constraints.Priority.Priority;
import java.util.HashMap;
import java.util.Map;

/**
 * An Argument to enter the Priority of a Appointment
 * @author SWOP-12
 */
@SystemAPI
public class PriorityArgument extends BasicArgument<Priority> implements PublicArgument<Priority>{

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
        out.put("high", Boolean.TRUE);
        out.put("urgent", Boolean.TRUE);
        out.put("1", Boolean.TRUE);
        out.put("low", Boolean.FALSE);
        out.put("normal", Boolean.FALSE);
        out.put("0", Boolean.FALSE);
        return out;
    }

    /**
     * Constructor with question
     * @param question the question to ask the user
     */
    public PriorityArgument(String question) {
        super(question);
    }

    /**
     * The interpreter of the answer
     * @param ans
     * @return this
     * @throws CannotChangeException if the Argument was already answered
     * @throws IllegalArgumentException if the answer was invalid
     */
    public PriorityArgument enterAnswer(String ans) throws CannotChangeException, IllegalArgumentException {
        Boolean answer = booleanMap.get(ans.toLowerCase());
        if (answer == null) {
            throw new IllegalArgumentException("Not a good answer, possible answers are: normal, urgent.");
        }
        setAnswer(new HighLowPriority(answer));
        return this;
    }
}
