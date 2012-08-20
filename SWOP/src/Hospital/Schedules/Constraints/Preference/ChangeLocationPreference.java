package Hospital.Schedules.Constraints.Preference;

import Hospital.Schedules.Appointment;
import Hospital.Schedules.Schedule;
import Hospital.World.Campus;
import Hospital.World.Time;
import Hospital.World.TimeUtils;

/**
 * The user of this preference only changes between campusses 4 times during the day.
 * Every new appointment is tested if it still fits this constraint.
 */
public class ChangeLocationPreference implements Preference {

    private static final int MAX_CHANGES = 4;
    private HasPreference schedulable;

    /**
     * Creates a new ChangeLocationPreference with the given HasPreference
     * @param hasPreference the Schedulable to check canAddAppointment with
     */
    public ChangeLocationPreference(HasPreference hasPreference) {
        this.schedulable = hasPreference;
    }

    @Override
    public void makeThisAsPreference(HasPreference d) {
        d.setPreference(new ChangeLocationPreference(d));
    }

    @Override
    public boolean canAddAppointment(Time tf, int length, Campus campus) {
        Time start = TimeUtils.getStartOfDay(tf.getTime());
        Time end = TimeUtils.addDay(start);
        Schedule sched = schedulable.getSchedule();
        Appointment appointmentAfter = sched.getAppointmentAfter(start);
        if (appointmentAfter == null) {
            return true;
        }
        Campus currentCampus = appointmentAfter.getCampus();
        start = appointmentAfter.getEndTime();
        int changeCount = getChangeCount(sched, start, end, tf, length, currentCampus);

        Appointment appointmentBefore = sched.getAppointmentBefore(tf.getTime());
        appointmentAfter = sched.getAppointmentAfter(tf.getLaterTime(length));
        if(appointmentBefore != null && appointmentBefore.getEndTime().compareTo(TimeUtils.getStartOfDay(tf.getTime()))<=0){
            appointmentBefore = null;
        }
        if(appointmentAfter != null &&appointmentAfter.getTime().compareTo(end)>=0){
            appointmentAfter = null;
        }
        if (appointmentBefore != null && appointmentAfter != null) {
            Campus beforeCampus = appointmentBefore.getCampus();
            Campus afterCampus = appointmentAfter.getCampus();
            if (beforeCampus.equals(afterCampus) && !beforeCampus.equals(campus)) {
                changeCount += 2;
            }
        } else if (appointmentAfter == null && appointmentBefore != null) {
            Campus beforeCampus = appointmentBefore.getCampus();
            if (!beforeCampus.equals(campus)) {
                changeCount++;
            }
        } else if (appointmentBefore == null && appointmentAfter != null) {
            Campus afterCampus = appointmentAfter.getCampus();
            if (!afterCampus.equals(campus)) {
                changeCount++;
            }
        } else {
        }

        //TODO in case of long appointments
        //Check this for the day the appointment ends too.
        //int changeCount2 = getChangeCount(sched, start2, end2, tf, campus);
        //WARNING: This is not logical, make a running constraint of maximum 4.
        return changeCount <= MAX_CHANGES;
    }

    private int getChangeCount(Schedule sched, Time start, Time end, Time startTimeToTest, int length, Campus currentCampus) {
        int changeCount = 0;
        while (true) {
            Appointment p = sched.getAppointmentAfter(start);
            if (p == null || p.getTime().compareTo(end) >= 0) {
                break;
            }
            if (p.collides(startTimeToTest, length)) {
                start = p.getEndTime();
                continue;
            }
            Campus c = p.getCampus();
            if (!currentCampus.equals(c)) {
                changeCount++;
                currentCampus = c;
            }
            start = p.getEndTime();
        }
        return changeCount;
    }

    @Override
    public String getDescription() {
        return "ChangeLocationPreference\n"
                + "Change at most four times of location during the day.";
    }
}
