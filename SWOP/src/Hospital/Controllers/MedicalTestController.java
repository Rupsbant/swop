package Hospital.Controllers;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.*;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.MedicalTest.MedicalTestCreator;
import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.SystemAPI;

/**
 * Used to perform MedicalTest-related actions
 * This controller enables the following usecases:
 * Order medicaltest.
 */
@SystemAPI
public class MedicalTestController {

    /**
     * the world in which to perform these actions
     */
    private WorldController wc;
    /**
     * the doctor performing these actions
     */
    private DoctorController dc;

    /**
     * Constructor
     * @param wc the world in which to perform these actions
     * @param dc the doctor performing these actions
     */
    @SystemAPI
    public MedicalTestController(WorldController wc, DoctorController dc) {
        this.wc = wc;
        this.dc = dc;
    }

    /**
     * Creates a new BloodAnalysis test of the given type
     * @return the details of the newly created test
     * @throws NotLoggedInException the doctor is not logged in 
     * @throws WrongArgumentListException the given arguments did not match the medical test type
     * @throws InvalidArgumentException thrown if one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    public String makeBloodAnalysis(String focus, int numberOfImages, Priority p) throws NoOpenedPatientFileException, ArgumentConstraintException, InvalidArgumentException {
        return MedicalTestCreator.SINGLETON.makeBloodAnalysis(wc.getWorld(), dc.getUser(), p, focus, numberOfImages);
    }

    /**
     * Creates a new UltraSouncScan test of the given type
     * @return the details of the newly created test
     * @throws NotLoggedInException the doctor is not logged in 
     * @throws WrongArgumentListException the given arguments did not match the medical test type
     * @throws InvalidArgumentException thrown if one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    public String makeUltraSound(String focus, boolean recordVideo, boolean recordImages, Priority p) throws NoOpenedPatientFileException, ArgumentConstraintException, InvalidArgumentException {
        return MedicalTestCreator.SINGLETON.makeUltraSoundScan(wc.getWorld(), dc.getUser(), p, focus, recordVideo, recordImages);
    }

    /**
     * Creates a new XRayScan test of the given type
     * @return the details of the newly created test
     * @throws NotLoggedInException the doctor is not logged in 
     * @throws WrongArgumentListException the given arguments did not match the medical test type
     * @throws InvalidArgumentException thrown if one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    public String makeXRayScan(String bodyPart, int zoom, int numberOfImages, Priority p) throws NoOpenedPatientFileException, ArgumentConstraintException, InvalidArgumentException {
        return  MedicalTestCreator.SINGLETON.makeXRayScan(wc.getWorld(), dc.getUser(), p, bodyPart, zoom, numberOfImages);
    }
}