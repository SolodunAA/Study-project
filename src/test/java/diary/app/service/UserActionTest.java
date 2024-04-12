package diary.app.service;

import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class UserActionTest {

    @Test
    public void actionByAliasTest() {
        for (UserAction action : UserAction.values()) {
            String alias = action.getActionAlias();
            Optional<UserAction> foundAction = UserAction.actionByAlias(alias);
            assertTrue(foundAction.isPresent());
            assertEquals(action, foundAction.get());
        }

        Optional<UserAction> actionForUnknownAlias = UserAction.actionByAlias("unknown");
        assertTrue(actionForUnknownAlias.isEmpty());
    }

    @Test
    public void allAliasesUnique() {
        Set<String> aliases = Arrays.stream(UserAction.values())
                .map(UserAction::getActionAlias)
                .collect(Collectors.toSet());

        assertEquals(UserAction.values().length, aliases.size());
    }
}
