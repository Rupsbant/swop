package Hospital.WareHouse;

import Hospital.Argument.Argument;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;

/**
 * Defines an interface for a factory to produce Items
 */
public interface ItemFactory {

    /**
     * Clones this item
     * @param args arguments to customise the clone
     * @return a clone of the prototype
     * @throws WrongArgumentListException the list of arguments didn't meet the requirements
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    Item clone(Argument[] args) throws WrongArgumentListException, InvalidArgumentException;

    /**
     * Gets the arguments required for creation of an item of this type
     * @return an array of Arguments which, when answered, can be used with clone()
     */
    Argument[] getArguments();
}
