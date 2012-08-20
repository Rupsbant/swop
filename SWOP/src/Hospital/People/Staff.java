package Hospital.People;

import Hospital.Controllers.CampusController;
import Hospital.Controllers.LoginController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Constraints.Implementation.WorkingHoursTimeConstraint;
import Hospital.Schedules.SchedulableVisitor;
import java.util.List;

/**
 * Staff represents hospital staff that can log into the system.
 */
public abstract class Staff extends Person {

    /**
     * Constructor
     * @param name The name of the new staff member.
     * @throws ArgumentConstraintException if the name is invalid (empty,duplicate,...)
     * @throws ArgumentIsNullException if the name is null
     */
    public Staff(String name) throws ArgumentConstraintException, ArgumentIsNullException {
        super(name);
    }

    /**
     * Logs this staff member in.
     * @param cc the campus from where is logged in
     * @return The LoginController representing the staff member during his/her session.
     */
    public abstract LoginController login(CampusController cc);

    /**
     * Returns the info needed for login.
     * @return LoginInfo for this PersonWithLogin.
     */
    public LoginInfo getLoginInfo() {
        try {
            return new LoginInfo(getName(), getRole());
        } catch (ArgumentIsNullException e) {
            throw new Error("This is not null");
        }
    }

    /**
     * Returns this person's role.
     * @return String The role of this person.
     */
    public abstract StaffRole getRole();

    /**
     * Returns a description of this Staff-member.
     * @return "$name"
     */
    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * Returns whether obj is equal to this.
     * @param obj Object to test
     * @return true if obj is Staff and the UNIQUE name is equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Staff) {
            return getName().equals(((Staff) obj).getName());
        }
        return false;
    }

    /**
     * Returns a hashCode of this object.
     * @return a hashCode based on the name.
     */
    @Override
    public int hashCode() {
        return getName().hashCode() * 47;
    }

    @Override
    public void visitConstraint(SchedulableVisitor tfContstraints){
        super.visitConstraint(tfContstraints);
        tfContstraints.setStaff(this);
    }

    @Override
    public List<TimeFrameConstraint> getConstraints() {
        List<TimeFrameConstraint> temp = super.getConstraints();
        temp.add(new WorkingHoursTimeConstraint());
        return temp;
    }
}
