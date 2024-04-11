package diary.app;

import diary.app.dao.AuditDao;
import diary.app.dao.UserRolesDao;
import diary.app.dto.AuditItem;
import diary.app.dto.Role;
import diary.app.in.ConsoleReader;
import diary.app.out.ConsolePrinter;
import diary.app.service.AuthenticationService;
import diary.app.service.RegistrationService;
import diary.app.service.UserOfficeServise;

public class DiaryApp {

    private final UserRolesDao userRolesDao;
    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final UserOfficeServise userOffice;
    private final AuditDao auditDao;

    public DiaryApp(UserRolesDao userRolesDao,
                    RegistrationService registrationService,
                    AuthenticationService authenticationService,
                    UserOfficeServise userOffice,
                    AuditDao auditDao) {
        this.userRolesDao = userRolesDao;
        this.registrationService = registrationService;
        this.authenticationService = authenticationService;
        this.userOffice = userOffice;
        this.auditDao = auditDao;
    }

    public void run() {
        ConsolePrinter.print("Hello! Welcome to our app!");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                runUserInteractions();
            } catch (Exception e) {
                ConsolePrinter.print("exception happened. User logged out." + e.getMessage());
            }
        }
        ConsolePrinter.print("shut down app");
    }

    private void runUserInteractions() {
        // print options
        ConsolePrinter.print("Print 1 if you want to register");
        ConsolePrinter.print("Print 2 if you want to login");
        ConsolePrinter.print("Print 3 if you want to stop server");
        // wait for option selection input
        String input = ConsoleReader.read();
        // print what input is required
        //handle input
        switch (input) {
            case "1" -> registrationService.register();
            case "2" -> {
                ConsolePrinter.print("Enter login");
                String login = ConsoleReader.read();
                ConsolePrinter.print("Enter password");
                String pswd = ConsoleReader.read();
                boolean isValidUser = authenticationService.auth(login, pswd);
                if (isValidUser) {
                    auditDao.addAuditItem(new AuditItem(login, "logged in", login));
                    Role role = userRolesDao.getUserRole(login);
                    ConsolePrinter.print("Login Successful");
                    userOffice.run(login, role);
                } else {
                    auditDao.addAuditItem(new AuditItem(login, "failed to logg in", login));
                    ConsolePrinter.print("Failed to login");
                }
            }
            case "3" -> Thread.currentThread().interrupt();
            default -> ConsolePrinter.print("error, try again");
        }
    }

}
