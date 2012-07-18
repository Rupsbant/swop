package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.IllegalInfo;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.MedicalTest.MedicalTest;
import Hospital.Patient.Patient;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used to perform MedicalTestResult-related actions.
 */
@SystemAPI
public class MedicalTestResultController {

	/**
	 * the nurse performing these actions
	 */
    private NurseController nc;
    /**
     * the world in which these actions are performed
     */
    private WorldController wc;

    /**
     * Constructor
     * @param wc world in which these actions are performed
     * @param nc the nurse performing these actions
     */
    @SystemAPI
    public MedicalTestResultController(WorldController wc, NurseController nc) {
        this.nc = nc;
        this.wc = wc;
    }

    /**
     * Gets all open medical tests in the world (open being "without result")
     * @return an array of MedicalTestInfos containing the information of open MedicalTests
     * @throws NotLoggedInException the nurse is not logged in
     */
    @SystemAPI
    public MedicalTestInfo[] getOpenMedicalTests() throws NotLoggedInException {
        nc.checkLoggedIn();
        List<Patient> patients = wc.getWorld().getResourceOfClass(Patient.class);
        ArrayList<MedicalTestInfo> out = new ArrayList<MedicalTestInfo>();
        for (Patient p : patients) {
            List<MedicalTest> medicalTests = p.getMedicalTests();
            for (MedicalTest t : medicalTests) {
                if (!t.isResultEntered()) {
                    out.add(t.getInfo());
                }
            }
        }
        return out.toArray(new MedicalTestInfo[0]);
    }

    /**
     * Gets the result arguments to a given type of medical test
     * @param m a MedicalTestInfo object representing the type of test to which a result will be added
     * @return a ArgumentList which, when answered, can be used for entering the results to a medical test
     * @throws IllegalInfo the given MedicalTestInfo did not represent an existing MedicalTest in this world
     */
    @SystemAPI
    public ArgumentList getArguments(MedicalTestInfo m) throws IllegalInfo {
        MedicalTest med = getMedicalTest(m);
        try {
            return new ArgumentList(med.getEmptyResultArgumentList());
        } catch (ArgumentIsNullException ex) {
            Logger.getLogger(MedicalTestResultController.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("ArgumentList should not give null");
        }
    }

    /**
     * Gets the MedicalTest represented by a MedicalTestInfo-object
     * @param m the MedicalTestInfo to resolve
     * @return the MedicalTest represented by m
     * @throws IllegalInfo the given MedicalTestInfo did not represent an existing MedicalTest in this world
     */
    @SystemAPI
    private MedicalTest getMedicalTest(MedicalTestInfo m) throws IllegalInfo {
        if(m == null){
            throw new IllegalInfo("MedicalTest is null");
        }
        MedicalTest med = null;
        for (Patient p : wc.getWorld().getResourceOfClass(Patient.class)) {
            for (MedicalTest test : p.getMedicalTests()) {
                if (m.equals(test)) {
                    med = test;
                }
            }
        }
        if(med == null){
            throw new IllegalInfo("MedicalTest not found");
        }
        return med;
    }

    /**
     * Enters the result to a medical test
     * @param m the test in which to enter the result
     * @param args the arguments to the result of the medical test
     * @return the advanced details of the medical test 
     * @throws NotLoggedInException the nurse is not logged in
     * @throws IllegalInfo the given medical test could not be found in the world
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    @SystemAPI
    public String enterResult(MedicalTestInfo m, ArgumentList args)
            throws InvalidArgumentException, NotLoggedInException, IllegalInfo {
        nc.checkLoggedIn();
        if(args == null){
            throw new ArgumentIsNullException("ArgumentList was null");
        }
        getMedicalTest(m).enterResult(args.getAllArguments());
        return getMedicalTest(m).advancedString();
    }
}
