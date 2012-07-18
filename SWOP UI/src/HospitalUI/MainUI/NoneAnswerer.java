package HospitalUI.MainUI;

import Hospital.Argument.PublicArgument;
import java.util.Scanner;

public class NoneAnswerer implements ArgumentAnswerer {

    public static final NoneAnswerer singleton = new NoneAnswerer();

    public void answer(PublicArgument arg, Scanner sc) {
        return;
    }
}
