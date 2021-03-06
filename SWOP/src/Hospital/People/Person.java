package Hospital.People;

import Hospital.Schedules.SchedulableVisitor;
import Hospital.Utils;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.Schedule;
import java.util.ArrayList;
import java.util.List;

/**
 * A person with a name and role, who can be scheduled for activities
 */
public abstract class Person implements Schedulable {

    /**
     * this person's schedule
     */
    private Schedule schedule;
    /**
     * this person's name
     */
    private String name;

    /**
     * Constructor
     * @param name the name of this person
     * @throws ArgumentConstraintException the name was empty
     * @throws ArgumentIsNullException the name was null
     */
    public Person(String name) throws ArgumentConstraintException, ArgumentIsNullException {
        checkName(name);
        this.name = name;
        this.schedule = new Schedule();
    }

    @Override
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Returns the name of the Person
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks whether the given name is a valid one
     * @param name the name to check
     * @throws ArgumentConstraintException the name is empty
     * @throws ArgumentIsNullException the name is null
     */
    private void checkName(String name) throws ArgumentConstraintException, ArgumentIsNullException {
        if (!isValidName(name)) {
            throw new ArgumentConstraintException("Name cannot be empty!");
        }
    }

    /**
     * Checks wether the given name is valid
     * @param name the name to check
     * @return true (throws an exception if the name is invalid)
     * @throws ArgumentIsNullException the given name was null
     * @throws ArgumentConstraintException the given name was empty
     */
    private boolean isValidName(String name) throws ArgumentIsNullException, ArgumentConstraintException {
        if (name == null) {
            throw new ArgumentIsNullException("Name is null");
        }
        if (Utils.testEmpty(name)) {
            throw new ArgumentConstraintException("Name cannot be empty!");
        }
        return true;
    }

    /**
     * Returns a description of this person.
     * @return "$name"
     */
    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public void visitConstraint(SchedulableVisitor tfContstraints) {
        tfContstraints.setSchedulable(this);
    }

    @Override
    public List<TimeFrameConstraint> getConstraints() {
        return new ArrayList<TimeFrameConstraint>();
    }
}
