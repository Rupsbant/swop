package Hospital.Schedules.Constraints.Implementation;

import Hospital.People.Unmovable;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;

public class UnmovableConstraint extends TimeFrameConstraint {

    private Unmovable unmovable;
    private Campus campus;

    public UnmovableConstraint(Unmovable unmovable) {
        this.unmovable = unmovable;
    }

    @Override
    public void setCampus(Campus c) {
        this.campus = c;
    }

    public Boolean isAccepted() {
        if(campus == null){
            return null;
        }
        return unmovable.getCampus().equals(campus);
    }

    public void reset() {
        campus = null;
    }

    @Override
    public void setTimeFrame(TimeFrame tf) {
        // does nothing
    }
}
