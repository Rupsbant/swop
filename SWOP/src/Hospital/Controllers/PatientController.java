package Hospital.Controllers;

import Hospital.Argument.Argument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Patient.Patient;
import Hospital.Patient.PatientFactory;
import Hospital.SystemAPI;
import Hospital.Utils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used for patient-related actions that are handled by a nurse
 */
@SystemAPI
public class PatientController {
	/**
	 * the world in which these actions take place
	 */
    WorldController wc;
    /**
     * the nurse performing these actions
     */
    NurseController nc;

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
     * @param factoryName The factoryName to add a person with.
     * @param args The name of the patient in a StringArgument.
     * @return The name of the patient that was made.
     * @throws NotLoggedInException If the nurse was logged out.
     * @throws NotAFactoryException If the factory's name was not found.
     * @throws SchedulableAlreadyExistsException If the patient already existed.
     * @throws InvalidArgumentException If the ArgumentList was of the wrong size, null, not answered or of the wrong type
     */
    @SystemAPI
    public String registerPatient(String factoryName, ArgumentList args)
            throws NotLoggedInException, NotAFactoryException, SchedulableAlreadyExistsException, InvalidArgumentException {
        nc.checkLoggedIn();
        if(args == null){
            throw new ArgumentIsNullException("ArgumentList was null");
        }
        PatientFactory factory = wc.getWorld().getFactory(PatientFactory.class, factoryName);
        Patient newPatient = (Patient) factory.make(args.getAllArguments());
        try {
            wc.getWorld().getPersonByName(Patient.class, newPatient.getName());
            throw new SchedulableAlreadyExistsException();
        } catch (NoPersonWithNameAndRoleException e) {
            wc.getWorld().addSchedulable(newPatient);
            return newPatient.toString();
        }
    }

    /**
     * Returns the names of all factories that makes patients.
     * @return Array of names of the factories.
     * @throws NotLoggedInException If the nurse was logged out.
     */
    @SystemAPI
    public String[] getAvailablePatientFactories() throws NotLoggedInException {
        nc.checkLoggedIn();
        try {
            return wc.getAvailableFactories(PatientFactory.class).toArray(new String[0]);
        } catch (ArgumentIsNullException ex) {
            throw new Error("Class is not null");
        }
    }

    /**
     * Returns the arguments of the chosen factory that makes patients.
     * @param factoryName The name of the factory to search the arguments of.
     * @return The arguments.
     * @throws NotLoggedInException If the nurse was logged out.
     * @throws NotAFactoryException If the factory was not found.
     */
    @SystemAPI
    public ArgumentList getFactoryArguments(String factoryName) throws NotLoggedInException, NotAFactoryException {
        nc.checkLoggedIn();
        try {
            Argument[] args = wc.getWorld().getFactory(PatientFactory.class, factoryName).getEmptyArgumentList();
            return new ArgumentList(args);
        } catch (ArgumentIsNullException ex) {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error("Invalid state, args should not be null, class is not null");
        }
    }
}
