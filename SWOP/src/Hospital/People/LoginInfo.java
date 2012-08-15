package Hospital.People;

import Hospital.SystemAPI;
import Hospital.Exception.Arguments.ArgumentIsNullException;

/**
 * An object that identiefies a user that can log in.
 */
@SystemAPI
public class LoginInfo {

    /**
     * the name of the represented user
     */
    private String name;
    /**
     * the role of the represented user
     */
    private StaffRole role;

    /**
     * Constructor
     * @param name  The name of the person identified by this LoginInfo.
     * @param role  The role of this person.
     * @throws ArgumentIsNullException when name is empty or when role is empty
     */
    public LoginInfo(String name, StaffRole role) throws ArgumentIsNullException {
        if (name == null || name.equals("")) {
            throw new ArgumentIsNullException("Empty name");
        }
        if (role == null) {
            throw new ArgumentIsNullException("Empty role");
        }
        this.name = name;
        this.role = role;
    }

    /**
     * Getter for the name.
     * @return The name of of the person identified by this LoginInfo.
     */
    @SystemAPI
    public String getName() {
        return name;
    }

    /**
     * Getter for the role.
     * @return The role of of the person identified by this LoginInfo.
     */
    @SystemAPI
    public StaffRole getRole() {
        return role;
    }

    /**
     * A description of the person to log in.
     * @return "$name, $role"
     */
    @Override
    @SystemAPI
    public String toString() {
        return this.getName() + ", " + this.getRole();
    }
}
