package Hospital.Schedules.ConstraintSolver;

import Hospital.Schedules.Constraints.GetCampusConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraintImplementation;
import Hospital.World.Campus;

public class GetC extends TimeFrameConstraintImplementation implements GetCampusConstraint {

    public GetC(Campus campus) {
        this.campus = campus;
    }

    @Override
    protected Boolean isAccepted() {
        return true;
    }

    @Override
    protected void resetValid() {
    }

    public Campus getCampus() {
        return this.campus;
    }
    
    private Campus campus;
}
