package HospitalUI.DoctorUI;

import Hospital.Controllers.DoctorController;
import Hospital.Controllers.MedicalTestController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Patient.NoOpenedPatientFileException;
import Hospital.Exception.NotLoggedInException;
import HospitalUI.DoctorUI.MedicalTests.OrderBloodAnalysis;
import HospitalUI.DoctorUI.MedicalTests.OrderUltraSoundScan;
import HospitalUI.DoctorUI.MedicalTests.OrderXRay;
import HospitalUI.DoctorUI.MedicalTests.RunnableUI;
import HospitalUI.MainUI.UtilsUI;

import java.util.Scanner;

public class OrderMedicalUI {

    private DoctorController doc;
    private WorldController wc;
    private MedicalTestController med;
    private RunnableUI[] medicalTests = new RunnableUI[]{new OrderBloodAnalysis(), new OrderUltraSoundScan(), new OrderXRay()};

    public OrderMedicalUI(DoctorController dc, WorldController wc) {
        this.doc = dc;
        this.wc = wc;
        this.med = new MedicalTestController(wc, dc);
    }

    public void run(Scanner sc) throws NotLoggedInException, NoOpenedPatientFileException {
        System.out.println("number: " + medicalTests.length);
        int chosenInt = UtilsUI.selectCommand(sc, medicalTests);
        if (chosenInt == 0) {
            return;
        }
        medicalTests[chosenInt-1].run(sc, med);
    }
}
