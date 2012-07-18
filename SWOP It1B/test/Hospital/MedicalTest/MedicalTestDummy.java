package Hospital.MedicalTest;

import Hospital.Argument.Argument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Machine.Machine;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.AppointmentCommand;
import Hospital.Schedules.TimeFrame;
import Hospital.World.Campus;
import Hospital.World.Time;
import java.util.Collections;

public class MedicalTestDummy extends MedicalTest {

    String data;

    public MedicalTestDummy(String string) {
        data = string;
    }

    @Override
    public Argument[] getEmptyResultArgumentList() {
        // TODO Auto-generated method stub
        return new Argument[0];
    }

    @Override
    public void enterResult(Argument[] args)
            throws WrongArgumentListException, InvalidArgumentException {
        validateResults(args);
    }

    @Override
    public boolean isResultEntered() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Appointment getAppointment() {
        try {
            return new Appointment(new TimeFrame(new Time(), 10), Collections.EMPTY_LIST, (AppointmentCommand) null, (Campus) null);
        } catch (ArgumentConstraintException e) {
            throw new RuntimeException();
        } catch (ArgumentIsNullException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public String getResultString() {
        // TODO Auto-generated method stub
        return "resultaat";
    }

    @Override
    public String toString() {
        return "TestMedicalTest " + data;
    }

    public <S extends Machine> Class<S> getRequiredMachine() {
        return null;
    }

    public int getLength() {
        return 10;
    }

    public boolean validateResults(Argument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if(args.length>0){
            throw new WrongArgumentListException("LONG ARRAY IS LOOOOOOOONG");
        }
        return true;
    }
}
