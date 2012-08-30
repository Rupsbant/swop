package Hospital.People;

import Hospital.Controllers.CampusController;
import Hospital.Controllers.NurseController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.CannotChangeException;
import Hospital.Schedules.Constraints.Implementation.UnmovableConstraint;
import Hospital.Schedules.Constraints.Implementation.WorkingHoursTimeConstraint;
import Hospital.Schedules.SchedulableVisitor;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.World.Campus;
import java.util.List;

/**
 * The nurse registers patients, and administers tests and treatments
 */
public class Nurse extends Staff implements Unmovable {

    private Campus campus;

    /**
     * Constructor
     * @param name the name of this nurse
     * @throws ArgumentConstraintException the name was empty
     * @throws ArgumentIsNullException the name was null
     */
    public Nurse(String name) throws ArgumentConstraintException, ArgumentIsNullException {
        super(name);
    }

    /**
     * Creates a NurseController of this person
     * @param cc the Campus from where is logged in
     * @return A NurseController with this nurse as primary actor.
     */
    @Override
    public NurseController login(CampusController cc) {
        return new NurseController(this, cc);
    }

    /**
     * Returns this Nurse's role.
     * @return String The role of this Nurse..
     */
    @Override
    public StaffRole getRole() {
        return StaffRole.Nurse;
    }

    /**
     * Returns the permanent campus of this nurse.
     * @return campus.
     */
    @Override
    public Campus getCampus() {
        return campus;
    }

    /**
     * Sets the campus of this nurse
     * @param campus the campus to set
     * @return this object
     * @throws CannotChangeException if the campus was already set
     */
    public Nurse setCampus(Campus campus) throws CannotChangeException {
        if (this.campus != null) {
            throw new CannotChangeException("Can't set a campus of a nurse a second time");
        }
        this.campus = campus;
        return this;
    }

    /**
     * Returns the constraints a appointment needs to satisfy in order to be accepted.
     * @return UnmovableConstraint(), nurses do not move.
     */
    @Override
    public List<TimeFrameConstraint> getConstraints() {
        List<TimeFrameConstraint> temp = super.getConstraints();
        temp.add(new UnmovableConstraint(this));
        return temp;
    }

    @Override
    public void visitConstraint(SchedulableVisitor tfContstraints) {
        super.visitConstraint(tfContstraints);
        tfContstraints.setNurse(this);
    }
}
