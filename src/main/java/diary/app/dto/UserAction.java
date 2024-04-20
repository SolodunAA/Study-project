package diary.app.dto;

import java.util.Arrays;
import java.util.Optional;

/**
 * list of constants users and admin possible actions
 */
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

    /**
     * constructor
     * @param actionName - name of action
     * @param actionAlias - short action name
     */
    UserAction(String actionName, String actionAlias) {
        this.actionName = actionName;
        this.actionAlias = actionAlias;
    }

    /**
     * method describe user what he can do
     * @return string of description
     */
    public String getActionDescriptionForUser() {
        return "Enter " + actionAlias + " to " + actionName;
    }

    /**
     * get short action name
     * @return short action name
     */
    public String getActionAlias() {
        return actionAlias;
    }

    public static Optional<UserAction> actionByAlias(String alias) {
        return Arrays.stream(UserAction.values())
                .filter(action -> action.actionAlias.equals(alias))
                .findAny();
    }
}
