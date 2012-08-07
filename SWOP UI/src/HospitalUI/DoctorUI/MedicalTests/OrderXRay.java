package HospitalUI.DoctorUI.MedicalTests;

import Hospital.Controllers.MedicalTestController;
import java.util.Scanner;

public class OrderXRay implements RunnableUI {

    public void run(Scanner sc, MedicalTestController med) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        return "Order new XRayScan";
    }
    
    public String toString(){
        return getName();
    }
    
}
