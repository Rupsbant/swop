package Hospital.Schedules.Constraints.Implementation;

import Hospital.People.Staff;
import Hospital.Schedules.Constraints.BasicTimeFrameConstraint;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Time;
import Hospital.World.TimeUtils;

/**
 * Defines the constraint on the availability of nurses
 */
public class WorkingHoursTimeConstraint extends BasicTimeFrameConstraint {
    private static final Time dayStart = new Time(0, 0, 0, 9, 0);
    private static final Time dayEnd = new Time(0, 0, 0, 17, 0);

    /**
     * Constructor
     */
    public WorkingHoursTimeConstraint() {
    }

    /**
     * @see Hospital.Schedules.Constraints.TimeFrameConstraint#setValidNurse(Hospital.Schedules.TimeFrame, Hospital.People.Nurse)
     */
    @Override
    protected void setValidStaff(TimeFrame tf, Staff n) {
        Time start = TimeUtils.copyDay(tf.getTime(), dayStart);
        Time end = TimeUtils.copyDay(tf.getTime(), dayEnd);
        if(tf.compareTo(start)<0){
            setValid(false);
        } else if(end.compareTo(tf.getEndTime())<0){
            setValid(false);
        } else {
            if(isAccepted() == null){
                setValid(true);
            }
        }
    }
}
