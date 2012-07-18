package HospitalUI.DoctorUI;

import Hospital.Controllers.CommandInfo;
import java.util.Scanner;

import Hospital.Controllers.DoctorController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.Command.CannotDoException;
import HospitalUI.MainUI.UtilsUI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UndoPreviousActionUI {

    DoctorController dc;
    WorldController wc;

    public UndoPreviousActionUI(DoctorController dc, WorldController wc) {
        this.dc = dc;
        this.wc = wc;
    }

    public void run(Scanner sc) {
        String[] undoRedo = new String[]{"Undo", "Redo"};
        int selected = UtilsUI.selectCommand(sc, undoRedo);
        if (selected == 0) {
            return;
        }
        CommandInfo[] infos = null;
        if (selected == 1) {
            infos = dc.getRecentCommands();
        } else if (selected == 2) {
            infos = dc.getUndoneCommands();
        }

        int selected2 = UtilsUI.selectCommand(sc, infos);
        if (selected2 == 0) {
            return;
        }
        try {
            if (selected == 1) {
                System.out.println(dc.undoCommand(infos[selected2 - 1]));
            } else if (selected == 2) {
            	System.out.println("Redone:");
                System.out.println(dc.redoCommand(infos[selected2 - 1]));
            }
        } catch (CannotDoException ex) {
        	System.out.println("Cannot undo/redo this operation\n" + ex.getMessage());
        }
    }
}
