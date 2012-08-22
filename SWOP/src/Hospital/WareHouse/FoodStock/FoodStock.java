package Hospital.WareHouse.FoodStock;

import Hospital.WareHouse.Item;
import java.util.List;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Warehouse.NotEnoughItemsAvailableException;
import Hospital.Patient.Patient;
import Hospital.WareHouse.ItemReservation;
import Hospital.WareHouse.Items.Meal;
import Hospital.WareHouse.OrderPlacers.OrderPlacer;
import Hospital.WareHouse.Stock;
import Hospital.World.Campus;
import Hospital.World.Time;
import Hospital.World.TimeSubject;
import Hospital.World.TimeUtils;
import Hospital.World.World;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A stock of a Meal items
 */
public class FoodStock extends Stock<Meal> {

    /**
     * The world
     */
    private World world;
    /**
     * the campus on which this foodstock operates
     */
    private Campus campus;

    /**
     * Constructs a new stock with capacity maxStock and OrderPlacer o.
     * @param maxStock The capacity of the Stock.
     * @param o The OrderPlacer used to order new stock
     * @param prototype the prototype of items in this stock
     * @param timeSubject the timeSubject to observe for changes
     * @param world the world used for the ordering of new food
     * @param c the campus on which this foodstock operates
     * @throws ArgumentConstraintException If maxStock is negative or 0.
     * @throws ArgumentIsNullException if o is null, world is null or campus is null
     */
    public FoodStock(int maxStock, OrderPlacer o, Meal prototype, TimeSubject timeSubject, World world, Campus c) throws ArgumentConstraintException,
            ArgumentIsNullException {
        super(maxStock, o, prototype, timeSubject);
        if (world == null) {
            throw new ArgumentIsNullException("the given world was null");
        }
        if (c == null) {
            throw new ArgumentIsNullException("the given campus was null");
        }
        this.world = world;
        this.campus = c;
    }

    /**
     * @see Hospital.World.TimeObserver#timeUpdate(Hospital.World.Time)
     */
    @Override
    public void timeUpdate(Time newTime) {
        if (newTime == null) {
            return;
        }
        PriorityQueue<Event> events = new PriorityQueue<Event>();
        Time startOfDay = TimeUtils.getStartOfDay(getCurrentTime());
        events.add(new EndEvent(newTime));
        events.add(new EatEvent(this, startOfDay.getDiffTime(0, 0, 0, 8, 0)));
        events.add(new EatEvent(this, startOfDay.getDiffTime(0, 0, 0, 12, 0)));
        events.add(new EatEvent(this, startOfDay.getDiffTime(0, 0, 0, 18, 0)));
        events.add(new RestockEvent(this, startOfDay.getDiffTime(0, 0, 0, 23, 59)));
        while (true) {
            Event e = events.poll();
            if (e.compareTo(getCurrentTime()) < 0) { // ignore if earlier than original time
                continue;
            }
            if (e.compareTo(newTime) >= 0) { //stop if later than new time
                break;
            }
            super.timeUpdate(e.getTime());
            e.doWork(events);
        }
        super.timeUpdate(newTime);
        System.err.println(getFreeStock() +" meals over.");
    }

    /**
     * reserve and remove stock for every patient.
     */
    String patientsEat() {
        int nbPatients = getNumberOfPatients();
        try {
            ItemReservation reservation = reserveItem(nbPatients, getCurrentTime());
            List<Item> m = reservation.use();
            m.clear();
            String out = "#" + nbPatients + " were fed at " + getCurrentTime()+", "+getFreeStock()+" over.";
            return out;
        } catch (NotEnoughItemsAvailableException ex) {
            //Logger.getLogger(FoodStock.class.getName()).log(Level.SEVERE, "Not enough food available", ex);
        }
        final String out = "#" + nbPatients + " were not fed, not enough food available";
        return out;
    }

    public int getPatientCapacity() {
        return super.getFreeStock() / 6 - getNumberOfPatients();
    }

    /**
     * Gets the number of patients from the campus
     * @return the amount of patients checked into c
     */
    private int getNumberOfPatients() {
        List<Patient> temp = world.getResourceOfClass(Patient.class, campus);
        int count = 0;
        for (Patient p : temp) {
            if (!p.isDischarged()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Orders new items for the stock at Midnight
     */
    @Override
    protected void restock() {
        if ((getCurrentTime().getHour() == 23 && getCurrentTime().getMinute() == 59)) {
            super.restock();
        }
    }
}
