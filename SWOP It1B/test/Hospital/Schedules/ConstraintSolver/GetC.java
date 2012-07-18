package Hospital.Schedules.ConstraintSolver;

import Hospital.Schedules.Constraints.BasicValidationTimeFrameConstraint;
import Hospital.Schedules.Constraints.GetCampusConstraint;
import Hospital.World.Campus;

public class GetC extends BasicValidationTimeFrameConstraint implements GetCampusConstraint {

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
