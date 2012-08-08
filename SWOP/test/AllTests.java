import org.junit.runners.Suite;
import org.junit.runner.RunWith;

import Hospital.AdminTests.AddEquipmentTest;
import Hospital.AdminTests.AddStaffMemberTest;
import Hospital.Controllers.AvailableFactoriesTest;
import Hospital.Controllers.CheckInPatientTest;
import Hospital.Controllers.DiagnosisControllerTest;
import Hospital.Controllers.DischargePatientTest;
import Hospital.Controllers.EnterTreatmentResultControllerTest;
import Hospital.Controllers.LoginTest;
import Hospital.Controllers.NurseControllerTest;
import Hospital.Controllers.PatientFileTest;
import Hospital.Controllers.RegisterNewPatientTest;
import Hospital.Controllers.WorldControllerTest;
import Hospital.MedicalTest.BloodAnalysisResultTest;
import Hospital.MedicalTest.EnterMedicalTestResultTest;
import Hospital.MedicalTest.UltraSoundScanResultTest;
import Hospital.MedicalTest.XRayResultTest;
import Hospital.Schedules.AppointmentTest;
import Hospital.Schedules.ScheduleTest;

import Hospital.Treatments.CastTest;
import Hospital.Treatments.CastResultTest;
import Hospital.Treatments.MedicationFactoryTest;
import Hospital.Treatments.MedicationResultTest;
import Hospital.Treatments.SurgeryFactoryTest;
import Hospital.Treatments.SurgeryResultTest;
import Hospital.UtilsTest.EmptyNameTest;
import Hospital.World.TimeTest;
import Hospital.World.WorldTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	
	AddStaffMemberTest.class,
	AddEquipmentTest.class,
	
	CheckInPatientTest.class,
	DischargePatientTest.class,
	DiagnosisControllerTest.class,
	LoginTest.class,
	PatientFileTest.class,
	RegisterNewPatientTest.class,
	NurseControllerTest.class,
	AvailableFactoriesTest.class,
	WorldControllerTest.class,
	
	
	BloodAnalysisResultTest.class,
	UltraSoundScanResultTest.class,
	EnterMedicalTestResultTest.class,
	XRayResultTest.class,
	
	AppointmentTest.class,
	ScheduleTest.class,
	
	
	CastTest.class,
	CastResultTest.class,
	EnterTreatmentResultControllerTest.class,
	MedicationFactoryTest.class,
	MedicationResultTest.class,
	SurgeryFactoryTest.class,
	SurgeryResultTest.class,
	
	EmptyNameTest.class,

	WorldTest.class,
	TimeTest.class
})

public class AllTests {
	// Used for standalone test-suite running
	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("AllTests");
}
}

