package Hospital.World;

/**
 * Provides an interface to compare a lot of f
 */
public interface HasTime extends Comparable<HasTime> {

    /**
     * returns the current Time to compare with
     * @return the current time this object has
     */
    Time getTime();
}
