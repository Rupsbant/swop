package HospitalUI.DoctorUI.MedicalTests;

import Hospital.Controllers.MedicalTestController;
import java.util.Scanner;

public interface RunnableUI {

    public void run(Scanner sc, MedicalTestController med);
    
    public String getName();
}
