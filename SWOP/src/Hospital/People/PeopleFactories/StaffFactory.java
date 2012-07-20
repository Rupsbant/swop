package Hospital.People.PeopleFactories;

import Hospital.Argument.Argument;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Factory.Factory;
import Hospital.People.Staff;

/**
 * A dummy inteface for a factory for creating staff members
 * This interface is used in filtering
 */
public interface StaffFactory extends Factory<Staff> {

    public Staff make(Argument[] args) throws WrongArgumentListException, InvalidArgumentException;

    public Argument[] getEmptyArgumentList();

    public boolean validate(Argument[] args) throws WrongArgumentListException, InvalidArgumentException;
}
