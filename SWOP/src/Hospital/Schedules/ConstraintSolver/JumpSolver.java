package Hospital.Schedules.ConstraintSolver;

import Hospital.Exception.Scheduling.ScheduleConstraintException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Schedules.CampusDecider;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.DelayedTimeLength;
import Hospital.Schedules.TimeFrameConstraint;
import Hospital.World.Campus;
import Hospital.World.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * A AppointmentConstraintSolver that effeciently determines a Appointment
 */
public class JumpSolver implements AppointmentConstraintSolver {

    private DelayedTimeLength startTime;
    private CampusDecider campusDecider;
    private List<ScheduleGroup> groups;
    private List<TimeFrameConstraint> tfConstraints;

    /*--------*\
    |* Output *|
    \*--------*/
    private List<Schedulable> output_list;
    private Time output_time;
    private Campus output_campus;

    protected JumpSolver() {
    }

    public void solve() throws SchedulingException {
        reset();
        List<Schedulable> testing = new ArrayList<Schedulable>();
        recursive(0, testing);
    }

    private void recursive(int pos, List<Schedulable> building) throws SchedulingException {
        if (pos == groups.size()) {
            stopConditions(building);
            return;
        }
        ScheduleGroup chooseSchedule = groups.get(pos);
        for (Schedulable sched : chooseSchedule.getSchedulables()) {
            building.add(pos, sched);
            recursive(pos + 1, building);
            building.remove(pos);
        }
    }

    private void stopConditions(List<Schedulable> built) throws SchedulingException {
        //reset previous
        for (TimeFrameConstraint tfC : tfConstraints) {
            tfC.reset();
        }
        //initialize
        List<TimeFrameConstraint> allConstraints = makeAllConstraints(built);
        Campus testCampus = makeCampus(built);
        Time currentTesting = startTime.getDelayedTime();

        //visit constraints
        for (TimeFrameConstraint tfC : allConstraints) {
            tfC.setCampus(testCampus);
            for (Schedulable sched : built) {
                sched.visitConstraint(tfC);
            }
        }

        int size = allConstraints.size();
        int finalTest = 0;
        int position = 0;
        do {
            //Go to the next position, if at the end of the list, restart from first
            position = (position + 1) % size;
            TimeFrameConstraint tfC = allConstraints.get(position);
            tfC.setTime(currentTesting, startTime.getLength());
            try {
                Time outputTimeFrame = tfC.isAccepted();
                if (outputTimeFrame == null) {
                    throw new SchedulingException("Something went very wrong, information was missing" + tfC);
                } else if (!outputTimeFrame.equals(currentTesting)) {
                    // the wrong outputTimeFrame was found, 
                    // Do all TimeFrameConstraints again, starting with the next one
                    currentTesting = outputTimeFrame;
                    finalTest = position;
                }
            } catch (ScheduleConstraintException ex) {
                return;
            }
        } while (finalTest != position);

        if (output_time == null || output_time.compareTo(currentTesting) > 0) {
            output_time = currentTesting;
            output_campus = testCampus;
            output_list = new ArrayList(built);
        }
    }

    private List<TimeFrameConstraint> makeAllConstraints(List<Schedulable> built) {
        //get constraints
        List<TimeFrameConstraint> allConstraints = new ArrayList<TimeFrameConstraint>();
        for (Schedulable sched : built) {
            allConstraints.addAll(sched.getConstraints());
        }
        allConstraints.addAll(tfConstraints);
        return allConstraints;
    }

    private Campus makeCampus(List<Schedulable> built) {
        //decide campus
        for (Schedulable sched : built) {
            sched.visitConstraint(campusDecider);
        }
        return campusDecider.getCampus();
    }

    public Campus getCampus() {
        return this.output_campus;
    }

    public List<Schedulable> getAttendees() {
        return this.output_list;
    }

    public Time getChosenTime() {
        return output_time;
    }

    public void setDelayedTimeLength(DelayedTimeLength tf) {
        this.startTime = tf;
    }

    public void setScheduleGroups(List<ScheduleGroup> list) {
        groups = list;
    }

    public void setConstaints(List<TimeFrameConstraint> tfConstraints) {
        this.tfConstraints = tfConstraints;
    }

    public void setCampusDecider(CampusDecider campusDecider) {
        this.campusDecider = campusDecider;
    }

    public void reset() {
        this.output_time = null;
        this.output_list = new ArrayList<Schedulable>();
        this.output_campus = null;
    }
}
