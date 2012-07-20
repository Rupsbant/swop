package Hospital.Schedules.ConstraintSolver;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Schedules.Constraints.GetCampusConstraint;
import Hospital.Schedules.Constraints.Implementation.NullConstraint;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.Schedules.Schedulable;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;
import Hospital.World.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Solver implements AppointmentConstraintSolver {

    //TODO: make solver using AI: backjumping or tables
    //Will require serious rewrite of Constraints
    //TODO: make solver faster with failfast.
    //Needs constraints on the constraints ;)
    //TODO: Make Constraints return the first possible time.
    //Contract of workings of the Constraints
    private TimeFrame tf;
    private TimeFrame chosenTimeFrame;
    private List<ScheduleGroup> groups;
    private List<TimeFrameConstraint> tfConstraints;
    private List<Schedulable> out;
    private Campus campus;

    public Solver solve() throws SchedulingException {
        if (out.isEmpty()) {
            chosenTimeFrame = tf;
            while (!recursive(0, chosenTimeFrame)) {
                chosenTimeFrame = getNextTimeFrame(chosenTimeFrame);
            }
        }
        return this;
    }

    private boolean stopConditions(TimeFrame tf) throws SchedulingException {
        for(TimeFrameConstraint tfC : tfConstraints){
            tfC.reset();
        }
        List<TimeFrameConstraint> allConstraints = new ArrayList<TimeFrameConstraint>();
        for (Schedulable sched : out) {
            allConstraints.addAll(sched.getConstraints());
        }
        allConstraints.addAll(tfConstraints);
        for(TimeFrameConstraint tfC : allConstraints){
            for (Schedulable sched : out) {
                sched.visitConstraint(tfC);
            }
            tfC.setTimeFrame(tf);
            Boolean accepted = tfC.isAccepted();
            if(accepted == null){
                throw new SchedulingException("Something went very wrong, information was missing");
            } else if(!accepted){
                return false;
            }
        }
        this.campus = ((GetCampusConstraint) tfConstraints.get(0)).getCampus();
        return true;
    }

    private boolean recursive(int pos, TimeFrame tf) throws SchedulingException {
        if (pos == groups.size()) {
            return stopConditions(tf);
        }
        ScheduleGroup chooseSchedule = groups.get(pos);
        for (Schedulable sched : chooseSchedule.getSchedulables()) {
            out.add(pos, sched);
            if (recursive(pos + 1, tf)) {
                return true;
            }
            out.remove(pos);
        }
        return false;
    }

    private TimeFrame getNextTimeFrame(TimeFrame tf) {
        try {
            Time nextTime = tf.getTime().getLaterTime(1);
            tf = new TimeFrame(nextTime, tf.getLength());
        } catch (ArgumentIsNullException ex) {
            throw new Error(ex);
        } catch (ArgumentConstraintException ex) {
            throw new Error(ex);
        }
        return tf;
    }

    public Campus getCampus() {
        System.out.println("Appointment at: "+this.campus);
        return this.campus;
    }

    public List<Schedulable> getAttendees() {
        return this.out;
    }

    public TimeFrame getChosenTimeFrame() {
        return chosenTimeFrame;
    }

    public void setFirstTimeFrame(TimeFrame tf) {
        reset();
        this.tf = tf;
    }

    public void setScheduleGroups(List<ScheduleGroup> list) {
        reset();
        groups = list;
    }

    public void setConstaints(List<TimeFrameConstraint> tfConstraints) {
        reset();
        this.tfConstraints = tfConstraints;
    }

    public void reset() {
        this.out = new ArrayList<Schedulable>();
        this.campus = null;
    }
}
