package Hospital.Controllers;

import Hospital.Argument.PublicArgument;
import Hospital.SystemAPI;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.IllegalInfo;
import Hospital.Exception.NotLoggedInException;
import Hospital.Patient.Diagnosis;
import Hospital.Patient.Patient;
import Hospital.Treatments.Treatment;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This controller adds the function to enter treatment-results to the main public API
 * This controller enables the following usecases: 
 * Enter TreatmentResults: 
 *      return the list of Arguments that must be answered
 *      enter the list of Arguments as results in the treatment
 */
@SystemAPI
public class TreatmentResultController {

    /**
     * the world in which this controller works
     */
    private WorldController wc;
    /**
     * the nurse using this controller
     */
    private NurseController nc;

    /**
     * This constructs this controller with a given WorldController and NurseController.
     * @param wc the world in which this controller works
     * @param nc the nurse using this controller
     * @throws ArgumentIsNullException wc or nc was null
     */
    @SystemAPI
    public TreatmentResultController(WorldController wc, NurseController nc) throws ArgumentIsNullException {
        setWorldController(wc);
        setNurseController(nc);
    }

    /**
     * Sets the nurse using this controller
     * @param nc a NurseController
     * @throws ArgumentIsNullException nc was null
     */
    private void setNurseController(NurseController nc) throws ArgumentIsNullException {
        if (nc == null) {
            throw new ArgumentIsNullException("NurseController was null");
        }
        this.nc = nc;
    }

    /**
     * Sets the world in which this controller works
     * @param wc a WorldController
     * @throws ArgumentIsNullException wc was null
     */
    private void setWorldController(WorldController wc) throws ArgumentIsNullException {
        if (wc == null) {
            throw new ArgumentIsNullException("WorldController was null");
        }
        this.wc = wc;
    }

    /**
     * Returns an array of all treatments without result.
     * @return an array of TreatmentInfos containing treatments without result
     * @throws NotLoggedInException the nurse is not logged in
     */
    @SystemAPI
    public TreatmentInfo[] getOpenTreatments() throws NotLoggedInException {
        nc.checkLoggedIn();
        List<Patient> patients = wc.getWorld().getResourceOfClass(Patient.class);
        ArrayList<TreatmentInfo> out = new ArrayList<TreatmentInfo>();
        for (Patient p : patients) {
            out.addAll(p.getOpenTreatments());
        }
        return out.toArray(new TreatmentInfo[0]);
    }

    /**
     * Returns the arguments needed to enter the treatment result
     * @param m The treatmentInfo associated with the treatment to enter the result of.
     * @return The arguments to be answered first
     * @throws IllegalInfo if the treatment was not found in the world
     */
    @SystemAPI
    public PublicArgument[] getArguments(TreatmentInfo m) throws IllegalInfo {
        Treatment t = getTreatment(m);
        return t.getEmptyResultArgumentList();
    }

    /**
     * Enters the result in the system with the given arguments.
     * @param m The treatmentInfo associated with the treatment to enter the results of.
     * @param args The arguments needed to enter the treatment.
     * @return A resultString  of the entered treatmentResult.
     * @throws IllegalInfo If the associated treatment was not found.
     * @throws InvalidArgumentException If the given ArgumentList was null, of the wrong length, not answered or if the answer did not satisfy the constraints.
     * @throws NotLoggedInException If the nurse was logged out.
     */
    @SystemAPI
    public String enterResult(TreatmentInfo m, PublicArgument[] args)
            throws InvalidArgumentException, IllegalInfo, NotLoggedInException {
        Treatment t = getTreatment(m);
        if (args == null) {
            throw new ArgumentIsNullException("ArgumentList was null");
        }
        t.enterResult(args);
        return m.toString();
    }

    /**
     * Returns the associated treatment.
     * @param m The public treatmentInfo.
     * @return The treatment found in the world.
     * @throws IllegalInfo if the treatment did not exist.
     */
    private Treatment getTreatment(TreatmentInfo m) throws IllegalInfo {
        if (m == null) {
            throw new IllegalInfo("Treatment is null");
        }
        Treatment t = null;
        for (Patient p : wc.getWorld().getResourceOfClass(Patient.class)) {
            for (Diagnosis d : p.getDiagnoses()) {
                if (m.equals(d.getTreatment())) {
                    t = d.getTreatment();
                }
            }
        }
        if (t == null) {
            throw new IllegalInfo("Treatment does not exist in world");
        }
        return t;
    }
}
