package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Treatments.Treatment;
import Hospital.World.Time;

/**
 * Represents a Treatment-object
 */
@SystemAPI
public class TreatmentInfo {
	/**
	 * the represented treatment
	 */
    private Treatment treatment;
    /**
     * Constructor
     * @param aThis the treatment to be represented
     */
    @SystemAPI
    public TreatmentInfo(Treatment aThis) {
        this.treatment = aThis;
    }

    /**
     * Getter for treatment
     * @return the represented treatment
     */
    private Treatment get(){
        return treatment;
    }

    /**
     * Returns the String of the wrapped Treatment
     * @return treatment.toString();
     */
    @Override
    @SystemAPI
    public String toString(){
        return treatment.toString();
    }

    /**
     * Returns true if this TreatmentInfo wraps or represents the same Object obj.
     * @param obj The object to test, a Treatment or TreatmentInfo can return true.
     * @return true if obj is a Treatment or TreatmentInfo and represent the same treatment.
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
        if(obj instanceof Treatment){
            if(treatment.equals(((Treatment) obj))){
                return true;
            }
        }
        if(obj instanceof TreatmentInfo){
            if(treatment.equals(((TreatmentInfo) obj).treatment)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the hashCode of the wrapped Treatment.
     * @return treatment.hashcode() if treatment not null.
     */
    @Override
    @SystemAPI
    public int hashCode() {
        return (this.treatment == null ? 0 : this.treatment.hashCode());
    }
    /**
     * Returns an ordering of the Appointment of the Treatment and the Time
     * @param t the time to compare with.
     * @return The ordering based on the startTime of the Appointment.
     */
    @SystemAPI
    public int compareTimeAppointment(Time t){
    	return treatment.getAppointment().getTimeFrame().compareTo(t);
    }



}
