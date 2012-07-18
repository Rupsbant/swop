package HospitalUI.MainUI;

import Hospital.Argument.ItemArgument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.NoneArgument;
import java.util.HashMap;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class contains utility functions used throughout the user interface. 
 */
public class UtilsUI {

    private static final HashMap<Class, ArgumentAnswerer> map = getMap();

    /**
     * Lets the user answer the given PublicArguments
     * @param sc The scanner that takes input from the user
     * @param args The PublicArguments to be anwered
     */
    public static void answerArguments(Scanner sc, PublicArgument[] args) {
        if (args.length == 0) {
            return;
        }
        sc.nextLine();
        for (PublicArgument arg : args) {
            if (map.get(arg.getClass()) == null) {
                BasicAnswerer.singleton.answer(arg, sc);
            } else {
                map.get(arg.getClass()).answer(arg, sc);
            }
        }
    }

    /**
     * Selects a valid option from a given array.
     * @param sc The scanner that takes input from the user.
     * @param options The possible options to be presented.
     * @return The array-index of the chosen option.
     */
    public static int selectCommand(Scanner sc, Object[] options) {
        if (options.length == 0) {
            System.out.println("No options available, aborting!");
            return 0;
        }
        while (true) {
            System.out.println("Enter the number of a command");
            System.out.println("0 to exit");
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ": " + options[i]);
            }
            try {
                int ans = sc.nextInt();
                if (ans >= 0 && ans <= options.length) {
                    return ans;
                }
            } catch (InputMismatchException e) {
                System.out.println("Enter a valid number!");
                sc.next();
            }
        }
    }

    private static HashMap<Class, ArgumentAnswerer> getMap() {
        HashMap<Class, ArgumentAnswerer> out = new HashMap<Class, ArgumentAnswerer>();
        out.put(NoneArgument.class, NoneAnswerer.singleton);
        out.put(ItemArgument.class, ItemArgumentAnswerer.singleton);
        return out;
    }

    public static void space() {
        for (int i = 0; i < 5; i++) {
            System.out.println();
        }
    }
}
