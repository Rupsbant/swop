package Hospital.Schedules.Constraints.Preference;

import Hospital.Schedules.Appointment;
import Hospital.Schedules.Schedule;
import Hospital.World.Campus;
import Hospital.World.Time;
import Hospital.World.TimeUtils;

/**
 * The user of this preference stays on the campus for a whole shift.
 * They only change campusses at noon between 11:45 and 12:00
 *                            at night between 23:44 and 23:59
 */
public class StayShiftPreference implements Preference {

    private static final Time NOON = new Time(0, 0, 0, 12, 0);
    private static final Time MIDNIGHT = new Time(0, 0, 0, 23, 59);
    HasPreference hasPreference;

    /**
     * Creates a new StayShiftPreference with the given HasPreference
     * @param hasPreference the Schedulable to check canAddAppointment with
     */
    public StayShiftPreference(HasPreference hasPreference) {
        this.hasPreference = hasPreference;
    }

    @Override
    public void makeThisAsPreference(HasPreference d) {
        d.setPreference(new StayShiftPreference(d));
    }

    @Override
    public boolean canAddAppointment(Time tf, int length, Campus campus) {
        if (!checkCampusForShift(tf.getTime(), tf, length, campus)) {
            return false;
        }
        return checkCampusForShift(tf.getLaterTime(length), tf, length, campus);
    }

    private boolean checkCampusForShift(Time startTime, Time startTimeToTest, int lengthOfAppointmentToTest, Campus campus) {
        Time startDay = TimeUtils.getStartOfDay(startTime);
        Time noonOfApp = TimeUtils.copyDay(startTime, NOON);
        Time midNightOfApp = TimeUtils.copyDay(startTime, MIDNIGHT);
        Time startShift = startTime.compareTo(noonOfApp) < 0 ? startDay : noonOfApp;
        Time endShift = startTime.compareTo(noonOfApp) < 0 ? noonOfApp : midNightOfApp;
        Schedule sched = hasPreference.getSchedule();
        Appointment app = sched.getAppointmentAfter(startShift);
        while (app != null && app.getTime().compareTo(endShift) < 0) {
            if (!app.collides(startTimeToTest, lengthOfAppointmentToTest)) {
                if (!app.getCampus().equals(campus)) {
                    return false;
                }
            }
            app = sched.getAppointmentAfter(app.getEndTime());
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "StayShiftPreference\n"+
                "Stay on one campus during the shift\n"+
                "Only change in the morning, or at noon.";
    }
}
