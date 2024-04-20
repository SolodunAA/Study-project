package diary.app.service;

import diary.app.auxiliaryfunctions.PasswordEncoder;
import diary.app.dao.AuditDao;
import diary.app.dao.LoginDao;
import diary.app.dao.UserRolesDao;
import diary.app.dto.AuditItem;
import diary.app.in.ConsoleReader;
import diary.app.in.Reader;
import diary.app.out.ConsolePrinter;

public class AuthenticationServiceImpl implements AuthenticationService {
    private final LoginDao loginDao;
    private final PasswordEncoder passwordEncoder;
    private final AuditDao auditDao;
    private final Reader reader;



    public AuthenticationServiceImpl(LoginDao loginDao,
                                     PasswordEncoder passwordEncoder,
                                     AuditDao auditDao, Reader reader) {
        this.loginDao = loginDao;
        this.passwordEncoder = passwordEncoder;
        this.auditDao = auditDao;
        this.reader = reader;
    }

    @Override
    public String auth() {
        ConsolePrinter.print("Enter login");
        String login = reader.read();
        ConsolePrinter.print("Enter password");
        String pswd = reader.read();
        boolean isUserExists = loginDao.checkIfUserExist(login);
        if (isUserExists) {
            int encodedPswd = passwordEncoder.encode(pswd);
            int savedEncodedPswd = loginDao.getEncodedPassword(login);
            if (encodedPswd == savedEncodedPswd) {
                auditDao.addAuditItem(new AuditItem(login, "logged in", login));
                ConsolePrinter.print("Login Successful");
                return login;
            } else {
                ConsolePrinter.print("Wrong login or password");
            }

        } else {
            auditDao.addAuditItem(new AuditItem(login, "not existing user failed to login.", login));
            ConsolePrinter.print("Wrong login or password");
        }
        return null;
    }
}
