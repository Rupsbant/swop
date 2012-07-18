package Hospital.Schedules;

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
     * the starting time and length of this appointment
     */
    private TimeFrame timeFrame;
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
     * Constructor
     * @param timeFrame the starting time and length of the appointment as TimeFrame object
     * @param attendees the schedules of the attendees
     */
    public Appointment(TimeFrame timeFrame, List<Schedule> attendees, AppointmentCommand appC, Campus campus) {
        this.campus = campus;
        this.appCommand = appC;
        this.timeFrame = timeFrame;
        try {
            this.addAttendees(attendees);
        } catch (SchedulingException ex) {
            throw new RuntimeException("Appointment is not scheduled on creation, exception can't be thrown");
        }
    }

    /**
     * Returns the TimeFrame for which this Appointment is scheduled.
     * @return the scheduled TimeFrame
     */
    public TimeFrame getTimeFrame() {
        return this.timeFrame;
    }

    /**
     * Returns the Time this appointment starts
     * @return The Time this appointment starts
     */
    public Time getTime(){
        return this.timeFrame.getTime();
    }

    /**
     * Returns true is the two Appointment's TimeFrames collide.
     * @param o Appointment
     * @return true is o collides with this, false otherwise.
     */
    public boolean collides(Appointment o) {
        return collides(o.timeFrame);
    }

    /**
     * Returns true if the Appointment collides with the given TimeFrame.
     * @param tf The TimeFrame to check.
     * @return true if the Appointment's TimeFrame and tf collide.
     */
    public boolean collides(TimeFrame tf) {
        return timeFrame.collides(tf);
    }

    /**
     * Compares the HasTime with the TimeFrame of this appointment
     * @param o The appointment to check.
     * @return  < 0 : if the given HasTime falls after the Appointment. <br>
     *          == 0: if they start at the same moment. <br>
     *          > 0 : if the given HasTime falls before the Appointment.
     */
    public int compareTo(HasTime o) {
        return timeFrame.compareTo(o.getTime());
    }

    /**
     * Gives a description of the Appointment.
     * @return a description of this Appointment including: startTime, length, endTime, and number of attendees.
     */
    @Override
    public String toString() {
        return "Appointment from " + timeFrame.getTime()
                + " of " + timeFrame.getLength() + " minutes, "
                + "until " + timeFrame.getEndTime()
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
                    throw new RuntimeException("This appointment is not null, java is WRONG!");
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
                throw new RuntimeException("This appointment is not null, java is WRONG!");
            }
        }
    }

    /**
     * Adds an attendee to the Appointment. If a exception is thrown a new Appointment must be made.
     * @param sched Schedule to add.
     * @throws SchedulingException If given Schedule can't be added.
     */
    public void addAttendee(Schedule sched) throws SchedulingException {
        attendees.add(sched);
        if (scheduled) {
            try {
                sched.addAppointment(this);
            } catch (SchedulingException ex) {
                remove();
                throw ex;
            } catch (ArgumentIsNullException ex) {
                throw new RuntimeException("this can't happen.");
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
                    throw new RuntimeException("this can't happen.");
                }
            }
        }
    }

    public AppointmentCommand getAppCommand() {
        return this.appCommand;
    }

    public Priority getPriority() {
        return this.appCommand.getPriority();
    }

    public Campus getCampus() {
        return this.campus;
    }

    List<Schedule> getAttendees() {
        return this.attendees;
    }
}
