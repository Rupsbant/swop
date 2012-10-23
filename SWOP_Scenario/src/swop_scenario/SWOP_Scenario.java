package swop_scenario;

import Hospital.Controllers.WorldController;
import HospitalUI.MainUI.MainUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SWOP_Scenario {

    private PrintStream fromUI_p;
    private PrintStream toUI_p;
    private Scanner toUI_sc;
    private final Scanner fromUI_sc;
    private final PrintStream orig_p;
    private final Scanner orig_sc;
    private final PipedInputStream fromUI_out;

    public SWOP_Scenario() throws IOException {
        fromUI_out = new PipedInputStream();
        fromUI_sc = new Scanner(fromUI_out);
        PipedOutputStream fromUI_in = new PipedOutputStream(fromUI_out);
        fromUI_p = new PrintStream(fromUI_in);

        orig_sc = new Scanner(System.in);
        orig_p = System.out;
    }

    public void run() throws FileNotFoundException, IOException {
        File f = null;
        do {
            if (f != null) {
                startWorld();
                runFile(f);
            }
            f = selectFile();
        } while (f != null);
    }

    public static void main(String[] args) throws IOException {
        SWOP_Scenario sw = new SWOP_Scenario();
        sw.run();
    }

    private File selectFile() {
        File f = new File("./scenario/");
        File[] files = f.listFiles();
        int chosen = selectCommand(orig_sc, files);
        if (chosen <= 0) {
            return null;
        }
        return files[chosen - 1];

    }

    private void runFile(File f) throws FileNotFoundException, IOException {
        Scanner filesSC = new Scanner(f);
        while (filesSC.hasNextLine()) {
            String line = filesSC.nextLine();
            switch (line.charAt(0)) {
                case '#':
                    handleComment(line);
                    break;
                case '<':
                    handleCode(line);
                    break;
            }
        }
    }

    private void handleComment(String line) {
        if (line.length() < 2) {
            orig_p.println(line);
            return;
        }
        orig_p.println(line);
        if (line.charAt(1) != '#') {
            orig_p.println("#Press enter to continue.");
            orig_sc.nextLine();
        }
    }

    private void handleCode(String line) throws IOException {
        String[] commands = line.substring(1, line.length() - 1).split(",");
        for (String str : commands) {
            toUI_p.println(str);
            orig_p.println("<" + str);
        }
        sleep(1000);
        String abc = "";
        ArrayList<String> groups = new ArrayList();
        while (fromUI_out.available() > 0) {
            char next = (char) fromUI_out.read();
            switch (next) {
                case '\r':
                    fromUI_out.read();
                case '\n':
                    String matches = handleMatcher(abc);
                    if (matches != null) {
                        groups.add(matches);
                    }
                    orig_p.println(abc);
                    abc = "";
                    break;
                default:
                    abc += next;
            }
        }
        System.out.println(abc);
        sleep(10);
        for (String str : groups) {
            System.err.println(str);
        }
        sleep(10);
    }

    private void sleep(long a) {
        try {
            Thread.sleep(a);
        } catch (InterruptedException ex) {
        }
    }

    private int selectCommand(Scanner sc, Object[] options) {
        if (options.length == 0) {
            orig_p.println("No options available, aborting!");
            return 0;
        }
        while (true) {
            orig_p.println("Enter the number of a command");
            orig_p.println("0 to exit");
            for (int i = 0; i < options.length; i++) {
                orig_p.println((i + 1) + ": " + options[i]);
            }
            try {
                int ans = sc.nextInt();
                if (ans >= 0 && ans <= options.length) {
                    sc.nextLine();
                    return ans;
                }
            } catch (InputMismatchException e) {
                orig_p.println("Enter a valid number!");
                sc.next();
            }
        }
    }

    private String handleMatcher(String line) {
        Pattern p = Pattern.compile("Appointment[^.]*attendees.");
        Matcher matcher = p.matcher(line);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private void startWorld() throws IOException {
        PipedInputStream toUI_in = new PipedInputStream();
        toUI_sc = new Scanner(toUI_in);
        final PipedOutputStream toUI_out = new PipedOutputStream(toUI_in);
        toUI_p = new PrintStream(toUI_out);
        System.setOut(fromUI_p);
        new Thread() {
            @Override
            public void run() {
                MainUI m = new MainUI(WorldController.getBasicWorld());
                //MainUI m = new MainUI(BasicWorld.getEmptyBasicWorld());
                m.run(toUI_sc);
            }
        }.start();
    }
}
