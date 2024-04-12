package diary.app.dto;

import diary.app.dto.Role;
import diary.app.out.ConsolePrinter;
import diary.app.service.UserAction;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Set;

public class RoleTest {
    @Test
    public void isActionAllowedTest() {
        UserAction userAction1 = UserAction.SEE_USER_TRAININGS;
        UserAction userAction2 = UserAction.EDIT_USER_TRAININGS;
        UserAction notAllowedAction = UserAction.SEE_OTHER_USERS_TRAININGS;
        Role role = new Role(Set.of(userAction1, userAction2));
        assertFalse(role.isActionAllowed(notAllowedAction));
        assertTrue(role.isActionAllowed(userAction1));
        assertTrue(role.isActionAllowed(userAction2));
    }
    @Test
    public void getAllowedActionsTest(){
        UserAction userAction1 = UserAction.SEE_USER_TRAININGS;
        UserAction userAction2 = UserAction.EDIT_USER_TRAININGS;
        Role role = new Role(Set.of(userAction1, userAction2));
        Set<UserAction> expectedAllowedActions = Set.of(userAction1, userAction2, UserAction.EXIT);
        assertEquals(expectedAllowedActions, role.getAllowedActions());
    }
}
