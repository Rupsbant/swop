package Hospital.Patient;

import Hospital.Exception.Command.CannotDoException;
import Hospital.Factory.Command;
import Hospital.People.Doctor;

/**
 * Approves a diagnosis and schedules its associated treatment,
 * and undoes that later if necessary
 */
public class DiagnosisApproveCommand implements Command {

    /**
     * the diagnosis to be approved by this command
     */
    private DiagnosisSecondOpinion diagsec;
    /**
     * the approving doctor
     */
    private Doctor toApproveDoctor;
    /**
     * indicates whether the command is already executed
     */
    private boolean done;

    /**
     * Constructor
     * @param diagsec the diagnosis to be approved by this command
     * @param toApproveDoctor the approving doctor
     */
    public DiagnosisApproveCommand(DiagnosisSecondOpinion diagsec, Doctor toApproveDoctor) {
        this.diagsec = diagsec;
        this.toApproveDoctor = toApproveDoctor;
    }

    /**
     * Approves the diagnosis and schedules its associated treatment (if any)
     * @see Hospital.Factory.Command#execute()
     */
    public String execute() throws CannotDoException {
        if (done) {
            throw new CannotDoException("Cannot do twice!");
        }
        if (diagsec.isApproved() == null) {
            throw new CannotDoException("Cannot approve/disapprove same diagnosis twice!");
        }
        String s = diagsec.setApproved(true);
        toApproveDoctor.removeSecondOpinion(diagsec);
        return s + "\n" + diagsec.toString();
    }

    /**
     * @see Hospital.Factory.Command#undo()
     */
    public String undo() throws CannotDoException {
        if (!done) {
            throw new CannotDoException("Not yet done!");
        }
        if (diagsec.getTreatment().getAppointment() != null) {
            throw new CannotDoException("Diagnosis has treatment with appointment, undo appointment first!");
        }
        String s = diagsec.setApproved(null);
        toApproveDoctor.addSecondOpinions(diagsec);
        return "Undone:\n" + this.toString() + "\n" + s;
    }

    /**
     * @see Hospital.Factory.Command#isDone()
     */
    public boolean isDone() {
        return done;
    }

    /**
     * @return the details of the diagnosis
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.diagsec.toString();
    }
}
