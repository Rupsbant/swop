package HospitalUI.MainUI;

import Hospital.Argument.PublicArgument;
import Hospital.Exception.CannotChangeException;
import java.util.Scanner;

public class BasicAnswerer implements ArgumentAnswerer {
    public static final BasicAnswerer singleton = new BasicAnswerer();

    public void answer(PublicArgument arg, Scanner sc) {
        System.out.println("\n\n");
        while (true) {
            System.out.println(arg.getQuestion());
            String answer = sc.nextLine();
            try {
                arg.enterAnswer(answer);
                System.out.println("Entered answer : " + arg.getAnswer());
                break;
            } catch (CannotChangeException e) {
                System.out.println("Argument was already answered, skipping!");
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Input was wrong, try again.");
            }
        }
    }
}
