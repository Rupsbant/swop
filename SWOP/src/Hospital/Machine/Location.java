package Hospital.Machine;

/**
 * This class is a basic class that holds a location.
 */
public class Location {

    /**
     * The location held in this object.
     */
    String location;

    /**
     * Constructor 
     * @param location The location held in this object
     */
    public Location(String location) {
        this.location = location;
    }

    /**
     * Returns a representation of this location.
     * @return "Location: $location"
     */
    @Override
    public String toString() {
        return "Location: " + location;
    }
}
