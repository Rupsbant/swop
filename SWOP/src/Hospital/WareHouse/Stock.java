package Hospital.WareHouse;

import Hospital.Argument.PublicArgument;
import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Arguments.ArgumentNotAnsweredException;
import Hospital.Exception.Arguments.InvalidArgumentException;
import Hospital.Exception.Warehouse.ItemNotReservedException;
import Hospital.Exception.Warehouse.StockException;
import Hospital.Exception.Warehouse.NotEnoughItemsAvailableException;
import Hospital.Exception.Warehouse.OrderUnavailableException;
import Hospital.Exception.Arguments.WrongArgumentListException;
import Hospital.WareHouse.ItemQueues.FIFOQueue;
import Hospital.WareHouse.ItemQueues.ItemQueue;
import Hospital.WareHouse.OrderPlacers.OrderPlacer;
import Hospital.World.Time;
import Hospital.World.TimeObserver;
import Hospital.World.TimeSubject;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A stock of a specific type of items
 * @param <I> the type of items in this stock
 */
public class Stock<I extends Item> implements TimeObserver {

    /**
     * The maximum amount of items that can be stored
     */
    private final int maxStock;
    /**
     * list of reservations made in this stock
     */
    private TreeSet<ItemReservation> reservations = new TreeSet<ItemReservation>();
    /**
     * A prototype of the items in this stock
     */
    private I prototype;
    /**
     * automatically places the orders for this stock
     */
    private OrderPlacer orderPlacer;
    /**
     * the items in this stock
     */
    private ItemQueue<I> items = new FIFOQueue<I>();
    /**
     * the orders for new items in this stock
     */
    protected OrderList orderList = new OrderList();
    /**
     * the observers to which this stock is subject
     */
    private ArrayList<StockObserver> observers = new ArrayList<StockObserver>();
    /**
     * The current time. As said by the world...
     */
    private Time currentTime;

    /**
     * Constructs a new stock with capacity maxStock and OrderPlacer o.
     * @param maxStock The capacity of the Stock.
     * @param o The OrderPlacer used to order new stock
     * @param prototype the prototype of items in this stock
     * @param timeSubject the timeSubject to observe for changes
     * @throws ArgumentConstraintException If maxStock is negative or 0.
     * @throws ArgumentIsNullException if o is null
     */
    protected Stock(int maxStock, OrderPlacer o, I prototype, TimeSubject timeSubject) throws ArgumentConstraintException, ArgumentIsNullException {
        if (!(maxStock > 0)) {
            throw new ArgumentConstraintException("The new stock needs a capacity larger than 0.");
        }
        if (o == null) {
            throw new ArgumentIsNullException();
        }
        this.prototype = prototype;
        this.maxStock = maxStock;
        this.orderPlacer = o;
        timeSubject.attachTimeObserver(this);
        this.currentTime = timeSubject.getTime();
        fillStock();
    }

