package diary.app.dto;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * store each user acceptable actions
 */
public final class Role {
    /**
     * definition of admin actions
     */
    public static Role ADMIN = new Role(EnumSet.allOf(UserAction.class));
    /**
     * definition of user default actions
     */
    public static Role DEFAULT_USER = new Role(EnumSet.of(UserAction.SEE_USER_TRAININGS, UserAction.EDIT_USER_TRAININGS));
    /**
     * set of available actions
     */
    private final Set<UserAction> allowedActions;

    /**
     * constructor of role objects
     * @param allowedActions action which can do user
     */
    public Role(Set<UserAction> allowedActions) {
        Set<UserAction> allUserActions = new HashSet<>(allowedActions);
        allUserActions.add(UserAction.EXIT);
        this.allowedActions = EnumSet.copyOf(allUserActions);
    }

    /**
     * checking the action availability
     * @param action - checking action availability
     * @return true - if this action allowed
     */

    public boolean isActionAllowed(UserAction action) {
        return allowedActions.contains(action);
    }

    /**
     * @return set of all available actions
     */
    public Set<UserAction> getAllowedActions() {
        return Set.copyOf(allowedActions);
    }

    /**
     * @return string representation
     */
    @Override
    public String toString() {
        return "Role{" +
                "allowedActions=" + allowedActions +
                '}';
    }
}
