package diary.app.service;

import diary.app.dto.UserAction;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class UserActionTest {

    @Test
    public void actionByAliasTest() {
        for (UserAction action : UserAction.values()) {
            String alias = action.getActionAlias();
            Optional<UserAction> foundAction = UserAction.actionByAlias(alias);
            assertThat(foundAction.isPresent()).isTrue();
            assertThat(foundAction.get()).isEqualTo(action);
        }

        Optional<UserAction> actionForUnknownAlias = UserAction.actionByAlias("unknown");
        assertThat(actionForUnknownAlias.isEmpty()).isTrue();
    }

    @Test
    public void allAliasesUnique() {
        Set<String> aliases = Arrays.stream(UserAction.values())
                .map(UserAction::getActionAlias)
                .collect(Collectors.toSet());

        assertThat(aliases.size()).isEqualTo(UserAction.values().length);
    }
}
