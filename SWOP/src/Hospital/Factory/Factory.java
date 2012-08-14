package Hospital.Factory;

import Hospital.Argument.PublicArgument;
import Hospital.Controllers.ArgumentList;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.Arguments.InvalidArgumentException;

/**
 *  An interface for the factory pattern used for treatments, medical tests, staff and machines
 * @param <Fac> the type of object produced in the factory
 */
public interface Factory<Fac> {

    /**
     * Creates an object of the type Fac
     * @param args arguments to the creation of the new object
     * @return the newly created object
     * @throws WrongArgumentListException the given arguments did not match the requirements for Fac
     * @throws IllegalArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    public Fac make(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException;
    
    /**
     * Tests if the argumentList satisfies the constraints
     * @param args arguments to the creation of the new object
     * @return the newly created object
     * @throws WrongArgumentListException the given arguments did not match the requirements for Fac
     * @throws IllegalArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    public boolean validate(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException;

    /**
     * Gets the arguments required for the creation of an object in this factory
     * @return an array of Arguments which, when answered, can be used for creation of an object of type Fac
     */
    public PublicArgument[] getEmptyArgumentList();

    /**
     * Returns the name of this factory, used to identify it
     * @return the name of this factory
     */
    public String getName();
}
