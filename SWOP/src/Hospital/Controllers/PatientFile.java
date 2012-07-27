package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Patient.Patient;

/**
 * This class gives a way to view the PatientFile in a safe way.
 */
@SystemAPI
public final class PatientFile {
	/**
	 * the patient to which this file belongs
	 */
    private Patient patient;

    /**
     * This creates a view for a given patient.
     * @param patient The patient whose patientfile to export.
     * @throws ArgumentIsNullException if the patient was null
     */
    @SystemAPI
    public PatientFile(Patient patient) throws ArgumentIsNullException {
        setPatient(patient);
    }

    /**
     * Sets the patient to which this file belongs
     * @param patient the subject of this patient file
     * @throws ArgumentIsNullException the given patient was null
     */
    private void setPatient(Patient patient) throws ArgumentIsNullException {
        if(patient == null){
            throw new ArgumentIsNullException("Given patient was null");
        }
        this.patient = patient;
    }

    /**
     * Returns the representation of the wrapped patient.
     * @return patient.toString();
     */
    @Override
    @SystemAPI
    public String toString() {
        return patient.toString();
    }

    /**
     * Gives an output of all diagnoses and medicalTests in a patient's file
     * @return an intermediate output.
     */
    @SystemAPI
    public String[] getPatientFileList() {
        String[] output = new String[patient.getDiagnoses().size()+patient.getMedicalTests().size()];
        for(int i = 0; i<patient.getDiagnoses().size(); i++){
            output[i] = patient.getDiagnoses().get(i).toString();
        }
        for(int i = 0; i<patient.getMedicalTests().size(); i++){
            output[patient.getDiagnoses().size()+i] = patient.getMedicalTests().get(i).toString();
        }
        return output;
    }

    /**
     * Returns a full output of a given case.
     * @param pos The position of the case in the patient's file.
     * @return The full output.
     */
    @SystemAPI
    public String getAdvancedInformation(int pos){
        if(pos<patient.getDiagnoses().size()){
            return patient.getDiagnoses().get(pos).advancedString();
        } else {
            return patient.getMedicalTests().get(pos-patient.getDiagnoses().size()).advancedString();
        }
    }

}
