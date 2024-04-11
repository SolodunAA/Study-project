package diary.app.service;

import diary.app.dao.AuditDao;
import diary.app.dao.UserRolesDao;
import diary.app.dto.AuditItem;
import diary.app.dto.Role;
import diary.app.out.ConsolePrinter;
import diary.app.in.ConsoleReader;
import diary.app.auxiliaryfunctions.PasswordEncoder;
import diary.app.dao.LoginDao;

public class RegistrationServiceImpl implements RegistrationService {

    private final PasswordEncoder passwordEncoder;
    private final LoginDao loginDAO;
    private final UserRolesDao userRolesDao;
    private final AuditDao auditDao;

    public RegistrationServiceImpl(PasswordEncoder passwordEncoder,
                                   LoginDao loginDAO,
                                   UserRolesDao userRolesDao, AuditDao auditDao) {
        this.passwordEncoder = passwordEncoder;
        this.loginDAO = loginDAO;
        this.userRolesDao = userRolesDao;
        this.auditDao = auditDao;
    }

    @Override
    public void register() {
        ConsolePrinter.print("Enter login");
        String login = ConsoleReader.read();
        boolean isAlreadyExists = loginDAO.checkIfUserExist(login);
        if (isAlreadyExists) {
            auditDao.addAuditItem(new AuditItem(login, "tried to register again", login));
            ConsolePrinter.print("Login already exists");
        } else {
            ConsolePrinter.print("Enter password");
            String password = ConsoleReader.read();
            int encodedPswd = passwordEncoder.encode(password);
            loginDAO.addNewUser(login, encodedPswd);
            auditDao.addAuditItem(new AuditItem(login, "register", login));
            Role role = Role.DEFAULT_USER;
            userRolesDao.addRoleForUser(login, role);
            auditDao.addAuditItem(new AuditItem(login, "user received roles", role.toString()));
            ConsolePrinter.print("Successfully register");
        }
    }
}
