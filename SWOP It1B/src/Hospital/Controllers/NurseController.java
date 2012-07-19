package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Exception.Command.CannotDoException;
import java.util.ArrayList;
import java.util.List;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.PatientIsCheckedInException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Exception.Warehouse.NotEnoughItemsAvailableException;
import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.People.Nurse;
import Hospital.Schedules.AppointmentCommand;
import Hospital.Schedules.DoctorPatientAppointment;
import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.Schedules.ScheduleGroups.ScheduleGroup;
import Hospital.Schedules.ScheduleGroups.SingleSchedulableGroup;


/**
 * LoginController for Nurses
 */
@SystemAPI
public class NurseController extends LoginController<Nurse> {

    /**
     * Constructor
     * @param n the nurse which this controller will represent
     * @param cc the Campus from where is logged in
     */
    @SystemAPI
    public NurseController(Nurse n, CampusController cc) {
        super(n, cc);
    }

    /**
     * Gets a list of all Doctors in this world
     * @param wc the world in which to search
     * @return an array of strings containing the names of doctors
     */
    @SystemAPI
    public String[] getDoctors(WorldController wc) {
        List<Doctor> temp = wc.getWorld().getResourceOfClass(Doctor.class);
        String[] out = new String[temp.size()];
        int i = 0;
        for (Doctor d : temp) {
            out[i] = d.toString();
            i++;
        }
        return out;
    }

    /**
     * Checks in a patient and schedules an appoint for this patient with a doctor
     * @param chosenPatient the name of the Patient to check in
     * @param doctor the name of the Doctor for the appointment 
     * @param wc the world in which this all happens 
     * @return a description of the made appointment
     * @throws NoPersonWithNameAndRoleException the doctor and/or patient could not be found in the world
     * @throws ArgumentIsNullException the given world was null
     * @throws ArgumentConstraintException somehow the arguments given to the scheduler were null
     * @throws SchedulingException some the scheduler failed to schedule an appointment 
     * @throws PatientIsCheckedInException the patient was already checked in
     * @throws NotLoggedInException the nurse is not logged in
     * @throws NotEnoughItemsAvailableException Not enough food for 2 days.
     */
    @SystemAPI
    public String checkIn(String chosenPatient, String doctor, WorldController wc)
            throws NoPersonWithNameAndRoleException,
            ArgumentIsNullException,
            ArgumentConstraintException,
            SchedulingException,
            PatientIsCheckedInException,
            NotLoggedInException, NotEnoughItemsAvailableException {
        super.checkLoggedIn();
        if (wc == null) {
            throw new ArgumentIsNullException("The world Controller is null");
        }
        Patient patient = wc.getWorld().getPersonByName(Patient.class, chosenPatient);
        if (this.getCampusController().getFoodCapacityForPatients() < 1) {
            throw new NotEnoughItemsAvailableException();
        }
        try {
            patient.checkIn(this.getCampusController().getCampus());
        } catch (InvalidArgumentException e) {
            throw new Error("This never happens, getCampus never returns null.");
        }
        Doctor doc = wc.getWorld().getPersonByName(Doctor.class, doctor);
        DoctorPatientAppointment app = new DoctorPatientAppointment();
        List<ScheduleGroup> docPat = new ArrayList<ScheduleGroup>();
        docPat.add(new SingleSchedulableGroup(patient));
        docPat.add(new SingleSchedulableGroup(doc));

        Priority p = DoctorPatientAppointment.PRIORITY;
        AppointmentCommand appC = new AppointmentCommand(wc.getWorld(), app, docPat, p);
        try {
            return appC.execute();
        } catch (CannotDoException ex) {
            throw new Error("This is the first time it is executed, it should be done...");
        }
    }
}
