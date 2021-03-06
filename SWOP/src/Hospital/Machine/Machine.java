package Hospital.Machine;

import Hospital.Exception.CannotChangeException;
import Hospital.People.Unmovable;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.SchedulableVisitor;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.Constraints.Implementation.UnmovableConstraint;
import Hospital.World.Campus;
import java.util.Arrays;
import java.util.List;

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
    @Override
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
     * This makes this object visit the TimeFrameConstraints during scheduling.
     * @param schedulableVisitor The object that must be visited by this schedulable
     */
    @Override
    public void visitConstraint(SchedulableVisitor schedulableVisitor) {
        schedulableVisitor.setSchedulable(this);
    }

    /**
     * Returns the constraints apppointments needs to satifsfy.
     * @return
     */
    @Override
    public List<TimeFrameConstraint> getConstraints() {
        return Arrays.asList((TimeFrameConstraint)new UnmovableConstraint(this));
    }

    /**
     * Returns the campus this machine is at.
     * @return the campus this machine is stationed at
     */
    @Override
    public Campus getCampus() {
        return campus;
    }

    /**
     * Sets the campus one time.
     * @param campus The campus this machine must be placed on
     * @return this
     * @throws CannotChangeException if the campus was already set 
     */
    public Machine setCampus(Campus campus) throws CannotChangeException {
        if (this.campus != null) {
            throw new CannotChangeException("Can't set a campus of a machine a second time");
        }
        this.campus = campus;
        return this;
    }
}
