package Hospital.Factory;

import Hospital.Argument.PublicArgument;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;

/**
 * An interface for the results of medical tests
 */
public interface Result {

    /**
     * Gets the arguments of this type of Result
     * @return an array of PublicArguments which, when answered, can be used for entering the details of a Result
     */
    public PublicArgument[] getEmptyResultArgumentList();

    /**
     * Enters the details of the result with the given arguments.
     * To get a good list, use getEmptyResultArgumentList()
     * @param args the details to be entered
     * @throws WrongArgumentListException the given list of arguments did not match this type of Result
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    public void enterResult(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException;

    /**
     * Validates the argumentList for this factory.
     * @param args the details to be entered
     * @returns true if no errors are thrown, the enterResult method will succeed
     * @throws WrongArgumentListException the given list of arguments did not match this type of Result
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    public boolean validateResults(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException;

    /**
     * Checks whether the details of the result are entered
     * @return true if the details are entered, false otherwise
     */
    public boolean isResultEntered();

    /**
     * Reads the details of the result
     * @return a formatted string containing the details of the result
     */
    public String getResultString();
}
