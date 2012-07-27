package Hospital.Schedules.Constraints.Implementation;

import Hospital.People.Unmovable;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;

public class UnmovableConstraint extends TimeFrameConstraint {

    private Unmovable unmovable;
    private Campus campus;
    private TimeFrame tf;

    public UnmovableConstraint(Unmovable unmovable) {
        this.unmovable = unmovable;
    }

    @Override
    public void setCampus(Campus c) {
        this.campus = c;
    }

    public TimeFrame isAccepted() {
        if(campus == null){
            return null;
        }
        if(unmovable.getCampus().equals(campus)){
            return tf;
        } else {
            return tf.next();
        }
    }

    public void reset() {
        campus = null;
    }

    @Override
    public void setTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }
}
