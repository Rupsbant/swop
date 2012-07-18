package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Argument.Argument;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Patient.DiagnosisInfo;
import Hospital.Argument.PublicArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.Warehouse.ItemNotFoundException;
import Hospital.Exception.Warehouse.ItemNotReservedException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Warehouse.StockException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Patient.Patient;
import Hospital.Schedules.AppointmentCommand;
import Hospital.Treatments.TreatmentCommand;
import Hospital.Treatments.TreatmentFactory;
import Hospital.WareHouse.Items.MedicationItem;
import Hospital.World.World;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Used for treatment-related actions
 */
@SystemAPI
public class TreatmentController {

    /**
     * The doctor which performs these actions
     */
    DoctorController dc;
    /**
     * The world in which these actions are performed
     */
    WorldController wc;

    /**
     * Constructor
     * @param wc the world in which these actions are performed
     * @param dc the doctor which performs these actions
     */
    @SystemAPI
    public TreatmentController(WorldController wc, DoctorController dc) {
        this.dc = dc;
        this.wc = wc;
    }

    /**
     * Gets a list of untreated diagnoses for the currently opened patient
     * @return an array of DiagnosisInfos representing untreated diagnoses
     * @throws NotLoggedInException the doctor is not logged in
     * @throws NoOpenedPatientFileException the doctor has no patientfile opened
     */
    @SystemAPI
    public DiagnosisInfo[] getUntreatedDiagnoses() throws NotLoggedInException, NoOpenedPatientFileException {
        dc.checkLoggedIn();
        return dc.getUser().getOpenedPatient().getUntreatedDiagnoses();
    }

    /**
     * Gets the arguments to the creation of a treatment
     * @param treatment the type of treatment to be created
     * @return an array of PublicArguments which, when answered, can be used in the creation of a new Treatment-object of given type
     * @throws NotLoggedInException the doctor is not logged in
     * @throws NotAFactoryException the given type does not exist in this world
     */
    @SystemAPI
    public ArgumentList getTreatmentArguments(String treatment) throws NotLoggedInException, NotAFactoryException {
        dc.checkLoggedIn();
        try {
            ArgumentList out = wc.getFactoryArguments(TreatmentFactory.class, treatment);
            Argument[] args = AppointmentCommand.getArguments();
            out.addArguments(args);
            return out;
        } catch (ArgumentIsNullException ex) {
            throw new RuntimeException("Class is not null");
        }
    }

    /**
     * Creates a new treatment of the given type
     * @param treatment the type of treatment to create
     * @param args the arguments to the creation of the new treatment
     * @param diagnosis the diagnosis to which this treatment belongs
     * @return the details of the newly created treatment
     * @throws NotAFactoryException the given type does not exist in this world
     * @throws NotLoggedInException the doctor is not logged in
     * @throws WrongArgumentListException the given list of arguments did not match the type of treatment
     * @throws InvalidDiagnosisException the given diagnosis was not found in this world
     * @throws NoOpenedPatientFileException the doctor currently has no opened patientfile
     * @throws StockException somehow this world exists without plaster
     * @throws ItemNotFoundException the reserved item has somehow disappeared from the stock
     * @throws ItemNotReservedException somehow the plaster was not reserved either though we just did that the statement before
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    @SystemAPI
    public String makeTreatment(String treatment, ArgumentList args, DiagnosisInfo diagnosis)
            throws NotLoggedInException,
            NotAFactoryException,
            InvalidDiagnosisException,
            WrongArgumentListException,
            NoOpenedPatientFileException,
            StockException,
            ItemNotReservedException,
            ItemNotFoundException, 
            InvalidArgumentException {
        dc.checkLoggedIn();
        Patient openedPatient = dc.getUser().getOpenedPatient();
        World world = wc.getWorld();
        TreatmentCommand execTreatment = new TreatmentCommand(openedPatient, diagnosis, world, treatment, args.getAllArguments());
        dc.addCommand(execTreatment);
        try {
            return execTreatment.execute();
        } catch (CannotDoException ex) {
            Logger.getLogger(TreatmentController.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException();
        }
    }

    /**
     * Gets a list of the available treatments
     * @return an array of strings containing the names of treatments
     */
    @SystemAPI
    public String[] getAvailableTreatments() {
        try {
            return wc.getAvailableFactories(TreatmentFactory.class).toArray(new String[0]);
        } catch (ArgumentIsNullException ex) {
            throw new RuntimeException("Class can't be null");
        }
    }

    /**
     * Indicates whether a certain type of treatment involves medication items
     * @param chosen the type of treatment to check
     * @return true if the treatment requires medication
     * @throws ArgumentIsNullException the given string was null
     * @throws NotAFactoryException chosen was not an existing treatment
     */
    @SystemAPI
    public boolean needsMedication(String chosen) throws ArgumentIsNullException, NotAFactoryException {
        return wc.needsMedication(TreatmentFactory.class, chosen);
    }
}
