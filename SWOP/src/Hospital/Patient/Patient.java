package Hospital.Patient;

import Hospital.Controllers.PatientFile;
import Hospital.Controllers.TreatmentInfo;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Patient.CannotDischargeException;
import Hospital.Exception.Patient.InvalidDiagnosisException;
import Hospital.Exception.Patient.PatientIsCheckedInException;
import Hospital.MedicalTest.MedicalTest;
import java.util.ArrayList;
import Hospital.People.Person;
import Hospital.Schedules.Constraints.TimeFrameConstraint;
import Hospital.World.Campus;

import java.util.List;

/**
 * A patient, who can be diagnosed, tested and treated
 */
public class Patient extends Person {

    /**
     * indicates whether this person currently is not staying in the hospital
     */
    private boolean isDischarged;
    /**
     * the list of diagnoses made about this patient
     */
    private ArrayList<Diagnosis> diagnoses;
    /**
     * the list of tests (to be) performed on this patient
     */
    private ArrayList<MedicalTest> medicalTests;
    /**
     * the campus where the patient is checked in
     */
    private Campus campus;

    /**
     * Constructor
     * @param name the name of this patient
     * @throws ArgumentConstraintException the given name is empty
     * @throws ArgumentIsNullException the given name is null
     */
    public Patient(String name) throws ArgumentConstraintException, ArgumentIsNullException {
        this(name, true);
    }

    /**
     * Extended constructor
     * @param name the name of this patient
     * @param discharged is the patient not yet checked in when he/she is created
     * @throws ArgumentConstraintException the given name is empty
     * @throws ArgumentIsNullException the given name is null
     */
    public Patient(String name, boolean discharged) throws ArgumentConstraintException, ArgumentIsNullException {
        super(name);
        this.setDischarged(discharged);
        this.diagnoses = new ArrayList<Diagnosis>();
        this.medicalTests = new ArrayList<MedicalTest>();
    }

    /**
     * Sets whether the patient is discharged or not.
     * @param isDischarged true if the patient is discharged
     */
    private final void setDischarged(boolean isDischarged) {
        this.isDischarged = isDischarged;
    }

    /**
     * Returns whether the patient is discharged or checked in the hospital.
     * @return true if discharged.
     */
    public boolean isDischarged() {
        return isDischarged;
    }

    /**
     * Returns true if the patient can be discharged. <br> A patient can be discharged if there are no Diagnsoses without Treatment, not Treatments without result and no MedicalTest without result and if the patient is checked in.
     * @return true if the patient can be discharged.
     */
    public boolean canBeDischarged() {
        if (isDischarged) {
            return false;
        }
        boolean test = true;
        for (Diagnosis d : getDiagnoses()) {
            test = d.canStartTreatment();
            if (!test) {
                break;
            }
            if (d.getTreatment() == null) {
                continue;
            }
            test = d.getTreatment().isResultEntered();
            if (!test) {
                break;
            }
        }
        for (MedicalTest mt : getMedicalTests()) {
            if (!test) {
                break;
            }
            test = mt.isResultEntered();
        }
        return test;
    }

    /**
     * Returns a list of MedicalTests ordered for this patient.
     * @return The list of MedicalTests done or planned for this patient.
     */
    public ArrayList<MedicalTest> getMedicalTests() {
        return (ArrayList<MedicalTest>) medicalTests.clone();
    }

    /**
     * Adds a MedicalTest to the file of the patient.
     * @param t The MedicalTest to add.
     */
    public void addMedicalTest(MedicalTest t) {
        medicalTests.add(t);
    }

    /**
     * Returns a list of all diagnoses added to this patient's file.
     * @return A List of all diagnsoses.
     */
    public ArrayList<Diagnosis> getDiagnoses() {
        return (ArrayList<Diagnosis>) diagnoses.clone();
    }

    /**
     * Adds a Diagnosis to this patient's file.
     * @param diagnose The Diagnosis to add.
     */
    public void addDiagnosis(Diagnosis diagnose) {
        this.diagnoses.add(diagnose);
    }

    /**
     * Returns a list of all Diagnoses without treatment.
     * @return List of all untreated Diagnoses.
     */
    public DiagnosisInfo[] getUntreatedDiagnoses() {
        ArrayList<DiagnosisInfo> out = new ArrayList<DiagnosisInfo>();
        for (Diagnosis d : diagnoses) {
            if (!d.hasTreatment()) {
                out.add(d.getDiagnosisInfo());
            }
        }
        return out.toArray(new DiagnosisInfo[0]);
    }

