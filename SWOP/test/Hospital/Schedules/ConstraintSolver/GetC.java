package Hospital.Schedules.ConstraintSolver;

import Hospital.Schedules.Constraints.CampusDecider;
import Hospital.World.Campus;

public class GetC extends CampusDecider {

    public GetC(Campus campus) {
        this.campus = campus;
    }

    public Campus getCampus() {
        return this.campus;
    }
    
    private Campus campus;
}
