package HospitalUI.WareHouseUI;

import java.util.Scanner;

import Hospital.Controllers.WareHouseController;
import Hospital.Controllers.WorldController;
import Hospital.Exception.NotLoggedInException;
import HospitalUI.MainUI.UtilsUI;

public class ListOrdersUI {
	
	WareHouseController wh;

	public ListOrdersUI(WareHouseController wh) {
		this.wh = wh;
	}

	public void run(Scanner sc) throws NotLoggedInException {
        String[] cat = wh.getStockCategories().toArray(new String[0]);
        int chosenCat = UtilsUI.selectCommand(sc, cat);
        if (chosenCat == 0) {
            return;
        }
        String categorie = cat[chosenCat - 1];
        String[] orders = wh.getLatestOrders(categorie);
        if (orders.length == 0) {
            System.out.println("There are no placed orders of the selected categorie that have not arrived already!");
            return;
        }
        System.out.println("Latest 20 placed orders that have not arrived already:");
        for (int i = 0; i < orders.length; i++) {
            System.out.println(orders[i]);
        }
    }
	
}
