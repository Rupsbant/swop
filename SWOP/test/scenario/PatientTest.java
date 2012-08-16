package scenario;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Warehouse.NotEnoughItemsAvailableException;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Hospital.Argument.PublicArgument;
import Hospital.Argument.StringArgument;
import Hospital.Controllers.ArgumentList;
import Hospital.Controllers.DiagnosisController;
import Hospital.Controllers.DoctorController;
import Hospital.Controllers.NurseController;
import Hospital.Controllers.PatientController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.CannotChangeException;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.PatientIsCheckedInException;
import Hospital.Exception.Patient.PatientIsDischargedException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.People.LoginInfo;
import Hospital.World.BasicWorld;

public class PatientTest {

    WorldController w;

    @Before
    public void setUp() {
        w = BasicWorld.getBasicWorld();
    }

    @Test
    public void test()
            throws CannotChangeException, InvalidArgumentException, NoPersonWithNameAndRoleException,
            SchedulingException, NotLoggedInException, NotAFactoryException, SchedulableAlreadyExistsException, WrongArgumentListException, NoOpenedPatientFileException, PatientIsDischargedException, ArgumentIsNullException, ArgumentConstraintException, NotEnoughItemsAvailableException {
        LoginInfo login = login("Nurse", "Verpleegster");
        NurseController nc = null;
        nc = (NurseController) w.login(w.getCampuses().get(0),login);
        PatientController pc = new PatientController(w, nc);
        PublicArgument[] args = new PublicArgument[1];
        args[0] = new StringArgument("name").enterAnswer("Jef");
        try {
            pc.registerPatient("Jef");
            System.out.println(nc.checkIn("Jef", "Doktoor", w));
            System.out.println(nc.checkIn("Jeroen", "Doktoor", w));
        } catch (PatientIsCheckedInException e) {
        }
        login = login("Doctor", "Doktoor");
        DoctorController dc = (DoctorController) w.login(w.getCampuses().get(0),login);
        dc.consultPatientFile("Jeroen", w);
        DiagnosisController diag = new DiagnosisController(w, dc);
        System.out.println(diag.enterDiagnosis("details blabla", null));
    }

    public LoginInfo login(String role, String name) {
        List<LoginInfo> logins = w.getLogins();
        LoginInfo login = null;
        for (LoginInfo loginInfo : logins) {
            if (loginInfo.getRole().toString().equals(role) && loginInfo.getName().equals(name)) {
                login = loginInfo;
                break;
            }
        }
        if (login == null) {
            fail("Login fail");
        }
        return login;
    }
}
