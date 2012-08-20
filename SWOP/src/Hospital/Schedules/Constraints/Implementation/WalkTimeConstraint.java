package Hospital.Schedules.Constraints.Implementation;

import Hospital.Schedules.Appointment;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.World.Campus;
import Hospital.World.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the constraint that every schedule needs to have enough time to walk campusses.
 * The previous appointment and the tested appointment and the next appointment and the tested appointment are checked.
 * @author Rupsbant
 */
public class WalkTimeConstraint extends TimeFrameConstraint {

    private Campus campus;
    private List<Schedule> schedules = new ArrayList<Schedule>();
    private Time startTime;
    private int length;

    @Override
    public Time isAccepted() {
        if (startTime == null || campus == null) {
            return null;
        }
        Time out = startTime.getTime();
        for (Schedule schedule : schedules) {
            Time out2 = handleNext(schedule);
            out = (out.compareTo(out2) > 0 ? out : out2);
            out2 = handlePrevious(schedule);
            out = (out.compareTo(out2) > 0 ? out : out2);
        }
        return out;
    }

    private Time handlePrevious(Schedule schedule) {
        Appointment prev = schedule.getAppointmentBefore(startTime.getTime());
        if (prev != null) {
            int timeDiff = prev.getEndTime().getMinutesDiff(startTime.getTime());
            int walkTime = prev.getCampus().getTravelTime(campus);
            if (timeDiff < walkTime) {
                //if not enough time, add more
                return prev.getEndTime().getLaterTime(walkTime);
            }
        }
        return startTime.getTime();
    }

    private Time handleNext(Schedule schedule) {
        Time endTime = startTime.getLaterTime(length);
        Appointment next = schedule.getAppointmentAfter(endTime);
        if (next != null) {
            int timeDiff = next.getTime().getMinutesDiff(endTime);
            int walkTime = next.getCampus().getTravelTime(campus);
            if (timeDiff < walkTime) {
                // if not enough, start on first colliding moment
                return next.getTime().getLaterTime(-length+1);
            }
        }
        return startTime.getTime();
    }

    @Override
    public void setSchedulable(Schedulable s) {
        schedules.add(s.getSchedule());
    }

    @Override
    public void setCampus(Campus c) {
        this.campus = c;
    }

    @Override
    public void setTime(Time tf, int length) {
        this.startTime = tf;
        this.length = length;
    }

    @Override
    public void reset() {
        this.startTime = null;
        this.campus = null;
        this.schedules.clear();
    }
}
