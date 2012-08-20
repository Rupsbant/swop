package Hospital.Controllers;

import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Patient.Patient;
import Hospital.SystemAPI;

/**
 * Used for patient-related actions that are handled by a nurse
 * This controller enables the following usecases:
 * Register patient: register the patient
 */
@SystemAPI
public class PatientController {
	/**
	 * the world in which these actions take place
	 */
    private WorldController wc;
    /**
     * the nurse performing these actions
     */
    private NurseController nc;

    /**
     * Creates a patientController to register a new Patient.
     * @param wc The WorldController to add the patient to.
     * @param nc The NurseController that adds the patient.
     * @throws ArgumentIsNullException wc or ac was null.
     */
    @SystemAPI
    public PatientController(WorldController wc, NurseController nc) throws ArgumentIsNullException {
        setWorldController(wc);
        setNurseController(nc);
    }

    /**
     * Sets the NurseController to be used with this object 
     * @param nc a NurseController
     * @throws ArgumentIsNullException nc was null
     */
    private void setNurseController(NurseController nc) throws ArgumentIsNullException {
        if(nc == null){
            throw new ArgumentIsNullException("NurseController should not be null");
        }
        this.nc = nc;
    }

    /**
     * Sets the WorldController to be used with this object 
     * @param wc a WorldController
     * @throws ArgumentIsNullException wc was null
     */
    private void setWorldController(WorldController wc) throws ArgumentIsNullException {
        if(wc == null){
            throw new ArgumentIsNullException("WorldController should not be null");
        }
        this.wc = wc;
    }

    /**
     * This creates a new Patient and adds it to the hospital.
     * @param name the name of the patient
     * @return a description of the new patient
     * @throws NotLoggedInException If the nurse was logged out.
     * @throws SchedulableAlreadyExistsException If a patient with the same name already existed.
     * @throws InvalidArgumentException if name was null
     */
    @SystemAPI
    public String registerPatient(String name)
            throws NotLoggedInException, SchedulableAlreadyExistsException, InvalidArgumentException {
        nc.checkLoggedIn();
        Patient newPatient = new Patient(name);
        wc.getWorld().addSchedulable(newPatient);
        return newPatient.toString();
    }
}
