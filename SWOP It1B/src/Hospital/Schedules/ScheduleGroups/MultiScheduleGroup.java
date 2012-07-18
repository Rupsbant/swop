package Hospital.Schedules.ScheduleGroups;

import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Schedules.Schedulable;
import Hospital.World.World;
import java.util.List;

/**
 * A group of multiple objects of the same subclass of Schedulable, used for resources (nurses, machines,...)
 * @param <S> the specific subclass
 */
public class MultiScheduleGroup<S extends Schedulable> implements ScheduleGroup {

    /**
     * The class-object of the specified subclass
     */
    private Class<S> resource;
    /**
     * The world in which the objects exist
     */
    private World world;

    /**
     * Constructor
     * @param resource the class of the resource to gather in this group
     * @throws ArgumentIsNullException resource was null
     */
    public MultiScheduleGroup(Class<S> resource) throws ArgumentIsNullException {
        if (resource == null) {
            throw new ArgumentIsNullException("resourceClass can't be null");
        }
        this.resource = resource;
    }

    /**
     * Sets the world in which to find the elements of this group
     * @param world the world to search
     * @throws CannotChangeException the world was already set
     * @throws ArgumentIsNullException world was null
     */
    public void setWorld(World world) throws CannotChangeException, ArgumentIsNullException {
        if (world == null) {
            throw new ArgumentIsNullException("Can't set null-World");
        }
        if (this.world != null) {
            throw new CannotChangeException("Cannot change world");
        }
        this.world = world;
    }

    /**
     * Gets a list of all objects of the class defined by resource in this world
     * @return the list of resource-objects as Schedulable
     */
    public List<Schedulable> getSchedulables() {
        return (List<Schedulable>) world.getResourceOfClass(resource);
    }
}