    /**
     * Fill this stock to max capacity
     */
    private void fillStock() {
        try {
            Order o = orderList.placeOrder(getAvailableSpace(), new Time(0, 0, 0, 0, 0));
            processOrder(o, new PublicArgument[0]);
        } catch (WrongArgumentListException ex) {
            Logger.getLogger(Stock.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidArgumentException ex) {
            Logger.getLogger(Stock.class.getName()).log(Level.SEVERE, null, ex);
        } catch (StockException ex) {
            Logger.getLogger(Stock.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OrderUnavailableException ex) {
            Logger.getLogger(Stock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the capacity of this stock
     */
    public int getMaxStock() {
        return maxStock;
    }

    int getReserved() {
        int reserved = 0;
        for (ItemReservation r : reservations) {
            reserved += r.getItemsReserved();
        }
        return reserved;
    }

    /**
     * Returns the amount of available items.
     * @return Returns the total number of items in the stock, reserved or not.
     */
    int getStockCount() {
        return items.size();
    }

    /**
     * Returns the number of unreserved items.
     * @return The amount of items that are contained in this Stock and are not reserved.
     */
    public int getFreeStock() {
        return getStockCount() - getReserved();
    }

    /**
     * Returns the number of items that can be added to the Stock.
     * @return The number of free spots to place items.
     */
    private int getAvailableSpace() {
        return getMaxStock() - getStockCount();
    }

    /**
     * This method is used to remove a reserved item from the stock.
     * @return a reserved item
     * @throws ItemNotReservedException If the item is not reserved
     */
    public ArrayList<I> getItems(ItemReservation reservation) throws ItemNotReservedException {
        if (!reservations.remove(reservation)) {
            throw new ItemNotReservedException("The item reservation is not available in this stock");
        }
        int count = reservation.getItemsReserved();
        ArrayList<I> i = new ArrayList<I>();
        for (int j = 0; j < count; j++) {
            i.add(items.get());
        }
        restock();
        return i;
    }

    /**
     * Reserves count items.
     * @param count the amount of items to reserve
     * @throws NotEnoughItemsAvailableException there were not enough non-reserved items in this stock at the given time
     */
    public ItemReservation reserveItem(int count, Time execTime) throws NotEnoughItemsAvailableException {
        if (!testReservation(count, execTime)) {
            throw new NotEnoughItemsAvailableException();
        }
        ItemReservation reservation = new ItemReservation(count, execTime, this);
        reservations.add(reservation);
        if (currentTime.compareTo(execTime) >= 0) {
            try {
                reservation.removeFromStock();
            } catch (ItemNotReservedException ex) {
                throw new Error(ex);
            }
        }
        return reservation;
    }

    /**
     * Removes the reservation from an amount of reserved items.
     * @param count the amount of items to de-reserve
     * @throws ItemNotReservedException there were not as many items reserved as were given
     */
    public void removeReservation(ItemReservation reservation) throws ItemNotReservedException {
        if (!reservations.remove(reservation)) {
            throw new ItemNotReservedException("The item reservation is not availble in this stock");
        }
        notifyObservers();
    }

    public boolean testReservation(int count, Time execTime) {
        PriorityQueue<StockChangeEvent> events = new PriorityQueue<StockChangeEvent>();
        for (ItemReservation i : reservations) {
            events.add(i);
        }
        int order = 0;
        for (Order o : getOrderList().getOrders()) {
            if(o.compareTo(execTime)<0){ // don't calculate with backorders, Warehousemanager should do his job
                continue;
            }
            events.add(o);
            order += o.getAmount();
        }
        events.add(new ItemReservation(count, execTime, this));
        //events.addAll(items.getEventList()); // doesn't work fully
        int items = getStockCount();
        return StockConsistency.evaluateEvents(events, items, order, getMaxStock(), getOrderPlacer());
    }

    /**
     * Orders new items for the stock given the current state of the stock
     */
    protected void restock() {
        try {
            final int stockCount = getStockCount();
            int toOrder = orderPlacer.checkStock(stockCount, getOrderedCount(), getMaxStock());
            if (toOrder != 0) {
                orderList.placeOrder(toOrder, currentTime);
                System.err.println("Ordered : " + toOrder+" on "+currentTime);
            }
        } catch (ArgumentConstraintException ex) {
            Logger.getLogger(Stock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected OrderPlacer getOrderPlacer() {
        return orderPlacer;
    }

    /**
     * Add an item to the stock
     * @param item the item to add
     * @throws ArgumentIsNullException the item is null.
     * @throws StockException there is no space available in the stock.
     */
    private void addItem(I item) throws ArgumentIsNullException, StockException {
        if (item == null) {
            throw new ArgumentIsNullException("The item to add should not be null.");
        }
        if (getAvailableSpace() <= 0) {
            throw new StockException();
        }
        items.add(item);
        notifyObservers();
    }

    /**
     * Attaches a new StockObserver to this subject
     * @param o the observer to attach
     * @throws ArgumentIsNullException the given observer was null
     */
    public void attach(StockObserver o) throws ArgumentIsNullException {
        if (o == null) {
            throw new ArgumentIsNullException("The observer should not be null");
        }
        observers.add(o);
    }

    /**
     * Detaches a given StockObserver from this subject
     * @param o the observer to detach
     */
    public void detach(StockObserver o) {
        observers.remove(o);
    }

    /**
     * Notify all observers of a change in this stock
     */
    void notifyObservers() {
        for (StockObserver o : observers) {
            o.stockUpdate();
        }
    }

    /**
     * Gets the name of this stock
     * @return the name as string
     */
    public String getName() {
        return prototype.getName();
    }

    /**
     * Gets how many items are ordered from the stock provider for this stock
     * @return the amount of items ordered
     */
    private int getOrderedCount() {
        return orderList.totalItemsOrdered();
    }

    /**
     * Processes a given order when it arrives
     * @param order the order to process
     * @param args the arguments needed to process this order (eg expiration date)
     * @throws NoSpaceAvailableException the stock is full
     * @throws OrderUnavailableException the order was not made for this stock
     * @throws WrongArgumentListException the arguments did not match the requirements for this type of item
     * @throws ArgumentNotAnsweredException one or more of the given arguments was not answered
     */
    void processOrder(Order order, PublicArgument[] args) throws StockException,
            OrderUnavailableException, WrongArgumentListException, InvalidArgumentException {
        orderList.remove(order);
        try {
            for (int i = 0; i < order.getAmount(); i++) {
                I newItem = (I) prototype.clone(args);
                addItem(newItem);
            }
        } catch (ArgumentIsNullException ex) {
            throw new Error("Clone was null!");
        }
    }

    /**
     * @see Hospital.World.TimeObserver#timeUpdate(Hospital.World.Time)
     */
    public void timeUpdate(Time newTime) {
        currentTime = newTime;
        items.timeUpdate(newTime);
        orderList.updateTime(newTime);
        updateTimeReservations();
        restock();
    }

    /**
     * Indicates whether this stock holds items of the given class
     * @param clazz the class to check
     * @return true if this stock holds items of class <i>clazz</i>
     */
    public boolean holdsClass(Class clazz) {
        return clazz.isInstance(prototype);
    }

    /**
     * Gets the list of outstanding orders for this stock
     * @return an OrderList-object containing orders for this stock
     */
    public OrderList getOrderList() {
        return orderList;
    }

    /**
     * Gets the arguments needed for processing orders for this stock
     * @return an array of PublicArguments which, when answered, can be used with processOrder()
     */
    PublicArgument[] getArguments() {
        return prototype.getArguments();
    }

    private void updateTimeReservations() {
        for (ItemReservation r : reservations) {
            if (r.compareTo(currentTime) <= 0) {
                try {
                    r.removeFromStock();
                } catch (ItemNotReservedException ex) {
                    Logger.getLogger(Stock.class.getName()).log(Level.SEVERE, "If the item is in the list, it was RESERVED!", ex);
                }
            }
        }
    }

    protected Time getCurrentTime() {
        return currentTime;
    }
}
