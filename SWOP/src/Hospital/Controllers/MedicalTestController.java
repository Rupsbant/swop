package Hospital.Controllers;

import Hospital.Argument.Argument;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.*;
import Hospital.Argument.PublicArgument;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.MedicalTest.MedicalTestCommand;
import Hospital.MedicalTest.MedicalTestFactory;
import Hospital.Patient.Patient;
import Hospital.Schedules.AppointmentCommand;
import Hospital.SystemAPI;
import Hospital.Utils;

/**
 * Used to perform MedicalTest-related actions
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
     * Gets the arguments to a type of MedicalTest
     * @param medicalTest the type of MedicalTest
     * @return an array of PubblicArguments which, when answered, can be used 
     *         for the creation of a new MedicalTest of the given type
     * @throws NotLoggedInException the doctor is not logged in
     * @throws NotAFactoryException the given type was invalid
     */
    @SystemAPI
    public ArgumentList getMedicalTestArguments(String medicalTest) throws NotLoggedInException, NotAFactoryException {
        dc.checkLoggedIn();
        try {
            ArgumentList out1 = wc.getFactoryArguments(MedicalTestFactory.class, medicalTest);
            Argument[] out2 = AppointmentCommand.getArguments();
            out1.addArguments(out2);
            return out1;
        } catch (ArgumentIsNullException ex) {
            throw new Error("Class is not null");
        }
    }

    /**
     * Creates a new medical test of the given type
     * @param medicalTest the type of test to create
     * @param argv the arguments to the creation of the new test
     * @return the details of the newly created test
     * @throws NotAFactoryException the given type of medical test was invalid
     * @throws NotLoggedInException the doctor is not logged in 
     * @throws WrongArgumentListException the given arguments did not match the medical test type
     * @throws NoOpenedPatientFileException the doctor has no patientfile opened for which to create this test
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    @SystemAPI
    public String makeMedicalTest(String medicalTest, ArgumentList argv) throws NotAFactoryException, NotLoggedInException, InvalidArgumentException, WrongArgumentListException, NoOpenedPatientFileException {
        dc.checkLoggedIn();
        Patient openedPatient = dc.getUser().getOpenedPatient();
        if(argv == null){
            throw new ArgumentIsNullException("ArgumentList was null");
        }
        MedicalTestCommand medCom = new MedicalTestCommand(wc.getWorld(), openedPatient, medicalTest, argv.getAllArguments());
        dc.addCommand(medCom);
        try {
            return medCom.execute();
        } catch (CannotDoException ex) {
            throw new Error("New command was not done: " + ex);
        }
    }

    /**.
     * Gets the available medical tests which can be created
     * @return an array of strings containing the details of tests
     */
    @SystemAPI
    public String[] getAvailableMedicalTests() {
        try {
            return wc.getAvailableFactories(MedicalTestFactory.class).toArray(new String[0]);
        } catch (ArgumentIsNullException ex) {
            throw new Error("Class is not null!");
        }
    }
}
