package Hospital.People.PeopleFactories;

import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Factory.Factory;
import Hospital.People.Staff;
import Hospital.World.CampusInfo;
import Hospital.World.World;

/**
 * A dummy inteface for a factory for creating staff members
 * This interface is used in filtering
 */
public interface StaffFactory extends Factory<Staff> {

    public Staff make(String name, World world, CampusInfo campus) throws InvalidArgumentException;
}
