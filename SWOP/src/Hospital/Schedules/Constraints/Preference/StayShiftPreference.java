package Hospital.Schedules.Constraints.Preference;

import Hospital.Schedules.Appointment;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;
import Hospital.World.Time;
import Hospital.World.TimeUtils;

public class StayShiftPreference implements Preference {

    private static final Time NOON = new Time(0, 0, 0, 12, 0);
    private static final Time MIDNIGHT = new Time(0, 0, 0, 23, 59);
    HasPreference hasPreference;

    public StayShiftPreference(HasPreference hasPreference) {
        this.hasPreference = hasPreference;
    }

    public void makeThisAsPreference(HasPreference d) {
        d.setPreference(new StayShiftPreference(d));
    }

    public boolean canAddAppointment(TimeFrame tf, Campus campus) {
        if (!checkCampusForShift(tf.getTime(), tf, campus)) {
            return false;
        }
        return checkCampusForShift(tf.getEndTime(), tf, campus);
    }

    private boolean checkCampusForShift(Time startTime, TimeFrame colliding, Campus campus) {
        Time startDay = TimeUtils.getStartOfDay(startTime);
        Time noonOfApp = TimeUtils.copyDay(startTime, NOON);
        Time midNightOfApp = TimeUtils.copyDay(startTime, MIDNIGHT);
        Time startShift = startTime.compareTo(noonOfApp) < 0 ? startDay : noonOfApp;
        Time endShift = startTime.compareTo(noonOfApp) < 0 ? noonOfApp : midNightOfApp;
        Schedule sched = hasPreference.getSchedule();
        Appointment app = sched.getAppointmentAfter(startShift);
        while (app != null && app.getTimeFrame().getTime().compareTo(endShift) < 0) {
            if (!app.collides(colliding)) {
                if (!app.getCampus().equals(campus)) {
                    return false;
                }
            }
            app = sched.getAppointmentAfter(app.getTimeFrame().getEndTime());
        }
        return true;
    }

    public String getDescription() {
        return "StayShiftPreference\n"+
                "Stay on one campus during the shift\n"+
                "Only change in the morning, or at noon.";
    }
}
