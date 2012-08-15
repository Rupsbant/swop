package Hospital.People;

import Hospital.Controllers.CampusController;
import Hospital.Controllers.NurseController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.CannotChangeException;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Constraints.Implementation.UnmovableConstraint;
import Hospital.Schedules.SchedulableVisitor;
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
    public Campus getCampus() {
        return campus;
    }

    /**
     * Sets the campus one time.
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
    public List<TimeFrameConstraint> getConstraints() {
        List<TimeFrameConstraint> temp = super.getConstraints();
        temp.add(new UnmovableConstraint(this));
        return temp;
    }


    /**
     * This method makes this object visit the constraints to approve them as a Schedulable and as a Staffmember.
     * @param tf The TimeFrame during which the constraints must be checked.
     * @param tfContstraints The list of constraints.
     * @return The constraints for simpler code : doctor.setValidTimeFrame(tf, tfc).isAccepted();.
     */
    @Override
    public void visitConstraint(SchedulableVisitor tfContstraints){
        super.visitConstraint(tfContstraints);
        tfContstraints.setNurse(this);
    }
}
