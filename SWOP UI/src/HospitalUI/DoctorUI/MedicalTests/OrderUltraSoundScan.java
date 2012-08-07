package HospitalUI.DoctorUI.MedicalTests;

import Hospital.Controllers.MedicalTestController;
import java.util.Scanner;

public class OrderUltraSoundScan implements RunnableUI {

    public void run(Scanner sc, MedicalTestController med) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        return "Order new UltraSoundScan";
    }
    
    public String toString(){
        return getName();
    }
    
}
