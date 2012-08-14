package Hospital;

import Hospital.Argument.PublicArgument;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import java.util.Arrays;

/**
 * This class contains various utility functions used throughout the system.
 */
public class Utils {

    /**
     * Returns a list containing only the elements of the given class.
     * @param <S> ClassType to filter by.
     * @param in The original list
     * @param clazz The class to filter by
     * @return a list containing all elements of type <i>clazz</i> that were in <i>in</i>
     */
    public static <S> List<S> filter(List<? extends Object> in, Class<S> clazz) {
        List<S> out = new ArrayList<S>();
        for (Object element : in) {
            if (clazz.isInstance(element)) {
                out.add((S) element);
            }
        }
        return out;
    }

    /**
     * The map version of filter(List,Class)
     * @param <K> Key-class to create new Map with
     * @param <S> Value-class to create Map and filter with.
     * @param in The original map
     * @param clazz The class to filter by
     * @return a map containing all elements of type <i>clazz</i> that were in <i>in</i>
     */
    public static <K, S> Map<K, S> filter(Map<K, ? extends Object> in, Class<S> clazz) {
        Map<K, S> out = new HashMap<K, S>();
        for (Entry<K, ?> element : in.entrySet()) {
            if (clazz.isInstance(element.getValue())) {
                out.put(element.getKey(), (S) element.getValue());
            }
        }
        return out;
    }

    /**
     * Gives back the list with all elements casted to a given class.
     * @param <S> type to cast up to.
     * @param <O> basetype to cast from.
     * @param in the original list
     * @param clazz The class to cast the elements to (must be a superclass of the elements in the original list)
     * @return a new list with all elements casted to <i>clazz</i>
     */
    public static <S, O extends S> List<S> castUp(List<O> in, Class<S> clazz) {
        return filter(in, clazz);
    }

    /**
     * Gives back an array as a list.
     * @param <T> Type to create list with.
     * @param in The original array
     * @return the list-version of the original array
     */
    public static <T> List<T> toList(T[] in) {
        return Arrays.asList(in);
    }

    /**
     * Checks whether a string is empty 
     * @param str the string to be checked
     * @return true if the string is empty or null
     *         false otherwise
     */
    public static boolean testEmpty(String str) {
        if ((str == null) || str.matches("^\\s*$")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * A safe method of reading the answer to an Argument-object
     * @param <S> Type clazz must have.
     * @param clazz the class of the Argument-type used
     * @param description the question answered by this Argument
     * @param arg the Argument from which to get the answer
     * @return the parsed value of the answer
     * @throws ArgumentIsNullException the given Argument is null
     * @throws WrongArgumentListException the given Argument differs from the type specified by clazz
     * @throws ArgumentNotAnsweredException the given Argument was not yet anwered
     */
    public static <O extends Object, S extends PublicArgument<O>> O getAnswer(Class<S> clazz, String description, PublicArgument arg)
            throws ArgumentIsNullException, WrongArgumentListException, ArgumentNotAnsweredException {
        if (arg == null) {
            throw new ArgumentIsNullException("Argument \"" + description + "\" was null");
        }
        if (!(clazz.isInstance(arg))) {
            throw new WrongArgumentListException("Argument is of the wrong type, it should be " + clazz.getSimpleName());
        }
        Object out = clazz.cast(arg).getAnswer();
        if (out == null) {
            throw new ArgumentNotAnsweredException("Argument-answer of \"" + description + "\" was null");
        }
        return (O) out;
    }

    /**
     * This method merges two arrays in one
     * @param first, the first part of the new array
     * @param second, the second part of the new array
     */
    public static <T> T[] merge(T[] first, T[] second) {
        int lastPos = first.length;
        first = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, first, lastPos, second.length);
        return first;
    }
}
