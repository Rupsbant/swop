package Hospital.Schedules.ConstraintSolver;

import Hospital.Schedules.Schedulable;
import Hospital.World.Campus;
import Hospital.World.Time;
import java.util.List;

public class AppointmentResult {

    private List<Schedulable> attendees;
    private Campus campus;
    private Time chosenTime;
    private int length;

    AppointmentResult(List<Schedulable> attendees, Campus campus, Time chosenTime, int length) {
        this.attendees = attendees;
        this.campus = campus;
        this.chosenTime = chosenTime;
        this.length = length;
    }

    public List<Schedulable> getAttendees() {
        return attendees;
    }

    public Time getChosenTime() {
        return chosenTime;
    }

    public Campus getCampus() {
        return campus;
    }

    public int getLength() {
        return length;
    }
}
