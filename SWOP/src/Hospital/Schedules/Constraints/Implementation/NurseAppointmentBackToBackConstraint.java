package Hospital.Schedules.Constraints.Implementation;

import Hospital.People.Nurse;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.Constraints.GetCampusConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;

public class NurseAppointmentBackToBackConstraint extends TimeFrameConstraint implements GetCampusConstraint {

    private Nurse nurse;
    private TimeFrame tf;

    public Boolean isAccepted() {
        if(tf == null || nurse == null){
            return false;
        }
        if (tf.getTime().getMinute() == 0) {
            return true;
        }
        Schedule schedule = nurse.getSchedule();
        Appointment prev = schedule.getAppointmentBefore(tf.getTime());
        if (prev == null) {
            return false;
        }
        int timeDiff = prev.getTimeFrame().getEndTime().getMinutesDiff(tf.getTime());
        return timeDiff == 0;

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

    public Campus getCampus() {
        if(nurse == null){
            return null;
        }
        return nurse.getCampus();
    }
}
