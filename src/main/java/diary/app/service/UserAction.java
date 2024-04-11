package diary.app.service;

import java.util.Arrays;
import java.util.Optional;

public enum UserAction {
    SEE_USER_TRAININGS("see your trainings", "1"),
    EDIT_USER_TRAININGS("edit your trainings", "2"),
    SEE_OTHER_USERS_TRAININGS("see other users trainings", "3"),
    EDIT_OTHER_USERS_TRAININGS("edit other users trainings", "4"),
    CHANGE_APP_SETTINGS("manage app settings", "5"),
    CHANGE_USER_PERMISSIONS("manage user permissions", "6"),
    GET_AUDIT("get audit", "7"),
    EXIT("exit", "exit");

    private final String actionName;
    private final String actionAlias;

    UserAction(String actionName, String actionAlias) {
        this.actionName = actionName;
        this.actionAlias = actionAlias;
    }

    public String getActionDescriptionForUser() {
        return "Enter " + actionAlias + " to " + actionName;
    }

    public String getActionAlias() {
        return actionAlias;
    }

    public static Optional<UserAction> actionByAlias(String alias) {
        return Arrays.stream(UserAction.values())
                .filter(action -> action.actionAlias.equals(alias))
                .findAny();
    }
}
