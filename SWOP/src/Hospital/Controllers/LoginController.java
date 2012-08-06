package Hospital.Controllers;

import Hospital.SystemAPI;
import Hospital.Exception.NotLoggedInException;
import Hospital.People.Staff;

/**
 * This controller is created when a user logs in and will represent
 * this user for the duration of his/her session.
 *
 * @param <T> The type of user, must be a child of the Staff-class (Doctor,Nurse,...)
 */
@SystemAPI
public abstract class LoginController<T extends Staff> {

    /**
     * the user this LoginController represents
     */
    private T user;
    /**
     * indicates whether the user is currently logged in
     */
    private boolean loggedIn;
    /**
     * the campus where the session is happening
     */
    private CampusController campus;

    /**
     * Constructor
     * @param user The user this object will represent.
     * @param cc the Campus from where is logged in
     */
    @SystemAPI
    public LoginController(T user, CampusController cc) {
        this.user = user;
        this.campus = cc;
        loggedIn = true;
    }

    /**
     * Gets the user.
     * @return the user this LoginCController represents
     */
    T getUser() {
        return user;
    }

    /**
     * Returns the user's role.
     * @return The role of the user represented by this object.
     */
    @SystemAPI
    public String getRole() {
        return user.getRole();
    }

    /**
     * Logs this user out.
     */
    @SystemAPI
    public void logout() {
        loggedIn = false;
    }

    /**
     * Checks whether this user is logged in.
     * @return boolean indicating whether this user is logged in or not.
     */
    @SystemAPI
    public boolean loggedIn() {
        return loggedIn;
    }

    /**
     * Used to check whether a user is logged in.
     * @throws NotLoggedInException when the user is not logged in.
     */
    @SystemAPI
    public void checkLoggedIn() throws NotLoggedInException {
        if (!loggedIn()) {
            throw new NotLoggedInException();
        }
    }

    /**
     * @return the location of this user-session
     */
    CampusController getCampusController() {
        return campus;
    }
}
