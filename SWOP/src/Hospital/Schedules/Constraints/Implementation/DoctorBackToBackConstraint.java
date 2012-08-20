package Hospital.Schedules.Constraints.Implementation;

import Hospital.People.Doctor;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Schedules.Schedule;
import Hospital.World.Campus;
import Hospital.World.Time;

/**
 * Constraints doctors so that they can only have appointments on the full hour or directly after a previous appointment (including walking time).
 */
public class DoctorBackToBackConstraint extends TimeFrameConstraint {

    private Campus campus;
    private Schedule schedule;
    private Time time;

    @Override
    public Time isAccepted() {
        if (schedule == null || time == null || campus == null) {
            return null;
        }
        //Base output is on the next hour.
        if (time.getTime().getMinute() == 0) {
            return time;
        }

        Appointment prev = schedule.getAppointmentBefore(time.getTime());
        if (prev != null) {
            int timeDiff = prev.getEndTime().getMinutesDiff(time.getTime());
            int walkTime = prev.getCampus().getTravelTime(campus);
            if (timeDiff == walkTime) {
                // If the WalkTime is correct, say yes with the current proposed time.
                return time;
            }
        }
        return time.getLaterTime(1);
    }

    @Override
    public void reset() {
        time = null;
        campus = null;
        schedule = null;
    }

    @Override
    public void setDoctor(Doctor d) {
        this.schedule = d.getSchedule();
    }

    @Override
    public void setCampus(Campus c) {
        this.campus = c;
    }

    @Override
    public void setTime(Time tf, int length) {
        this.time = tf;
    }
}
