package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.MedicalTest.MedicalTest;
import Hospital.World.Time;

/**
 * This class represents a medical test object
 */
@SystemAPI
public class MedicalTestInfo {

    /**
     * the medical test that is represented
     */
    private MedicalTest m;

    /**
     * Constructor
     * @param m the medical test that is represented
     */
    @SystemAPI
    public MedicalTestInfo(MedicalTest m) {
        this.m = m;
    }

    /**
     * Gets the details of the represented medical test
     * @return a detailed string about the medical test
     */
    @SystemAPI
    public String advancedString() {
    	return m.advancedString();
    }
    
    /**
     * Returns an ordering of the Appointment of the medicalTest and the Time
     * @param t the time to compare with.
     * @return The ordering based on the startTime of the Appointment.
     */
    @SystemAPI
    public int compareTimeAppointment(Time t){
    	return m.getAppointment().getTimeFrame().compareTo(t);
    }

    /**
     * Returns true if this Info-object holds the same MedicalTest as the parameter.
     * @param obj The object to test equality with.
     * @return true if obj is a MedicalTest and the wrapped MedicalTest is equal of if obj is a MedicalTestInfo and obj holds an equal MedicalTest.
     */
    @Override
    @SystemAPI
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(super.equals(obj)){
            return true;
        }
        if(obj instanceof MedicalTest){
            if(m.equals(((MedicalTest) obj))){
                return true;
            }
        }
        if(obj instanceof MedicalTestInfo){
            if(m.equals(((MedicalTestInfo) obj).m)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a hashcode based on the medicalTest it holds.
     * The hashCode of a MedicalTest and it's MedicalTestInfo are equal.
     * @return The hashcode of the wrapped medicalTest.
     */
    @Override
    @SystemAPI
    public int hashCode() {
        return (this.m != null ? this.m.hashCode() : 0);
    }



}
