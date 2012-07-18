package Hospital.Controllers;

import Hospital.Argument.Argument;
import Hospital.Argument.PublicArgument;
import Hospital.Argument.WorldPopulatedArgument;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.SystemAPI;
import Hospital.Utils;
import Hospital.World.World;
import java.util.List;

/**
 * This class wraps around a list of Arguments to provide safe access to the PublicArguments and not to the private Arguments.
 * @author SWOP-12
 */
@SystemAPI
public class ArgumentList {

    private Argument[] args;

    /**
     * Constructor of this list
     * @param args the list of arguments
     * @throws ArgumentIsNullException if args is null
     */
    public ArgumentList(Argument[] args) throws ArgumentIsNullException {
        if (args == null) {
            throw new ArgumentIsNullException("Can't make an ArgumentList with a null list");
        }
        this.args = args.clone();
    }

    /**
     * Returns the list of PublicArguments that need to be answered by de Users.
     * @return PublicArgument[], all PublicArguments this ArgumentList has
     */
    @SystemAPI
    public PublicArgument[] getPublicArguments() {
        List<PublicArgument> out = Utils.filter(Utils.toList(args), PublicArgument.class);
        return (PublicArgument[]) out.toArray(new PublicArgument[0]);
    }

    /**
     * Returns a clone of all the arguments in this list
     * @return the argument list
     */
    @SystemAPI
    Argument[] getAllArguments() {
        return this.args.clone();
    }

    /**
     * Populates all WorldPopulatedArguments in this list with this world.
     * @param w The world to populate with
     * @return this for chaining
     */
    ArgumentList setWorld(World w) {
        List<WorldPopulatedArgument> out = Utils.filter(Utils.toList(args), WorldPopulatedArgument.class);
        for (WorldPopulatedArgument e : out) {
            e.setWorld(w);
        }
        return this;
    }

    /**
     * Adds the given list of arguments to this list
     * @param args the arguments to add.
     * @return this
     */
    ArgumentList addArguments(Argument[] args) {
        if (args != null) {
            this.args = Utils.merge(this.args, args);
        }
        return this;
    }
}
