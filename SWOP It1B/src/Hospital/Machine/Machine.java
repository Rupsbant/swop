package Hospital.Machine;

import Hospital.Argument.Argument;
import Hospital.Argument.CampusInfoArgument;
import Hospital.Argument.StringArgument;
import Hospital.Argument.WorldArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.CannotChangeException;
import Hospital.People.Unmovable;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;
import Hospital.Schedules.Constraints.Implementation.UnmovableConstraint;
import Hospital.Utils;
import Hospital.World.Campus;

/**
 * Machines are used in medical tests and treatments
 */
public abstract class Machine implements Schedulable, Unmovable {

    /**
     * Unique identifier
     */
    private String id;
    /**
     * The location on a campus
     */
    private Location location;
    /**
     * Schedule containing the appointments the machine is involved in
     */
    private Schedule schedule;
    /**
     * The campus this machine is located on.
     */
    private Campus campus;

    /**
     * Constructor
     * @param id the unique identifier of machine
     * @param location the machine's location
     */
    public Machine(String id, Location location) {
        this.schedule = new Schedule();
        this.id = id;
        this.location = location;
    }

    /**
     * Returns this Machine's schedule
     * @return schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Gets the machine's id
     * @return an identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the machine's location
     * @return a location in the hospital
     */
    Location getLocation() {
        return location;
    }

    /**
     * Returns true if obj is a machine and has the same UNIQUE id.
     * @param obj The object to test.
     * @return true if obj is machine and getId() equals obj.getId()
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof Machine) {
            return getId().equals(((Machine) obj).getId());
        }
        return false;
    }

    /**
     * Returns a hash of this object.
     * @return Hash based on machine-ID.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    /**
     * Gets the arguments used for the creation of a machine
     * @return an array of Arguments which, when answered, can be used for the creation of a machine
     */
    public Argument[] getEmptyArgumentList() {
        Argument[] out = new Argument[4];
        out[0] = new StringArgument("Enter the id of the machine");
        out[1] = new StringArgument("Enter the location of the machine");
        out[2] = new CampusInfoArgument("Enter the number of the campus");
        out[3] = new WorldArgument();
        return out;
    }

    public boolean validate(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args == null) {
            throw new ArgumentIsNullException("ArgumentList is null");
        }
        if (args.length != 4) {
            throw new WrongArgumentListException("Length of list is wrong");
        }
        Utils.getAnswer(StringArgument.class, "ID", args[0]);
        Utils.getAnswer(StringArgument.class, "Location", args[1]);
        Utils.getAnswer(CampusInfoArgument.class, "Campus", args[2]);
        Utils.getAnswer(WorldArgument.class, "World", args[3]);
        return true;
    }

    /**
     * This makes this object visit the TimeFrameConstraints during scheduling.
     * @param tf The timeFrame during which the constraints need to be satisfied.
     * @param tfContstraints The constraints that must be satisfied.
     * @return The constraints for simpler code : schedulable.setValidTimeFrame(tf, tfc).acceptAll();.
     */
    public TimeFrameConstraint setValidTimeFrame(TimeFrame tf, TimeFrameConstraint tfContstraints) {
        return tfContstraints.setValidTimeFrameSchedulable(tf, this);
    }

    /**
     * Returns the constraints apppointments needs to satifsfy.
     * @return
     */
    public TimeFrameConstraint getConstraints() {
        return new UnmovableConstraint(this);
    }

    /**
     * Returns the campus this machine is at.
     * @return
     */
    public Campus getCampus() {
        return campus;
    }

    /**
     * Sets the campus one time.
     */
    public Machine setCampus(Campus campus) throws CannotChangeException {
        if (this.campus != null) {
            throw new CannotChangeException("Can't set a campus of a machine a second time");
        }
        this.campus = campus;
        return this;
    }
}
