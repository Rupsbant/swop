/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Hospital.Schedules.ConstraintSolver;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.Scheduling.SchedulingException;

/**
 *
 * @author Rupsbant
 */
public class BruteForceSolverTest extends AppointmentConstraintSolverTestDummy {

    public BruteForceSolverTest() throws NoPersonWithNameAndRoleException, ArgumentConstraintException {
    }

    @Override
    public void setUp() throws ArgumentIsNullException, CannotDoException, SchedulingException, ArgumentConstraintException {
        instance = new BruteForceSolver();
        super.setUp();
    }
}
