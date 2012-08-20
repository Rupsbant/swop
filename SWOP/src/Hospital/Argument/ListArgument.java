package Hospital.Argument;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.CannotChangeException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a Argument to select a object from a list.
 * @author SWOP-12
 * @param <E> the Type of the Object that is selected
 */
public class ListArgument<E extends Object> implements PublicArgument<E> {

    private List<E> possible;
    private int position = -1;
    private String question;

    /**
     * Basic constructor, no possible answers
     * @param question the question to ask
     */
    public ListArgument(String question) {
        this.question = question;
        this.possible = new ArrayList<E>();
    }

    /**
     * Constructor
     * @param question the question to ask
     * @param possible the list of possible answers
     * @throws ArgumentIsNullException if possible is null
     */
    public ListArgument(String question, List<E> possible) throws ArgumentIsNullException {
        if (possible == null) {
            throw new ArgumentIsNullException("Isn't it obvious?");
        }
        this.question = question;
        this.possible = new ArrayList<E>(possible);
    }

    /**
     * Sets the possible answers
     * @param possible the list of possible answers
     */
    protected void setPossible(List<E> possible) {
        this.possible = possible;
    }

    /**
     * Returns the question to ask the user
     * @return a question concatenated with a list of possible answers and their number
     */
    @Override
    public String getQuestion() {
        if (possible == null) {
            return "Argument was not populated, ask an administrator for help";
        }
        String out = question + "\n";
        int i = 0;
        for (E pos : possible) {
            out += (++i) + ": " + pos.toString() + "\n";
        }
        return out;
    }

    /**
     * This parses the String to the index of the chosen item in the list
     * @param ans The number to parse, the index of the list
     * @throws CannotChangeException if the answer was already set
     * @throws IllegalArgumentException if the answer was invalid or out of range.
     */
    @Override
    public void enterAnswer(String ans) throws CannotChangeException, IllegalArgumentException {
        if (possible == null) {
            return;
        }
        if (position != -1) {
            throw new CannotChangeException();
        }
        position = Integer.parseInt(ans) - 1;
        if (position < 0 || position >= possible.size()) {
            position = -1;
            throw new IllegalArgumentException("The entered number was not a valid position");
        }
    }

    /**
     * Returns the entered answer
     * @return the answer
     */
    @Override
    public E getAnswer() {
        if (position < 0) {
            return null;
        }
        return possible.get(position);
    }
}
