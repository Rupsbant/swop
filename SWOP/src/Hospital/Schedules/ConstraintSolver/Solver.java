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
    //Contract of workings of the Constraints
    private TimeFrame tf;
    private TimeFrame chosenTimeFrame;
    private List<ScheduleGroup> groups;
    private TimeFrameConstraint tfConstraints;
    private List<Schedulable> out;
    private Campus campus;

    public void setFirstTimeFrame(TimeFrame tf) {
        reset();
        this.tf = tf;
    }

    public void setScheduleGroups(List<ScheduleGroup> list) {
        reset();
        groups = list;
    }

    public void setConstaints(TimeFrameConstraint tfConstraints) {
        reset();
        this.tfConstraints = tfConstraints;
    }

    public void reset() {
        this.out = new ArrayList<Schedulable>();
        this.campus = null;
    }

    public Solver solve() throws SchedulingException {
        if (out.isEmpty()) {
            chosenTimeFrame = tf;
            while (!recursive(0, chosenTimeFrame)) {
                chosenTimeFrame = getNextTimeFrame(chosenTimeFrame);
            }
        }
        return this;
    }

    private boolean stopConditions(TimeFrame tf) {
        tfConstraints.resetAll();
        TimeFrameConstraint allConstraints = new NullConstraint();
        for (Schedulable sched : out) {
            allConstraints.addConstraintList(sched.getConstraints());
        }
        allConstraints.addConstraintList(tfConstraints);
        for (Schedulable sched : out) {
            allConstraints.setTimeFrame(tf);
            sched.setValidTimeFrame(allConstraints);
        }
        Boolean accepted = allConstraints.acceptAll();
        if (accepted != null && accepted) {
            this.campus = ((GetCampusConstraint) tfConstraints).getCampus();
            return true;
        } else {
            return false;
        }
    }

    private boolean recursive(int pos, TimeFrame tf) {
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
            Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArgumentConstraintException ex) {
            Logger.getLogger(Solver.class.getName()).log(Level.SEVERE, null, ex);
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
}
