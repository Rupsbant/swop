package Hospital.WareHouse;

import Hospital.Argument.PublicArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Warehouse.OrderUnavailableException;
import Hospital.Exception.Warehouse.StockException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A warehouse keeps the stocks of the Items in the hospital
 */
public class Warehouse {

    /**
     * the List of stocks in this warehouse
     */
    private ArrayList<Stock> stocks;

    /**
     * Constructor
     */
    public Warehouse() {
        stocks = new ArrayList<Stock>();

    }

    public void addStock(Stock st) {
        stocks.add(st);
    }

    /**
     * Processes an order for a stock in this warehouse
     * @param order the order to process
     * @param args the arguments to process this stock with
     * @throws StockException the stock which processed this order was full
     * @throws WrongArgumentListException the arguments didn't meet the requirements for the item for which this order was made
     * @throws InvalidArgumentException thrown if the list or one of the arguments is null, or if the answer does not satisfy the constraints.
     */
    public void processOrder(Order order, PublicArgument[] args) throws StockException, WrongArgumentListException, InvalidArgumentException {
        if (order == null) {
            throw new ArgumentIsNullException("The order is null");
        }
        try {
            for (Stock stock : stocks) {
                stock.processOrder(order, args);
            }
        } catch (OrderUnavailableException oue) {
            ;//all but the stock for which this order was made will throw this exception
        }
    }

    /**
     * Gets a specific stock
     * @param name the name of the stock to search for
     * @return the Stock with the given name
     * @throws StockNotFoundException there was not stock with the given name in this warehouse
     */
    public Stock getStock(String name) throws StockException {
        for (Stock st : stocks) {
            if (st.getName().equals(name)) {
                return st;
            }
        }
        throw new StockException("Stock not found: " + name);
    }

    /**
     * Gets the names of all the stocks in this warehouse
     * @return a set of strings containing the names of stocks in this warehouse
     */
    public Set<String> getStockNames() {
        Set<String> result = new HashSet<String>();
        for (int i = 0; i < stocks.size(); i++) {
            result.add(stocks.get(i).getName());
        }
        return result;
    }

    /**
     * Gets the stocks which holds items of a given class
     * @param clazz the item-class
     * @return a list of stocks which hold items of this class
     */
    public <I extends Item> List<Stock<I>> getStockByType(Class<I> clazz) {
        List<Stock<I>> out = new ArrayList<Stock<I>>();
        for (Stock element : stocks) {
            if (element.holdsClass(clazz)) {
                out.add((Stock<I>) element);
            }
        }
        return out;
    }

    /**
     * Gets the names of the stocks which holds items of a given class
     * @param clazz the item-class
     * @return a list strings which contain the name of stocks which hold items of this class
     */
    public <I extends Item> List<String> getStockNamesByType(Class<I> clazz) {
        List<String> out = new ArrayList<String>();
        for (Stock element : stocks) {
            if (element.holdsClass(clazz)) {
                out.add(element.getName());
            }
        }
        return out;
    }

    /**
     * Gets all the orders for a given type of item
     * @param clazz the type of item to search orders for
     * @return a list of orders
     */
    public <I extends Item> List<Order> getOrdersByType(Class<I> clazz) {
        List<Stock<I>> stocks = getStockByType(clazz);
        List<Order> orders = new ArrayList<Order>();
        for (Stock st : stocks) {
            orders.addAll(st.getOrderList().getOrders());
        }
        return orders;
    }

    /**
     * Gets the stock for which this order was made
     * @param o the order
     * @return the stock for which this order was made
     * @throws StockException there was no stock in this warehouse which holds this order
     */
    private Stock getStockOfOrder(Order o) throws StockException {
        for (Stock st : stocks) {
            if (st.getOrderList().getOrders().contains(o)) {
                return st;
            }
        }
        throw new StockException();
    }

    /**
     * Gets the arguments needed to process this order
     * @param o the order to process
     * @return an array of PublicArguments which, when answered, can be used for processing this order
     * @throws StockNotFoundException there was no stock in this warehouse which holds this order
     */
    public PublicArgument[] getOrderArguments(Order o) throws StockException {
        return getStockOfOrder(o).getArguments();
    }
}
