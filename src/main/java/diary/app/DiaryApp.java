package diary.app;

import diary.app.dao.UserRolesDao;
import diary.app.factory.DaoFactory;
import diary.app.factory.ServicesFactory;
import diary.app.in.Reader;
import diary.app.out.ConsolePrinter;
import diary.app.service.AuthenticationService;
import diary.app.service.RegistrationService;

public class DiaryApp {

    private final UserRolesDao userRolesDao;
    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;

    public DiaryApp(DaoFactory daoFactory, ServicesFactory servicesFactory) {
        this.userRolesDao = daoFactory.getUserRolesDao();
        this.registrationService = servicesFactory.getRegistrationService();
        this.authenticationService = servicesFactory.getAuthenticationService();
    }

    public void run() {
        ConsolePrinter.print("Hello! Welcome to our app!");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                runUserInteractions();
            } catch (Exception e) {
                ConsolePrinter.print("Exception happened. User logged out." + e.getMessage());
            }
        }
        ConsolePrinter.print("Shut down app");
    }

    private void runUserInteractions() {
       /* // print options
        ConsolePrinter.print("Print 1 if you want to register");
        ConsolePrinter.print("Print 2 if you want to login");
        ConsolePrinter.print("Print 3 if you want to stop server");
        // wait for option selection input
        String input = reader.read();
        // print what input is required
        // handle input
        switch (input) {
            case "1" -> registrationService.register();
            case "2" -> {
                String login = authenticationService.auth();
                if (login != null) {
                    Role role = userRolesDao.getUserRole(login);
                    userOffice.run(login, role);
                } else {
                    ConsolePrinter.print("Authentication failed. Try again.");
                }
            }
            case "3" -> Thread.currentThread().interrupt();
            default -> ConsolePrinter.print("Error, try again");
        }*/
    }

}
