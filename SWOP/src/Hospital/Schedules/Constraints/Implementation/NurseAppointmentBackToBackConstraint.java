package Hospital.Schedules.Constraints.Implementation;

import Hospital.People.Nurse;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.Constraints.BasicTimeFrameConstraint;
import Hospital.Schedules.Constraints.GetCampusConstraint;
import Hospital.Schedules.Schedule;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;

public class NurseAppointmentBackToBackConstraint extends BasicTimeFrameConstraint implements GetCampusConstraint {

    private Campus campus;

    @Override
    protected void setValidNurse(TimeFrame tf, Nurse nurse) {
        this.campus = nurse.getCampus();
        if (tf.getTime().getMinute() == 0) {
            setValid(true);
            return;
        }
        Schedule schedule = nurse.getSchedule();
        Appointment prev = schedule.getAppointmentBefore(tf.getTime());
        if (prev == null) {
            setValid(false);
            return;
        }
        int timeDiff = prev.getTimeFrame().getEndTime().getMinutesDiff(tf.getTime());
        if (timeDiff == 0) {
            setValid(true);
        } else {
            setValid(false);
        }
    }

    @Override
    protected void resetValid() {
        super.resetValid();
        this.campus = null;
    }

    public Campus getCampus() {
        return this.campus;
    }
}
