package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.NotLoggedInException;
import Hospital.Exception.Warehouse.OrderUnavailableException;
import Hospital.Exception.Warehouse.StockException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.WareHouse.Item;
import Hospital.WareHouse.Order;
import Hospital.WareHouse.Warehouse;
import Hospital.WareHouse.WarehouseManager;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This controller handles the login of a warehousemanager.
 * StaffController and MachineController extends the functions to the usecases.
 */
@SystemAPI
public class WareHouseController extends LoginController<WarehouseManager> {

    /**
     * defines the maximum amount of orders to show when asking for an order history
     */
    @SystemAPI
    public static final int ORDERS_TO_SHOW = 20;

    /**
     * When logging in this controller gets made from a StaffMember
     * @param cc the Campus from where is logged in
     * @param user The staffmember to log in.
     */
    @SystemAPI
    public WareHouseController(WarehouseManager user, CampusController cc) {
        super(user, cc);
    }

    /**
     * Returns a list of the different stock categories
     * @return a list of strings representing the stock categories
     * @throws NotLoggedInException The Warehousemanager is not logged in
     */
    @SystemAPI
    public Set<String> getStockCategories() throws NotLoggedInException {
        checkLoggedIn();
        Warehouse w = this.getCampusController().getCampus().getWarehouse();
        Set<String> types = w.getStockNames();
        return types;
    }

    /**
     * Returns the arguments needed to process the order. This is the expiration date if needed.
     * @param order the order.
     * @return a list of arguments that are needed to process the order. an empty list in case there are no arguments needed.
     * @throws NotLoggedInException The Warehousemanager is not logged in.
     * @throws OrderUnavailableException the order was not found in this warehouse
     * @throws StockException this order was not in the correct warehouse
     */
    @SystemAPI
    public ArgumentList getOrderArguments(String order) throws NotLoggedInException, OrderUnavailableException, StockException {
        checkLoggedIn();
        Order o = getOrder(order);
        try {
            return new ArgumentList(this.getCampusController().getCampus().getWarehouse().getOrderArguments(o));
        } catch (ArgumentIsNullException ex) {
            Logger.getLogger(WareHouseController.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error("getOrderArguments should not be null");
        }
    }

    /**
     * Find an order
     * @param order the string identifying the order
     * @return the order identified by the combination of order and type
     * @throws NotLoggedInException the warehouse manager is not logged in
     * @throws OrderUnavailableException the order was not found in this warehouse
     */
    private Order getOrder(String order) throws NotLoggedInException, OrderUnavailableException {
        checkLoggedIn();
        Warehouse w = this.getCampusController().getCampus().getWarehouse();
        List<Order> list = w.getOrdersByType(Item.class);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).toString().equals(order)) {
                return list.get(i);
            }
        }
        throw new OrderUnavailableException("Order not found: " + order);
    }

    /**
     * This method will process the order. It will create the items and add them to the warehouse.
     * @param order the order to process.
     * @param arg the arguments needed to process the order. Use the method getOrderArguments to get the correct arguments.
     * @return a string detailing the processed order
     * @throws OrderUnavailableException the order o is not registered in the system.
     * @throws StockException the stock was full
     * @throws WrongArgumentListException one of the arguments in arg is wrong
     * @throws NotLoggedInException The Warehousemanager is not logged in.
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    @SystemAPI
    public String processOrder(String order, ArgumentList arg) throws
            OrderUnavailableException,
            StockException,
            WrongArgumentListException,
            NotLoggedInException,
            InvalidArgumentException {
        checkLoggedIn();
        Order o = getOrder(order);
        Warehouse w = this.getCampusController().getCampus().getWarehouse();
        w.processOrder(o, arg.getAllArguments());
        return "Order was processed.";
    }

    /**
     * Gets the latest ORDERS_TO_SHOW orders of a given type
     * @param stockName the type of order to search
     * @return an array of strings containing the details of the last 20 orders of the given type
     * @throws NotLoggedInException the warehousemanager is not logged in
     */
    @SystemAPI
    public String[] getLatestOrders(String stockName) throws NotLoggedInException {
        checkLoggedIn();
        return getNumberOfOrders(stockName, ORDERS_TO_SHOW, false);
    }

    /**
     * Gets all orders in the given category
     * @param category the category of orders to search
     * @return all orders in the given category as an array of strings
     * @throws NotLoggedInException the warehouse manager is not logged in
     */
    @SystemAPI
    public String[] getArrivedOrders(String category) throws NotLoggedInException {
        checkLoggedIn();
        return getNumberOfOrders(category, Integer.MAX_VALUE, true);
    }

    /**
     * Gets the last <i>count</i> orders of the given type of stock
     * @param stockName the name of the stock to search
     * @param count the amount of orders to return
     * @param onlyArrivedOrders makes the function only return orders that have already arrived
     * @return an array of strings containing the descriptions of orders
     */
    private String[] getNumberOfOrders(String stockName, int count, boolean onlyArrivedOrders) {
        try {
            Warehouse w = this.getCampusController().getCampus().getWarehouse();
            List<Order> list = w.getStock(stockName).getOrderList().getOrders();
            if (onlyArrivedOrders) {
                list = w.getStock(stockName).getOrderList().getArrivedOrders();
            }
            count = Math.min(list.size(), count);
            String[] result = new String[count];
            for (int i = 1; i <= count; i++) {
                result[i - 1] = list.get(list.size() - i).toString();
            }
            return result;
        } catch (StockException ex) {
            return new String[0];
        }
    }
}
