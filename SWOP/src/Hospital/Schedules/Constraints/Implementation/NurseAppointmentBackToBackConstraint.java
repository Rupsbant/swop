package Hospital.Schedules.Constraints.Implementation;

import Hospital.People.Nurse;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Schedule;
import Hospital.World.Time;

public class NurseAppointmentBackToBackConstraint extends TimeFrameConstraint {

    private Nurse nurse;
    private Time startTime;
    private int length;

    public Time isAccepted() {
        if(startTime == null || nurse == null){
            return null;
        }
        if (startTime.getTime().getMinute() == 0) {
            return startTime;
        }
        Schedule schedule = nurse.getSchedule();
        Appointment prev = schedule.getAppointmentBefore(startTime.getTime());
        if (prev == null) {
            return startTime.getLaterTime(1);
        }
        int timeDiff = prev.getEndTime().getMinutesDiff(startTime.getTime());
        if(timeDiff == 0){
            return startTime;
        } else {
            return startTime.getLaterTime(1);
        }

    }

    public void reset() {
        this.nurse = null;
        this.startTime = null;
    }

    @Override
    public void setTime(Time tf, int length) {
        this.startTime = tf;
        this.length = length;
    }

    @Override
    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }
}
