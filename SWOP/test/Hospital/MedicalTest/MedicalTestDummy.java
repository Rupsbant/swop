package Hospital.MedicalTest;

import Hospital.Argument.PublicArgument;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.Machine.Machine;
import Hospital.Schedules.Appointment;
import Hospital.Schedules.AppointmentCommand;
import Hospital.World.Campus;
import Hospital.World.Time;
import java.util.Collections;

public class MedicalTestDummy extends MedicalTest {

    String data;

    public MedicalTestDummy(String string) {
        data = string;
    }

    @Override
    public PublicArgument[] getEmptyResultArgumentList() {
        // TODO Auto-generated method stub
        return new PublicArgument[0];
    }

    @Override
    public void enterResult(PublicArgument[] args)
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
        return new Appointment(new Time(), 10, Collections.EMPTY_LIST, null, null, null);
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

    public boolean validateResults(PublicArgument[] args) throws WrongArgumentListException, InvalidArgumentException {
        if (args.length > 0) {
            throw new WrongArgumentListException("LONG ARRAY IS LOOOOOOOONG");
        }
        return true;
    }
}
