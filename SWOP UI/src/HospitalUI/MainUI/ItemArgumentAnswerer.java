package HospitalUI.MainUI;

import Hospital.Argument.ItemArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Exception.CannotChangeException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemArgumentAnswerer implements ArgumentAnswerer {

    public static final ItemArgumentAnswerer singleton = new ItemArgumentAnswerer();

    public void answer(PublicArgument arg, Scanner sc) {
        String answer = "";
        String[] medItems = ((ItemArgument) arg).getPossibleAnswers();
        int itemInt = -1;
        while (itemInt != 0) {
            System.out.println("Select a medication item to add, if you're done, enter 0!");
            itemInt = UtilsUI.selectCommand(sc, medItems);
            if (itemInt > 0) {
                answer = answer + ", " + medItems[itemInt - 1];
            }
        }
        sc.nextLine();
        try {
            arg.enterAnswer(answer);
        } catch (CannotChangeException ex) {
            Logger.getLogger(ItemArgumentAnswerer.class.getName()).log(Level.SEVERE, "Cannot happen, this is legal", ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ItemArgumentAnswerer.class.getName()).log(Level.SEVERE, "Cannot happen, this is legal", ex);
        }
    }
}
