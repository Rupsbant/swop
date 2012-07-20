package Hospital.Schedules.Constraints.Implementation;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.MedicalTest.XRayScan;
import Hospital.Patient.Patient;
import Hospital.Schedules.Constraints.TimeFrameConstraintImplementation;
import Hospital.Schedules.TimeFrame;
import Hospital.Utils;
import Hospital.World.Time;
import Hospital.World.TimeUtils;
import java.util.List;
import java.util.PriorityQueue;

/**
 * The constraint imposed on scheduling by X-ray scans
 */
public class XRayConstraint extends TimeFrameConstraintImplementation {

    /**
     * How many X-ray scans are planned to be done
     */
    private final int wantToDo;
    private Patient patient;
    private TimeFrame tf;

    /**
     * Constructor
     * @param wantToDo how many X-ray scans are planned to be done
     * @throws ArgumentConstraintException wantToDo was negative
     */
    public XRayConstraint(int wantToDo) throws ArgumentConstraintException {
        if (wantToDo < 0) {
            throw new ArgumentConstraintException("Can't check for a negative number of XRays");
        }
        this.wantToDo = wantToDo;
    }

    /**
     * (in)validates the constraint based on a given TimeFrame for a Patient.<br />
     * Will result in invalidation if scheduling the in the constructor given amount of X-ray scans at the given timeframe would
     * result in the patient getting more than MAX_XRAY_COUNT scans in the span of a year
     * @see Hospital.Schedules.Constraints.TimeFrameConstraint#setValidPatient(Hospital.Schedules.TimeFrame, Hospital.Patient.Patient)
     */
    @Override
    protected void setValidPatient(Patient p) {
        this.patient = p;
    }

    @Override
    protected void setValidTimeFrame(TimeFrame tf) {
        this.tf = tf;
    }

    @Override
    protected void reset() {
        this.patient = null;
        this.tf = null;
    }

    @Override
    protected Boolean isAccepted() {
        if (tf == null || patient == null) {
            return null;
        }
        List<XRayScan> filter = Utils.filter(patient.getMedicalTests(), XRayScan.class);
        Time start = TimeUtils.getStartOfDay(tf.getTime());
        Time end = TimeUtils.getNextYear(start).getDiffTime(0, 0, 0, 0, -1);
        start = TimeUtils.getLastYear(start);

        PriorityQueue<Event> events = new PriorityQueue<Event>();
        for (XRayScan xray : filter) {
            if (xray.getAppointment() == null) {
                continue;
            }
            if (xray.getAppointment().compareTo(start) < 0) {
                continue;
            }
            if (xray.getAppointment().compareTo(end) >= 0) {
                continue;
            }
            Time appStart = TimeUtils.getStartOfDay(xray.getAppointment().getTimeFrame().getTime());
            Time appOver = TimeUtils.getNextYear(xray.getAppointment().getTimeFrame().getEndTime());
            appOver = TimeUtils.getStartOfDay(appOver).getDiffTime(0, 0, 0, 23, 59);

            events.add(new Event(appStart, xray.getXRayCount()));
            events.add(new Event(appOver, -xray.getXRayCount()));
        }
        events.add(new Event(tf.getTime(), wantToDo));
        events.add(new Event(TimeUtils.getNextYear(tf.getTime()), -wantToDo));
        int count = 0;
        while (!events.isEmpty()) {
            Event e = events.poll();
            count = e.newValue(count);
            if (count > XRayScan.MAX_XRAY_COUNT) {
                return false;
            }
        }
        return true;
    }

    private class Event implements Comparable<Event> {

        private int change;
        private Time time;

        public Event(Time time, int change) {
            this.change = change;
            this.time = time;
        }

        public int compareTo(Event o) {
            return time.compareTo(o.time);
        }

        int newValue(int old) {
            return change + old;
        }
    }
}