package diary.app.service;

import diary.app.auxiliaryfunctions.PasswordEncoder;
import diary.app.dao.AuditDao;
import diary.app.dao.LoginDao;
import diary.app.dto.AuditItem;

public class AuthenticationServiceImpl implements AuthenticationService {
    private final LoginDao loginDao;
    private final PasswordEncoder passwordEncoder;
    private final AuditDao auditDao;

    public AuthenticationServiceImpl(LoginDao loginDao,
                                     PasswordEncoder passwordEncoder,
                                     AuditDao auditDao) {
        this.loginDao = loginDao;
        this.passwordEncoder = passwordEncoder;
        this.auditDao = auditDao;
    }

    @Override
    public boolean auth(String login, String pswd) {
        boolean isUserExists = loginDao.checkIfUserExist(login);
        if (!isUserExists) {
            auditDao.addAuditItem(new AuditItem(login, "not existing user failed to login.", login));
            return false;
        } else {
            int encodedPswd = passwordEncoder.encode(pswd);
            int savedEncodedPswd = loginDao.getEncodedPassword(login);
            return encodedPswd == savedEncodedPswd;
        }
    }
}
