package Hospital.Schedules;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.World.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * A schedule for keeping appointments
 */
public class Schedule {

    /**
     * the datastructure keeping the Appointment-objects
     */
    private TreeSet<Appointment> appointments;

    /**
     * Constructor
     */
    public Schedule() {
        appointments = new TreeSet<Appointment>();
    }

    /**
     * Adds an appointment to this schedule.
     * @param app The appointment to be scheduled.
     * @throws SchedulingException if the given appointment doesn't fit the schedule
     * @throws ArgumentIsNullException app was null
     */
    void addAppointment(Appointment app) throws SchedulingException, ArgumentIsNullException {
        if (app == null) {
            throw new ArgumentIsNullException("The appointment should not be null.");
        }
        if (!isFree(app)) {
            throw new SchedulingException("Schedule was not free anymore");
        }
        appointments.add(app);
    }

    /**
     * If the Appointment is in this schedule, remove it.
     * @param app The Appointment to be removed
     * @throws ArgumentIsNullException app was null
     */
    void removeAppointment(Appointment app) throws ArgumentIsNullException {
        if (app == null) {
            throw new ArgumentIsNullException("Can't remove null appointment");
        }
        appointments.remove(app);
    }

    /**
     * Outputs an appointment that fits in the schedule starting with the given appointment
     * @param tf the timeframe for which a free spot is to be found
     * @return a TimeFrame-object with the same length as tf and its starting time set to the first fitting time after the time in tf
     */
    public TimeFrame getNextFreeSpot(TimeFrame tf) {
        while (!isFree(tf)) { // pro forma , appointments are sorted, and iterated in order
            for (Appointment app : appointments) {
                if (app.collides(tf)) {
                    try {
                        tf = new TimeFrame(app.getTimeFrame().getEndTime(), tf.getLength());
                    } catch (ArgumentConstraintException ex) {
                        throw new Error("app.getLength() was negative, should not happen");
                    } catch (ArgumentIsNullException ex) {
                        throw new Error("app.getEndTime() was null, should not happen");
                    }
                }
            }
        }
        return tf;
    }

    /**
     * Checks whether there is free space to add the given appointment in this schedule
     * @param appointment the appointment to check
     * @return true if the appointment can be added without creating time-conflicts, false otherwise
     */
    boolean isFree(Appointment appointment) {
        return isFree(appointment.getTimeFrame());
    }

    /**
     * Checks whether there is free space to add an appointment with the given timeframe in this schedule
     * @param tf the timeframe to check
     * @return true if the timeframe can be added without creating time-conflicts, false otherwise
     */
    public boolean isFree(TimeFrame tf) {
        for (Appointment app : this.appointments) {
            if (app.collides(tf)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Transforms a List of Schedulables to a List of it's Schedules.
     * @param temp List of Schedulables to transform.
     * @return A List of Schedules .
     */
    public static List<Schedule> getSchedules(List<Schedulable> temp) {
        List<Schedule> out = new ArrayList<Schedule>();
        for (Schedulable n : temp) {
            out.add(n.getSchedule());
        }
        return out;
    }

    /**
     * This returns all appointments that are colliding with the given TimeFrame
     * @param tf , The TimeFrame to check the appointments against.
     * @return List of all appointments that collide
     */
    public List<Appointment> getCollidingAppointments(TimeFrame tf) {
        List<Appointment> out = new ArrayList<Appointment>();
        for (Appointment app : appointments) {
            if (app.collides(tf)) {
                out.add(app);
            }
        }
        return out;
    }

    /**
     * Returns the first appointment before the given time.
     * @param time, The time to start checking from
     * @return The first appointment before time.
     */
    public Appointment getAppointmentBefore(Time time) {
        Appointment max = null;
        //Can be optimalised with the invariants of the TreeSet
        for (Appointment app : appointments) {
            if (time.compareTo(app.getTimeFrame().getEndTime()) < 0) {
                continue;
            }
            if (max == null || app.compareTo(max) > 0) {
                max = app;
            }
        }
        return max;
    }

    /**
     * Returns the first appointment after the given time.
     * @param time, The time to start checking from
     * @return The first appointment after time.
     */
    public Appointment getAppointmentAfter(Time time) {
        Appointment max = null;
        //Can be optimalised with the invariants of the TreeSet
        for (Appointment app : appointments) {
            if (time.compareTo(app.getTimeFrame().getTime()) > 0) {
                continue;
            }
            if (max == null || app.compareTo(max) < 0) {
                max = app;
            }
        }
        return max;
    }
}