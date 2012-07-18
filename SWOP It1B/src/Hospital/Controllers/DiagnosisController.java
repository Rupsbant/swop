//TODO: catch every error with "somehow" in its documentation
package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Patient.DiagnosisInfo;
import Hospital.Argument.Argument;
import Hospital.Argument.DoctorArgument;
import Hospital.Argument.StringArgument;
import Hospital.Exception.*;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Patient.Diagnosis;
import Hospital.Patient.DiagnosisApproveCommand;
import Hospital.Patient.DiagnosisCommand;
import Hospital.Patient.DiagnosisFactory;
import Hospital.Patient.DiagnosisSecondOpinion;
import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.People.LoginInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The DiagnosisController is used by DoctorControllers to perform
 * diagnosis-related actions, including:
 * <ul>
 * <li>Creating diagnoses</li>
 * <li>Approving/disapproving diagnoses</li>
 * <li>Determining the type of a diagnosis</li>
 * <li>Finding available doctors to ask a second opinion from</li>
 * </ul>
 * To create a DiagnosisController, the LoginController of a logged in Doctor is required. 
 */
@SystemAPI
public class DiagnosisController {

    /**
     * The WorldController of the world in which to perform these actions
     */
    private WorldController wc;
    /**
     * The DoctorController of the doctor performing these actions
     */
    private DoctorController dc;

    /**
     * Constructs a DiagnosisController
     * @param wc the WorldController of the world this controller is relevant to
     * @param dc the doctor using this controller
     */
    @SystemAPI
    public DiagnosisController(WorldController wc, DoctorController dc) {
        this.wc = wc;
        this.dc = dc;
    }

    /**
     * This creates a diagnosis in the system with the given Arguments
     * @param factoryName The type of diagnosis to be created
     * @param args The arguments needed to instantiate the diagnosis
     * @param secondOpinion the LoginInfo of the doctor to ask a second opinion from
     * @return The details of the created diagnosis
     * @throws NoPersonWithNameAndRoleException The name of the second opinion doctor was not found in the world
     * @throws NoOpenedPatientFileException the doctor currently has no patientfile opened to enter this diagnosis in
     * @throws PatientIsDischargedException the patient for which we're entering this diagnosis is already discharged
     * @throws NotLoggedInException the doctor is not logged in
     * @throws NotAFactoryException the given factoryName did not lead to an existing type of diagnosis
     * @throws WrongArgumentListException the given list of arguments was invalid
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    @SystemAPI
    public String enterDiagnosis(String factoryName, ArgumentList args, LoginInfo secondOpinion)
            throws InvalidArgumentException,
            NoPersonWithNameAndRoleException,
            NoOpenedPatientFileException,
            PatientIsDischargedException,
            NotLoggedInException,
            NotAFactoryException,
            WrongArgumentListException {
        dc.checkLoggedIn();
        Doctor secondDoc = null;
        if (secondOpinion != null) {
            if (!secondOpinion.getRole().equals("Doctor")) {
                throw new NoPersonWithNameAndRoleException();
            }
            secondDoc = wc.getWorld().getPersonByName(Doctor.class, secondOpinion.getName());
        }
        DiagnosisCommand diaC = new DiagnosisCommand(wc.getWorld(), dc.getUser(), factoryName, secondDoc, args.getAllArguments());
        dc.addCommand(diaC);
        try {
            return diaC.execute();
        } catch (CannotDoException ex) {
            Logger.getLogger(DiagnosisController.class.getName()).log(Level.SEVERE, "Execute should work!", ex);
            return "Error";
        }
    }

    /**
     * Used to approve a diagnosis to which a second opinion was asked
     * @param diag the DiagnosisInfo of the diagnosis to be approved
     * @return details about the diagnosis
     * @throws PatientIsDischargedException the patient the doctor is working on is already discharged
     * @throws NoOpenedPatientFileException the doctor has no patientfile opened
     * @throws NotLoggedInException the doctor is not logged in
     * @throws InvalidDiagnosisException the diagnosis could not be found
     * @throws CannotDoException this command could somehow not be executed, even though it was just created
     */
    @SystemAPI
    public String approveDiagnosis(DiagnosisInfo diag)
            throws PatientIsDischargedException,
            NoOpenedPatientFileException,
            NotLoggedInException,
            InvalidDiagnosisException,
            CannotDoException {
        dc.checkLoggedIn();
        if (dc.getUser().getOpenedPatient().isDischarged()) {
            throw new PatientIsDischargedException();
        }
        List<Patient> patientList = wc.getWorld().getResourceOfClass(Patient.class);
        DiagnosisSecondOpinion diagsec = null;
        for (Patient p : patientList) {
            try {
                diagsec = ((DiagnosisSecondOpinion) p.isValidDiagnosisInfo(diag));
            } catch (InvalidDiagnosisException ex) {
                //try next patient.
            }
        }
        if (diagsec == null) {
            throw new InvalidDiagnosisException("Diagnosis not found in all patients");
        }
        DiagnosisApproveCommand diaAppC = new DiagnosisApproveCommand(diagsec, dc.getUser());
        dc.addCommand(diaAppC);
        return diaAppC.execute();
    }

