package Hospital.Schedules;

import Hospital.Schedules.ConstraintSolver.AppointmentResult;
import Hospital.World.HasTime;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.World.Campus;
import Hospital.World.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * An appointment between multiple Scheduleable objects
 */
public class Appointment implements HasTime {

    /**
     * Returns true if the length is more than 0 minutes.
     * @param length The appointmentLength to test.
     * @return length >=0
     */
    public static boolean isValidLength(int length) {
        return length >= 0;
    }
    /**
     * the schedules of the attendees to this appointment
     */
    private List<Schedule> attendees = new ArrayList<Schedule>();
    /**
     * indicates whether this appointment has been scheduled
     */
    private boolean scheduled;
    /**
     * this provides a backlink to the appointmentCommand for preemptive scheduling
     */
    private AppointmentCommand appCommand;
    /**
     * the campus where the attendees will meet
     */
    private Campus campus;
    /**
     * the starting time
     */
    private final Time time;
    /**
     * the length in minutes
     */
    private final int length;
    /**
     * the priority of the appointment
     */
    private Priority priority;

    /**
     * Constructor
     * @param timeFrame the starting time and length of the appointment as TimeFrame object
     * @param attendees the schedules of the attendees
     */
    public Appointment(Time startTime, int length, List<Schedule> attendees, AppointmentCommand appC, Campus campus, Priority priority) {
        this.campus = campus;
        this.appCommand = appC;
        this.time = startTime;
        this.length = length;
        this.priority = priority;
        try {
            this.addAttendees(attendees);
        } catch (SchedulingException ex) {
            throw new Error("Appointment is not scheduled on creation, exception can't be thrown");
        }
    }

    Appointment(AppointmentResult result, AppointmentCommand appCommand, Priority priority) {
        this.campus = result.getCampus();
        this.length = result.getLength();
        this.time = result.getChosenTime();
        this.appCommand = appCommand;
        this.priority = priority;
        try {
            addAttendees(Schedule.getSchedules(result.getAttendees()));
        } catch (SchedulingException ex) {
            throw new Error("Appointment is not scheduled on creation, exception can't be thrown");
        }
    }

    /**
     * Returns the Time this appointment starts
     * @return The Time this appointment starts
     */
    public Time getTime() {
        return this.time;
    }

    /**
     * Returns true if the two TimeFrame falls during the same time.
     * @param tf Other TimeFrame to test.
     * @return true if TimeFrames collide.
     */
    public boolean collides(Appointment tf) {
        return (getTime().compareTo(tf.getTime()) <= 0 && tf.getTime().compareTo(getEndTime()) < 0) || (tf.getTime().compareTo(getTime()) <= 0 && getTime().compareTo(tf.getEndTime()) < 0);
    }

    /**
     * Returns true if the two TimeFrame falls during the same time.
     * @param tf Other TimeFrame to test.
     * @return true if TimeFrames collide.
     */
    public boolean collides(Time tf, int length) {
        Time endTime = tf.getLaterTime(length);
        return (getTime().compareTo(tf.getTime()) <= 0
                && tf.getTime().compareTo(getEndTime()) < 0)
                || (tf.getTime().compareTo(getTime()) <= 0
                && getTime().compareTo(endTime) < 0);
    }

    /**
     * Compares the HasTime with the TimeFrame of this appointment
     * @param o The appointment to check.
     * @return  < 0 : if the given HasTime falls after the Appointment. <br>
     *          == 0: if they start at the same moment. <br>
     *          > 0 : if the given HasTime falls before the Appointment.
     */
    public int compareTo(HasTime o) {
        return getTime().compareTo(o.getTime());
    }

    /**
     * Gives a description of the Appointment.
     * @return a description of this Appointment including: startTime, length, endTime, and number of attendees.
     */
    @Override
    public String toString() {
        return "Appointment from " + getTime()
                + " of " + getLength() + " minutes, "
                + "until " + getEndTime()
                + " with " + attendees.size() + " attendees.";
    }

    /**
     * Returns whether this Appointment is scheduled.
     * @return true if the Appointment is added to the Schedules.
     */
    public boolean isScheduled() {
        return scheduled;
    }

    /**
     * Schedules the appointment.
     * @throws SchedulingException If the appointment was already scheduled, or if one of the schedules aborted.
     */
    public void schedule() throws SchedulingException {
        if (scheduled) {
            throw new SchedulingException("Already scheduled");
        }
        try {
            for (Schedule sched : attendees) {
                try {
                    sched.addAppointment(this);
                } catch (ArgumentIsNullException ex) {
                    throw new Error("This appointment is not null, java is WRONG!");
                }
            }
            scheduled = true;
        } catch (SchedulingException ex) { //Undo adding
            removeAttendees();
            throw ex;
        }
    }

    /**
     * Removes this Appointment from the schedules. It can't be reScheduled but must be remade.
     */
    public void remove() {
        removeAttendees();
        scheduled = true; //cannot reschedule
    }

    private void removeAttendees() {
        for (Schedule sched : attendees) {
            try {
                sched.removeAppointment(this);
            } catch (ArgumentIsNullException ex1) {
                throw new Error("This appointment is not null, java is WRONG!");
            }
        }
    }

    /**
     * Adds attendees to this appointment
     * @param attendees the schedules of the attendees to add
     * @throws SchedulingException one of the attendees has schedule that conflicts with this appointment,
     *                             remove() is called right before this exception is thrown
     */
    private void addAttendees(List<Schedule> attendees) throws SchedulingException {
        if (attendees == null) {
            return;
        }
        this.attendees.addAll(attendees);
        if (scheduled) {
            for (Schedule sched : attendees) {
                try {
                    sched.addAppointment(this);
                } catch (SchedulingException ex) {
                    remove();
                    throw ex;
                } catch (ArgumentIsNullException ex) {
                    throw new Error("this can't happen.");
                }
            }
        }
    }

    public AppointmentCommand getAppCommand() {
        return this.appCommand;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public Campus getCampus() {
        return this.campus;
    }

    List<Schedule> getAttendees() {
        return this.attendees;
    }

    /**
     * Return the length of the TimeFrame.
     * @return length(minutes);
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the endTime of the TimeFrame.
     * @return endTime.
     */
    public Time getEndTime() {
        return getTime().getLaterTime(length);
    }
}
