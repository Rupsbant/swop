package Hospital.World;

import Hospital.Exception.Arguments.ArgumentIsNullException;

/**
 * An object that identifies a campus.
 */
public class CampusInfo {

    /**
     * the name of the represented campus
     */
    private String name;

    /**
     * Constructor
     * @param name  The name of the campus identified by this CampusInfo.
     * @throws ArgumentIsNullException when name is empty or when role is empty
     */
    public CampusInfo(String name) throws ArgumentIsNullException {
        if (name == null || name.equals("")) {
            throw new ArgumentIsNullException("Empty name");
        }
        this.name = name;
    }

    /**
     * Getter for the name.
     * @return The name of of the campus identified by this CampusInfo.
     */
    public String getName() {
        return name;
    }

    /**
     * A description of the campus.
     * @return "$name"
     */
    @Override
    public String toString() {
        return this.getName();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof CampusInfo) {
            CampusInfo c = (CampusInfo) o;
            return this.getName().equals(c.getName());
        }
        return false;
    }
}
