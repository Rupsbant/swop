package Hospital.Schedules.Constraints.XRayConstraint;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.MedicalTest.XRayScan;
import Hospital.Patient.Patient;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.Utils;
import Hospital.World.Time;
import Hospital.World.TimeUtils;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * The constraint imposed on scheduling by X-ray scans
 */
public class XRayConstraint extends TimeFrameConstraint {

    /**
     * How many X-ray scans are planned to be done
     */
    private final int wantToDo;
    private Patient patient;
    private Time tf;

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
    public void setPatient(Patient p) {
        this.patient = p;
    }

    @Override
    public void setTime(Time tf, int length) {
        this.tf = tf;
    }

    @Override
    public void reset() {
        this.patient = null;
        this.tf = null;
    }

    @Override
    public Time isAccepted() {
        if (tf == null || patient == null) {
            return null;
        }
        Time start = TimeUtils.getStartOfDay(tf.getTime());
        start = TimeUtils.getLastYear(start);
        PriorityQueue<AppEvent> events = getBasicEvents(start);
        TreeMap<Integer, Integer> counter = new TreeMap<Integer, Integer>();
        int countPlanned = 0;
        
        Time unlockTime = TimeUtils.getNextYear(tf.getTime()).getLaterTime(-1);
        events.add(new AppNothingEvent(unlockTime));
        counter.put(0, 1);

        while (!events.isEmpty()) {
            AppEvent e = events.poll();
            countPlanned = e.doEvent(countPlanned, counter, events);
            if (e.compareTo(unlockTime) >= 0) {
                Integer maximum = counter.lastKey();
                if (wantToDo < XRayScan.MAX_XRAY_COUNT - maximum) {
                    try {
                        Time time = e.getTime();
                        time = TimeUtils.getLastYear(time).getLaterTime(1);
                        return time;
                    } catch (Exception ex) {
                        throw new Error("this cannot happen");
                    }
                }
            }
        }
        return null;
    }

    private PriorityQueue<AppEvent> getBasicEvents(Time start) {
        List<XRayScan> filter = Utils.filter(patient.getMedicalTests(), XRayScan.class);
        PriorityQueue<AppEvent> events = new PriorityQueue<AppEvent>();
        for (XRayScan xray : filter) {
            if (xray.getAppointment() == null) {
                continue;
            }
            if (xray.getAppointment().compareTo(start) < 0) {
                continue;
            }
            Time appStart = xray.getAppointment().getTime();
            appStart = TimeUtils.getStartOfDay(appStart);
            events.add(new AppStartEvent(appStart, xray.getXRayCount()));
        }
        return events;
    }
}
