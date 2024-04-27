package diary.app.service;

import diary.app.dao.UserRolesDao;
import diary.app.dto.Role;
import diary.app.out.ConsolePrinter;
import diary.app.auxiliaryfunctions.PasswordEncoder;
import diary.app.dao.LoginDao;

public class RegistrationServiceImpl implements RegistrationService {

    private final PasswordEncoder passwordEncoder;
    private final LoginDao loginDAO;
    private final UserRolesDao userRolesDao;

    public RegistrationServiceImpl(PasswordEncoder passwordEncoder,
                                   LoginDao loginDAO,
                                   UserRolesDao userRolesDao) {
        this.passwordEncoder = passwordEncoder;
        this.loginDAO = loginDAO;
        this.userRolesDao = userRolesDao;
    }

    @Override
    public boolean register(String login, String password) {
        boolean isAlreadyExists = loginDAO.checkIfUserExist(login);
        if (isAlreadyExists) {
            ConsolePrinter.print("Login already exists");
            return false;
        } else {
            int encodedPswd = passwordEncoder.encode(password);
            loginDAO.addNewUser(login, encodedPswd);
            Role role = Role.DEFAULT_USER;
            userRolesDao.addRoleForUser(login, role);
            ConsolePrinter.print("Successfully register");
            return true;
        }
    }
}
