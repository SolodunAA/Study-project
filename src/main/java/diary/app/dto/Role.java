package diary.app.dto;

import diary.app.service.UserAction;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public final class Role {

    public static Role ADMIN = new Role(EnumSet.allOf(UserAction.class));
    public static Role DEFAULT_USER = new Role(EnumSet.of(UserAction.SEE_USER_TRAININGS, UserAction.EDIT_USER_TRAININGS));

    private final Set<UserAction> allowedActions;

    public Role(Set<UserAction> allowedActions) {
        Set<UserAction> allUserActions = new HashSet<>(allowedActions);
        allUserActions.add(UserAction.EXIT);
        this.allowedActions = EnumSet.copyOf(allUserActions);
    }

    public boolean isActionAllowed(UserAction action) {
        return allowedActions.contains(action);
    }

    public Set<UserAction> getAllowedActions() {
        return Set.copyOf(allowedActions);
    }

    @Override
    public String toString() {
        return "Role{" +
                "allowedActions=" + allowedActions +
                '}';
    }
}
