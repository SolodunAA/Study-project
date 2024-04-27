package diary.app.dto;

import org.junit.Test;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleTest {
    @Test
    public void isActionAllowedTest() {
        UserAction userAction1 = UserAction.SEE_USER_TRAININGS;
        UserAction userAction2 = UserAction.EDIT_USER_TRAININGS;
        UserAction notAllowedAction = UserAction.SEE_OTHER_USERS_TRAININGS;
        Role role = new Role(Set.of(userAction1, userAction2));
        assertThat(role.isActionAllowed(notAllowedAction)).isFalse();
        assertThat(role.isActionAllowed(userAction1)).isTrue();
        assertThat(role.isActionAllowed(userAction2)).isTrue();
    }
    @Test
    public void getAllowedActionsTest(){
        UserAction userAction1 = UserAction.SEE_USER_TRAININGS;
        UserAction userAction2 = UserAction.EDIT_USER_TRAININGS;
        Role role = new Role(Set.of(userAction1, userAction2));
        Set<UserAction> expectedAllowedActions = Set.of(userAction1, userAction2, UserAction.EXIT);
        assertThat(role.getAllowedActions()).isEqualTo(expectedAllowedActions);
    }
}
