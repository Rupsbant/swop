package Hospital.Schedules;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Scheduling.SchedulingException;

public class ScheduleTestUtil {
    public static void addAppointment(Schedule schedule, Appointment app) throws SchedulingException, ArgumentIsNullException{
        schedule.addAppointment(app);
    }

}
