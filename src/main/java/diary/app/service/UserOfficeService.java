package diary.app.service;

import diary.app.dao.AuditDao;
import diary.app.dto.AuditItem;
import diary.app.dto.Role;
import diary.app.dto.UserAction;
import diary.app.in.Reader;
import diary.app.out.ConsolePrinter;

public class UserOfficeService {

    private final DefaultUserOfficeService defaultUserOfficeService;
    private final AdminOfficeService adminOfficeService;
    private final Reader reader;
    private final AuditDao auditDao;

    public UserOfficeService(DefaultUserOfficeService defaultUserOfficeService, AdminOfficeService adminOfficeService, Reader reader, AuditDao auditDao) {
        this.defaultUserOfficeService = defaultUserOfficeService;
        this.adminOfficeService = adminOfficeService;
        this.reader = reader;
        this.auditDao = auditDao;
    }


    public void run(String login, Role role) {
        ConsolePrinter.print("Welcome " + login + " to your personal account!");
        ConsolePrinter.print("Let's start!");
        boolean exit = false;
        while (!exit) {
            var allowedActions = role.getAllowedActions();
            allowedActions.forEach(action -> ConsolePrinter.print(action.getActionDescriptionForUser()));
            String userAnswer = reader.read();
            var actionOptional = UserAction.actionByAlias(userAnswer);

            if (actionOptional.isEmpty() || !role.isActionAllowed(actionOptional.get())) {
                ConsolePrinter.print("Unknown action. Please try again");
            } else {
                switch (actionOptional.get()) {

                    case SEE_USER_TRAININGS -> defaultUserOfficeService.seeUserTrainings(login);
                    case EDIT_USER_TRAININGS -> defaultUserOfficeService.editUserTrainings(login);
                    case SEE_OTHER_USERS_TRAININGS -> {
                        ConsolePrinter.print("Enter username to see trainings");
                        adminOfficeService.seeUserTrainings(login, reader.read());
                    }
                    case EDIT_OTHER_USERS_TRAININGS -> {
                        ConsolePrinter.print("Enter username to edit trainings");
                        adminOfficeService.editUserTrainings(login, reader.read());
                    }
                    case CHANGE_APP_SETTINGS -> adminOfficeService.changeAppSettings(login);
                    case CHANGE_USER_PERMISSIONS -> adminOfficeService.changeUserPermissions(login);
                    case GET_AUDIT -> adminOfficeService.getAudit(login);
                    case EXIT -> exit = true;
                    default -> ConsolePrinter.print("Error. Check actions and try again.");
                }
            }
        }
        auditDao.addAuditItem(new AuditItem(login, "EXIT", "exit"));
    }
}
