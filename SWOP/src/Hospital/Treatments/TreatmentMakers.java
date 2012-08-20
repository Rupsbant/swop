package Hospital.Treatments;

import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Patient.DiagnosisInfo;
import Hospital.Patient.Patient;
import Hospital.People.Doctor;
import Hospital.Schedules.Constraints.Priority.Priority;
import Hospital.WareHouse.ItemInfo;
import Hospital.World.World;

/**
 * This class makes different treatments
 */
public class TreatmentMakers {

    /**
     * The singleton
     */
    public static TreatmentMakers SINGLETON = new TreatmentMakers();

    /**
     * Creates a cast
     * @param orderingDoc
     * @param bodyPart
     * @param duration
     * @param patient
     * @param diagnosis
     * @param world
     * @param priority
     * @return a description of the cast
     * @throws InvalidDiagnosisException
     * @throws InvalidArgumentException
     */
    public String makeCast(Doctor orderingDoc, String bodyPart, int duration,
            Patient patient, DiagnosisInfo diagnosis, World world, Priority priority)
            throws InvalidDiagnosisException, InvalidArgumentException {
        Treatment out = new Cast(bodyPart, duration);
        TreatmentCommand outCommand = new TreatmentCommand(patient, diagnosis, world, out, priority);
        try {
            return orderingDoc.getHistory().addCommand(outCommand);
        } catch (CannotDoException ex) {
            throw new Error(ex);
        }
    }

    /**
     * Creates a medicationTreatment with the given medication
     * @param orderingDoc
     * @param description
     * @param sensitivity
     * @param items
     * @param patient
     * @param diagnosis
     * @param world
     * @param p
     * @return
     * @throws InvalidDiagnosisException
     * @throws InvalidArgumentException
     */
    public String makeMedication(Doctor orderingDoc, String description, boolean sensitivity, String items,
            Patient patient, DiagnosisInfo diagnosis, World world, Priority p)
            throws InvalidDiagnosisException, InvalidArgumentException {
        ItemInfo[] infos = ItemInfo.getItemInfo(items);
        Treatment out = new Medication(description, sensitivity, infos);
        TreatmentCommand outCommand = new TreatmentCommand(patient, diagnosis, world, out, p);
        try {
            return orderingDoc.getHistory().addCommand(outCommand);
        } catch (CannotDoException ex) {
            throw new Error(ex);
        }
    }

    /**
     * Creates a surgery with the given parameters
     * @param orderingDoc
     * @param description
     * @param patient
     * @param diagnosis
     * @param world
     * @param p
     * @return a description
     * @throws InvalidDiagnosisException
     * @throws InvalidArgumentException
     */
    public String makeSurgery(Doctor orderingDoc, String description,
            Patient patient, DiagnosisInfo diagnosis, World world, Priority p)
            throws InvalidDiagnosisException, InvalidArgumentException {
        Treatment out = new Surgery(description);
        TreatmentCommand outCommand = new TreatmentCommand(patient, diagnosis, world, out, p);
        try {
            return orderingDoc.getHistory().addCommand(outCommand);
        } catch (CannotDoException ex) {
            throw new Error(ex);
        }
    }
}