    /**
     * Disapproves a diagnosis
     * @param diag the diagnosis to disapprove
     * @param details no use
     * @return the details of the new diagnosis made to correct the original
     * @throws CannotChangeException if this happens, a newly created argument was somehow already answered
     * @throws NoOpenedPatientFileException the doctor has no patientfile opened
     * @throws NotLoggedInException the doctor is not logged in
     * @throws InvalidDiagnosisException the diagnosis could not be found
     * @throws WrongArgumentListException the arguments to the corrected diagnosis somehow became invalid
     * @throws NoPersonWithNameAndRoleException somehow something went wrong in finding the original doctor
     * @throws NotAFactoryException somehow the "Diagnosis with secondOpinion"-factory disappeared from the world
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    @SystemAPI
    public String disapproveDiagnosis(DiagnosisInfo diag, String details)
            throws CannotChangeException,
            WrongArgumentListException,
            NoPersonWithNameAndRoleException,
            NoOpenedPatientFileException,
            InvalidDiagnosisException,
            NotLoggedInException,
            NotAFactoryException,
            InvalidArgumentException {
        dc.checkLoggedIn();
        if (dc.getUser().getOpenedPatient().isDischarged()) {
            throw new RuntimeException("Patient can't be discharged with unaproved diagnosis");
            //throw new PatientIsDischargedException();
        }
        Diagnosis diagnosis = dc.getUser().getOpenedPatient().isValidDiagnosisInfo(diag);
        DiagnosisSecondOpinion diagsec = ((DiagnosisSecondOpinion) diagnosis);
        if (!dc.getUser().getSecondOpinions().contains(diagsec)) {
            throw new InvalidDiagnosisException("Diagnosis was not found in doctor");
        }
        try {
            Argument[] args = new Argument[3];
            args[0] = (new StringArgument("Controller added")).enterAnswer(details);
            args[1] = (new DoctorArgument("Controller added")).setAnswer(diagsec.getSecondOpinion());
            args[2] = (new DoctorArgument("Controller added")).setAnswer(diagsec.getOriginalDoctor());
            dc.getUser().removeSecondOpinion(diagsec);
            DiagnosisCommand diaC = new DiagnosisCommand(wc.getWorld(), dc.getUser(), "Diagnosis with SecondOpinion", diagsec.getOriginalDoctor(), args);
            diagsec.setApproved(false);
            return diaC.execute();
        } catch (PatientIsDischargedException ex) {
            throw new RuntimeException("Patient can't be discharged with unaproved diagnosis");
        } catch (CannotDoException ex) {
            throw new RuntimeException("First time should always succeed");
        }
    }

    /**
     * Gets a list of doctors available to a ask a second opinion from
     * @return an array of LoginInfo's containing the information about available doctors
     * @throws NotLoggedInException the doctor is not logged in
     */
    @SystemAPI
    public LoginInfo[] getAvailableSecondOpinionDoctors() throws NotLoggedInException {
        dc.checkLoggedIn();
        List<Doctor> docList = wc.getWorld().getResourceOfClass(Doctor.class);
        List<LoginInfo> out = new ArrayList<LoginInfo>();
        for (Doctor d : docList) {
            if (!d.equals(this.dc.getUser())) {
                out.add(d.getLoginInfo());
            }
        }
        return out.toArray(new LoginInfo[0]);
    }

    /**
     * Used to find all possible types of diagnosis
     * @return an array of strings containing the names of all types of diagnosis-factory
     * @throws NotLoggedInException the doctor is not logged in
     */
    @SystemAPI
    //TODO:do we really need to be logged in for this action?
    public String[] getAvailableDiagnosisFactories() throws NotLoggedInException {
        dc.checkLoggedIn();
        try {
            return wc.getAvailableFactories(DiagnosisFactory.class).toArray(new String[0]);
        } catch (ArgumentIsNullException ex) {
            throw new RuntimeException("Class is not null");
        }
    }

    /**
     * Gets the required arguments to a type of diagnosis
     * @param factoryName the type of diagnosis
     * @return an array of PublicArguments which, when answered, can be used to create a diagnosis of the given type
     * @throws NotLoggedInException the doctor is not logged in
     * @throws NotAFactoryException the given type of diagnosis does not exist in this world
     */
    @SystemAPI
    public ArgumentList getDiagnosisArguments(String factoryName) throws NotLoggedInException, NotAFactoryException {
        dc.checkLoggedIn();
        try {
            return wc.getFactoryArguments(DiagnosisFactory.class, factoryName);
        } catch (ArgumentIsNullException ex) {
            throw new RuntimeException("Class is not null");
        }
    }
}