    /**
     * Returns whether the patient has untreated Diagnoses or not.
     * @return True if the patient has untreated Diagnsoses.
     */
    public boolean hasUntreatedDiagnoses() {
        for (Diagnosis d : diagnoses) {
            if (!d.hasTreatment()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a wrapper for this patient.
     * @return a new PatientFile to represent this patient.
     */
    public PatientFile getPatientFile() {
        try {
            return new PatientFile(this);
        } catch (ArgumentIsNullException ex) {
            throw new Error("This is not null!!!");
        }
    }

    /**
     * Checks a patient in the hospital.
     * @throws PatientIsCheckedInException If the patient was already checked in.
     * @throws InvalidArgumentException the given campus was null
     */
    public void checkIn(Campus campus) throws PatientIsCheckedInException, InvalidArgumentException {
        if (!isDischarged()) {
            throw new PatientIsCheckedInException();
        }
        setCampus(campus);
        setDischarged(false);
    }

    /**
     * This method checks if the DiagnosisInfo that came from the outside is valid. i.e. The diagnosisobject was made in the system and is thus 'safe'.
     * @param diagnosis The informationObject to be checked
     * @return The diagnosis if no errors are thrown.
     * @throws InvalidDiagnosisException if diagnosis was null or not found
     */
    public Diagnosis isValidDiagnosisInfo(DiagnosisInfo diagnosis) throws InvalidDiagnosisException {
        if (diagnosis == null) {
            throw new InvalidDiagnosisException("Diagnosis of diagnosisInfo was null");
        }
        if (!getDiagnoses().contains(diagnosis.get())) {
            throw new InvalidDiagnosisException("Diagnosis did not exist");
        }
        return diagnosis.get();
    }

    /**
     * Removes a MedicalTest from the patient's file.
     * @param made The MedicalTest to remove
     * @return true if the MedicalTest was found.
     */
    public boolean removeMedicalTest(MedicalTest made) {
        return medicalTests.remove(made);
    }

    /**
     * Returns true if obj represents the same patient, based on the class and the UNIQUE name.
     * @param obj The Object to test
     * @return true if patient is a Patient and the name is the same.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Patient) {
            return getName().equals(((Patient) obj).getName());
        }
        return false;
    }

    /**
     * Returns the hashCode of the patient.
     * @return Hashcode based on name.
     */
    @Override
    public int hashCode() {
        return getName().hashCode() * 43;
    }

    /**
     * This method makes this object visit the constraints to approve them as a Schedulable and as a Patient.
     * @param tf The TimeFrame during which the constraints must be checked.
     * @param tfContstraints The list of constraints.
     * @return The constraints for simpler code : doctor.setValidTimeFrame(tf, tfc).acceptAll();.
     */
    @Override
    public TimeFrameConstraint setValidTimeFrame(TimeFrameConstraint tfContstraints) {
        super.setValidTimeFrame(tfContstraints);
        tfContstraints.setValidTimeFramePatient(this);
        return tfContstraints;
    }

    /**
     * Removes a diagnosis from the list.
     * @param diagnosis Diagnosis to remove
     * @return true if diagnosis was found, false otherwise.
     */
    boolean removeDiagnosis(Diagnosis diagnosis) {
        return diagnoses.remove(diagnosis);
    }

    /**
     * Returns a list of all treatments without result that were scheduled.
     * @return a list of TreatmentInfos referring to the patient's treatments
     */
    public List<TreatmentInfo> getOpenTreatments() {
        ArrayList<TreatmentInfo> out = new ArrayList<TreatmentInfo>();
        for (Diagnosis d : diagnoses) {
            if (d.getTreatment() == null) {
                continue;
            }
            if (d.getTreatment().canEnterResults() && !d.getTreatment().isResultEntered()) {
                out.add(d.getTreatment().getInfo());
            }
        }
        return out;
    }

    /**
     * This method discharges the patient.
     * If this is not possible, CannotDischargeException is thrown.
     * @throws CannotDischargeException if the patient cannot be discharged
     */
    public void dischargePatient() throws CannotDischargeException {
        if (!canBeDischarged()) {
            throw new CannotDischargeException();
        }
        setDischarged(true);
    }

    /**
     * Sets the campus where the patient is currently checked in
     * @param campus
     * @throws InvalidArgumentException nothing changed because the given campus was null
     */
    private void setCampus(Campus campus) throws InvalidArgumentException {
        if (campus == null) {
            throw new ArgumentIsNullException("the given campus was null");
        }
        this.campus = campus;
    }

    /**
     * @return the campus where the patient was last checked in
     */
    public Campus getCampus() {
        return campus;
    }
}
