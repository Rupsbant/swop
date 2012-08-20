package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Argument.TimeArgument;
import Hospital.Exception.InvalidTimeException;
import Hospital.People.HospitalAdministrator;
import Hospital.World.Time;

/**
 * LoginController for the Hospital Administrator.
 * This enables the following usecase:
 * Advance Time.
 */
@SystemAPI
public class AdministratorController extends LoginController<HospitalAdministrator> {

    /**
     * When logging in this controller gets made from a StaffMember
     * @param user The staffmember to log in.
     * @param cc the Campus from where is logged in
     */
    @SystemAPI
    public AdministratorController(HospitalAdministrator user, CampusController cc) {
        super(user, cc);
    }

    /**
     * Advances the time in the world.
     * @param wc the world in which to advance the time
     * @param arg a TimeArgument with as answer the time to advance to
     * @throws InvalidTimeException the given time has already passed
     */
    @SystemAPI
    public void advanceTime(WorldController wc, TimeArgument arg) throws InvalidTimeException {
        Time time = arg.getAnswer();
        wc.getWorld().getWorldTime().setTime(time);
    }

    /**
     * Get the argument to enter the time.
     * @return the TimeArgument
     */
    @SystemAPI
    public TimeArgument getTimeArgument() {
        TimeArgument arg = new TimeArgument("Enter the new current time");
        return arg;
    }
}
