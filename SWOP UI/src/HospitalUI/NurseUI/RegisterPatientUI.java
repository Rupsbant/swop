package HospitalUI.NurseUI;

import Hospital.Controllers.ArgumentList;
import Hospital.Exception.Arguments.WrongArgumentListException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import Hospital.Argument.PublicArgument;
import Hospital.Controllers.NurseController;
import Hospital.Controllers.PatientController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Scheduling.SchedulableAlreadyExistsException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.NotAFactoryException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Patient.PatientIsCheckedInException;
import Hospital.Exception.Scheduling.SchedulingException;
import Hospital.Exception.Warehouse.NotEnoughItemsAvailableException;
import Hospital.World.CampusInfo;
import HospitalUI.MainUI.UtilsUI;

public class RegisterPatientUI {

    NurseController nc;
    WorldController wc;
    PatientController pc;

    public RegisterPatientUI(NurseController nc, WorldController wc) throws ArgumentIsNullException {
        setNurseController(nc);
        setWorldController(wc);
        this.pc = new PatientController(wc, nc);
    }

    private void setWorldController(WorldController wc) throws ArgumentIsNullException {
        if (wc == null) {
            throw new ArgumentIsNullException("WorldController was null");
        }
        this.wc = wc;
    }

    private void setNurseController(NurseController nc) throws ArgumentIsNullException {
        if (nc == null) {
            throw new ArgumentIsNullException("NurseController was null");
        }
        this.nc = nc;
    }

    public void run(Scanner sc) throws NotLoggedInException {
        String[] regDoctors = nc.getDoctors(wc);
        if (regDoctors.length == 0) {
            System.out.println("No doctors available in the world.");
            return;
        }
        ArrayList<String> regPat = wc.getPatients();
        final String newPatient = "Enter a new Patient!";
        regPat.add(newPatient);
        String[] regPatients = regPat.toArray(new String[0]);
        int chosenInt = UtilsUI.selectCommand(sc, regPatients);
        if (chosenInt == 0) {
            return;
        }
        String chosenPatient;
        if (chosenInt == regPatients.length) {
            try {
                chosenPatient = createNewPatient(sc);
            } catch (SchedulableAlreadyExistsException e) {
                System.out.println("This patient already exists!");
                return;
            }
        } else {
            chosenPatient = regPatients[chosenInt - 1];
        }
        System.out.println("Select a doctor");
        chosenInt = UtilsUI.selectCommand(sc, regDoctors);
        String chosenDoctor = regDoctors[chosenInt - 1];
        try {
            String app = nc.checkIn(chosenPatient, chosenDoctor, wc);
            System.out.println("The patient is checked in and an appointment was made.");
            System.out.println(app);
        } catch (NoPersonWithNameAndRoleException e) {
            System.err.println("Couldnt find patient, this should not happen!");
            return;
        } catch (ArgumentIsNullException e) {
            System.err.println("The patient could not be registered, this interface is incorrectly initialised.");
            return;
        } catch (ArgumentConstraintException e) {
            System.err.println("An unexpected error has occured.\n This action could not be completed.\n\n"
                    + "Details:" + e.getMessage());
            return;
        } catch (SchedulingException e) {
            System.err.println("An unexpected error has occured.\n This action could not be completed.\n\n"
                    + "Details:" + e.getMessage());
            return;
        } catch (PatientIsCheckedInException e) {
            System.out.println("The patient is already checked in.");
            return;
        } catch (NotEnoughItemsAvailableException e) {
            System.out.println("There is not enough food available at this campus.");
            System.out.println("There is enough food availble at following campuses:");
            List<CampusInfo> otherCampuses = wc.getCampusesWithFood();
            for (CampusInfo campusInfo : otherCampuses) {
                System.out.println(campusInfo);
            }
            return;
        }
    }

    private String createNewPatient(Scanner sc) throws NotLoggedInException, SchedulableAlreadyExistsException {
        while (true) {
            try {
                System.out.println("Enter the name of the new patient: ");
                String name = sc.nextLine();
                String chosenPatient = pc.registerPatient(name);
                System.out.println("The patient is added:");
                System.out.println(chosenPatient);
                return chosenPatient;
            } catch (InvalidArgumentException ex) {
                System.out.println("The patients name cannot be empty");
            }
        }
    }
}
