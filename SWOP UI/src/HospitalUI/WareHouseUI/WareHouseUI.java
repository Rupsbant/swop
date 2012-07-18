package HospitalUI.WareHouseUI;

import java.util.Scanner;

import Hospital.Controllers.WareHouseController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.NotLoggedInException;
import HospitalUI.MainUI.UtilsUI;

public class WareHouseUI {

    WareHouseController wh;

    public WareHouseUI(WareHouseController wh) {
        this.wh = wh;
    }

    public void run(Scanner sc) throws NotLoggedInException {
        int pos = -1;
        while (pos != 0) {
            String[] commands = new String[]{
                "Fill Stock in Warehouse",
                "List Orders"
            };
            pos = UtilsUI.selectCommand(sc, commands);
            switch (pos) {
                case 1:
                	FillStockUI fillstock = new FillStockUI(wh);
                    fillstock.run(sc);
                    break;
                case 2:
                	ListOrdersUI listorders = new ListOrdersUI(wh);
                    listorders.run(sc);
                    break;
            }
        }
        wh.logout();
    }
    
}
