package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Patient.DiagnosisInfo;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Patient.Diagnosis;
import Hospital.Patient.DiagnosisApproveCommand;
import Hospital.Patient.DiagnosisCreator;
import Hospital.Patient.DiagnosisSecondOpinion;
import Hospital.People.Doctor;
import Hospital.People.LoginInfo;
import Hospital.People.StaffRole;
import java.util.ArrayList;
import java.util.List;

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
     * This creates a diagnosis with the given content and the optional secondOpinion doctor
     * @param content The content of this diagnosis
     * @param secondOpinion The doctor that must approve the diagnosis
     * @return a description of the created diagnosis
     * @throws InvalidArgumentException if the content was null and shouldn't be
     * @throws NoPersonWithNameAndRoleException the given logininfo was not found in the system
     * @throws NoOpenedPatientFileException no patientfile was opened
     * @throws PatientIsDischargedException the patient was already discharged
     * @throws NotLoggedInException the doctor has logged out
     */
    @SystemAPI
    public String enterDiagnosis(String content, LoginInfo secondOpinion)
            throws InvalidArgumentException,
            NoPersonWithNameAndRoleException,
            NoOpenedPatientFileException,
            PatientIsDischargedException,
            NotLoggedInException {
        dc.checkLoggedIn();
        Doctor secondDoc = null;
        if (secondOpinion != null) {
            if (!secondOpinion.getRole().equals(StaffRole.Doctor)) {
                throw new NoPersonWithNameAndRoleException();
            }
            secondDoc = wc.getWorld().getPersonByName(Doctor.class, secondOpinion.getName());
            return DiagnosisCreator.SINGLETON.makeSecondOpinionDiagnosis(content, dc.getUser(), secondDoc);
        } else {
            return DiagnosisCreator.SINGLETON.makeNormalDiagnosis(content, dc.getUser());
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
        DiagnosisSecondOpinion diagsec = (DiagnosisSecondOpinion) dc.getUser().getOpenedPatient().isValidDiagnosisInfo(diag);
        if (diagsec == null) {
            throw new InvalidDiagnosisException("Diagnosis not found for opened patient");
        }
        DiagnosisApproveCommand diaAppC = new DiagnosisApproveCommand(diagsec, dc.getUser());
        return dc.addCommand(diaAppC);
    }

    /**
     * Disapproves a diagnosis
     * @param diag the diagnosis to disapprove
     * @param details to use
     * @return the details of the new diagnosis made to correct the original
     * @throws CannotChangeException if this happens, a newly created argument was somehow already answered
     * @throws NoOpenedPatientFileException the doctor has no patientfile opened
     * @throws NotLoggedInException the doctor is not logged in
     * @throws InvalidDiagnosisException the diagnosis could not be found
     * @throws NoPersonWithNameAndRoleException somehow something went wrong in finding the original doctor
     * @throws InvalidArgumentException if the details were null
     * @throws PatientIsDischargedException  
     */
    @SystemAPI
    public String disapproveDiagnosis(DiagnosisInfo diag, String details)
            throws CannotChangeException,
            NoPersonWithNameAndRoleException,
            NoOpenedPatientFileException,
            InvalidDiagnosisException,
            NotLoggedInException,
            InvalidArgumentException,
            PatientIsDischargedException {
        dc.checkLoggedIn();
        if (dc.getUser().getOpenedPatient().isDischarged()) {
            throw new Error("Patient can't be discharged with unapproved diagnosis");
        }
        Diagnosis diagnosis = dc.getUser().getOpenedPatient().isValidDiagnosisInfo(diag);
        DiagnosisSecondOpinion diagsec = ((DiagnosisSecondOpinion) diagnosis);
        if (!dc.getUser().getSecondOpinions().contains(diagsec)) {
            throw new InvalidDiagnosisException("Diagnosis was not found in doctor");
        }
        dc.getUser().removeSecondOpinion(diagsec);
        diagsec.setApproved(false);
        return DiagnosisCreator.SINGLETON.makeSecondOpinionDiagnosis(details, dc.getUser(), diagsec.getOriginalDoctor());
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
}
