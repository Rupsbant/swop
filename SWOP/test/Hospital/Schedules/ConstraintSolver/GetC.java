package Hospital.Schedules.ConstraintSolver;

import Hospital.Schedules.Constraints.GetCampusConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;

public class GetC extends TimeFrameConstraint implements GetCampusConstraint {

    public GetC(Campus campus) {
        this.campus = campus;
    }

    public Boolean isAccepted() {
        return true;
    }

    public void reset() {
    }

    public Campus getCampus() {
        return this.campus;
    }
    
    private Campus campus;

    public void setTimeFrame(TimeFrame tf) {
    }
}
