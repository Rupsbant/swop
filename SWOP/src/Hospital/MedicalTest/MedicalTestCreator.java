package Hospital.MedicalTest;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;

public class MedicalTestCreator {

    public static final MedicalTestCreator SINGLETON = new MedicalTestCreator();

    private MedicalTestCreator() {
    }

    public MedicalTest makeBloodAnalysis(String focus, int numberOfAnalyses) throws ArgumentConstraintException {
        MedicalTest med = new BloodAnalysis(focus, numberOfAnalyses);
        return med;
    }

    public MedicalTest makeUltraSoundScan(String focus, boolean recordVideo, boolean recordImages) throws ArgumentIsNullException {
        MedicalTest med = new UltraSoundScan(focus, recordVideo, recordImages);
        return med;
    }

    public MedicalTest makeXRayScan(String bodyPart, int zoom, int numberOfImages) throws ArgumentConstraintException {
        MedicalTest med = new XRayScan(zoom, numberOfImages, bodyPart);
        return med;
    }
}
