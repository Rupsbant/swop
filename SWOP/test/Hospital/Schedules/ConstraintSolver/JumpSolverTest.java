package Hospital.Schedules.ConstraintSolver;

import Hospital.Exception.Arguments.ArgumentConstraintException;
import Hospital.Exception.Arguments.ArgumentIsNullException;
import Hospital.Exception.Command.CannotDoException;
import Hospital.Exception.NoPersonWithNameAndRoleException;
import Hospital.Exception.Scheduling.SchedulingException;

public class JumpSolverTest extends AppointmentConstraintSolverTestDummy {

    public JumpSolverTest() throws NoPersonWithNameAndRoleException, ArgumentConstraintException {
    }

    @Override
    public void setUp() throws ArgumentIsNullException, CannotDoException, SchedulingException, ArgumentConstraintException {
        instance = new JumpSolver();
        super.setUp();
    }
    
    
}
