package HospitalUI.WareHouseUI;

import Hospital.Argument.PublicArgument;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import Hospital.Controllers.WareHouseController;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Warehouse.OrderUnavailableException;
import Hospital.Exception.Warehouse.StockException;
import Hospital.World.Time;
import HospitalUI.MainUI.UtilsUI;

public class FillStockUI {

    WareHouseController wh;

    public FillStockUI(WareHouseController wh) {
        this.wh = wh;
    }

    public void run(Scanner sc) throws NotLoggedInException {
        String[] cat = wh.getStockCategories().toArray(new String[0]);
        int chosenCat = UtilsUI.selectCommand(sc, cat);
        if (chosenCat == 0) {
            return;
        }
        String categorie = cat[chosenCat - 1];
        String[] orders = wh.getArrivedOrders(categorie);
        if (orders.length == 0) {
            System.out.println("No orders pending! Aborting!");
            return;
        }
        int chosenOrder = UtilsUI.selectCommand(sc, orders);
        if (chosenOrder == 0) {
            System.out.println("You selected 0, aborting!");
            return;
        }
        String order = orders[chosenOrder - 1];
        Time time = null;
        
        try {
            PublicArgument[] args = wh.getOrderArguments(order);
            UtilsUI.answerArguments(sc, args);
            String ans = wh.processOrder(order, args);
            System.out.println(ans);
        } catch (InvalidArgumentException ex) {
            Logger.getLogger(FillStockUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (StockException ex) {
            Logger.getLogger(WareHouseUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OrderUnavailableException ex) {
            System.out.println("Entered order was not found!, this should not happen!");
        }

    }
}
