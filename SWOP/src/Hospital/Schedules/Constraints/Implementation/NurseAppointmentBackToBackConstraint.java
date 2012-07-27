package Hospital.Schedules.Constraints.Implementation;

import Hospital.People.Nurse;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;

public class NurseAppointmentBackToBackConstraint extends TimeFrameConstraint {

    private Nurse nurse;
    private TimeFrame tf;

    public TimeFrame isAccepted() {
        if(tf == null || nurse == null){
            return null;
        }
        if (tf.getTime().getMinute() == 0) {
            return tf;
        }
        Schedule schedule = nurse.getSchedule();
        Appointment prev = schedule.getAppointmentBefore(tf.getTime());
        if (prev == null) {
            return tf.next();
        }
        int timeDiff = prev.getTimeFrame().getEndTime().getMinutesDiff(tf.getTime());
        if(timeDiff == 0){
            return tf;
        } else {
            return tf.next();
        }

    }

    public void reset() {
        this.nurse = null;
        this.tf = null;
    }

    @Override
    public void setTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }

    @Override
    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }
}
