package Hospital.Schedules.Constraints.Implementation;

import Hospital.People.Nurse;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.Constraints.GetCampusConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraintImplementation;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;

public class NurseAppointmentBackToBackConstraint extends TimeFrameConstraintImplementation implements GetCampusConstraint {

    private Nurse nurse;
    private TimeFrame tf;

    @Override
    protected void setValidTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }

    @Override
    protected void setValidNurse(Nurse nurse) {
        this.nurse = nurse;
    }

    @Override
    protected Boolean isAccepted() {
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

    @Override
    protected void resetValid() {
        this.nurse = null;
        this.tf = null;
    }

    public Campus getCampus() {
        if(nurse == null){
            return null;
        }
        return nurse.getCampus();
    }
}
