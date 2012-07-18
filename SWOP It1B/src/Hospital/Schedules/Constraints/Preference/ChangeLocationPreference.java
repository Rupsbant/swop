package Hospital.Schedules.Constraints.Preference;

import Hospital.Schedules.Appointment;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;
import Hospital.World.Time;
import Hospital.World.TimeUtils;

public class ChangeLocationPreference implements Preference {

    public static final int MAX_CHANGES = 4;
    HasPreference schedulable;

    public ChangeLocationPreference(HasPreference schedulable) {
        this.schedulable = schedulable;
    }

    public void makeThisAsPreference(HasPreference d) {
        d.setPreference(new ChangeLocationPreference(d));
    }

    public boolean canAddAppointment(TimeFrame tf, Campus campus) {
        Time start = TimeUtils.getStartOfDay(tf.getTime());
        Time end = TimeUtils.addDay(start);
        Schedule sched = schedulable.getSchedule();
        Appointment appointmentAfter = sched.getAppointmentAfter(start);
        if (appointmentAfter == null) {
            return true;
        }
        Campus currentCampus = appointmentAfter.getCampus();
        start = appointmentAfter.getTimeFrame().getEndTime();
        int changeCount = getChangeCount(sched, start, end, tf, currentCampus);

        Appointment appointmentBefore = sched.getAppointmentBefore(tf.getTime());
        appointmentAfter = sched.getAppointmentAfter(tf.getEndTime());
        if(appointmentBefore != null && appointmentBefore.getTimeFrame().getEndTime().compareTo(TimeUtils.getStartOfDay(tf.getTime()))<=0){
            appointmentBefore = null;
        }
        if(appointmentAfter != null &&appointmentAfter.getTimeFrame().getTime().compareTo(end)>=0){
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

    private int getChangeCount(Schedule sched, Time start, Time end, TimeFrame tf, Campus currentCampus) {
        int changeCount = 0;
        while (true) {
            Appointment p = sched.getAppointmentAfter(start);
            if (p == null || p.getTimeFrame().getTime().compareTo(end) >= 0) {
                break;
            }
            if (p.collides(tf)) {
                start = p.getTimeFrame().getEndTime();
                continue;
            }
            Campus c = p.getCampus();
            if (!currentCampus.equals(c)) {
                changeCount++;
                currentCampus = c;
            }
            start = p.getTimeFrame().getEndTime();
        }
        return changeCount;
    }

    public String getDescription() {
        return "ChangeLocationPreference\n"
                + "Change at most four times of location during the day.";
    }
}
