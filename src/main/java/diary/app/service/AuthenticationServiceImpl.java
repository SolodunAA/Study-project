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

    public AuthenticationServiceImpl(LoginDao loginDao,
                                     PasswordEncoder passwordEncoder) {
        this.loginDao = loginDao;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @param login
     * @param pswd
     * @return token
     */
    @Override
    public String auth(String login, String pswd) {
        boolean isUserExists = loginDao.checkIfUserExist(login);
        if (isUserExists) {
            int encodedPswd = passwordEncoder.encode(pswd);
            int savedEncodedPswd = loginDao.getEncodedPassword(login);
            if (encodedPswd == savedEncodedPswd) {
                ConsolePrinter.print("Login Successful");
                return login;
            } else {
                ConsolePrinter.print("Wrong login or password");
            }

        } else {
            ConsolePrinter.print("Wrong login or password");
        }
        return null;
    }
}
