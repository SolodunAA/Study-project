package diary.app.service;

import diary.app.auxiliaryfunctions.HashEncoder;
import diary.app.auxiliaryfunctions.PasswordEncoder;
import diary.app.dao.AuditDao;
import diary.app.dao.InMemoryAuditDao;
import diary.app.dao.InMemoryLoginDao;
import diary.app.dao.LoginDao;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AuthenticationServiceImplTest {

    @Test
    public void authUnknownUserTest() {
        LoginDao loginDao = new InMemoryLoginDao();
        PasswordEncoder passwordEncoder = new HashEncoder();
        AuditDao auditDao = new InMemoryAuditDao();

        AuthenticationService authenticationService = new AuthenticationServiceImpl(loginDao, passwordEncoder, auditDao, consoleReader, userRolesDao, userOffice, reader);

        assertFalse(authenticationService.auth("user", "password"));
    }

    @Test
    public void authWrongPasswordTest() {
        LoginDao loginDao = new InMemoryLoginDao();
        PasswordEncoder passwordEncoder = new HashEncoder();
        AuditDao auditDao = new InMemoryAuditDao();
        String user = "user";
        String password = "password";

        AuthenticationService authenticationService = new AuthenticationServiceImpl(loginDao, passwordEncoder, auditDao, consoleReader, userRolesDao, userOffice, reader);
        loginDao.addNewUser(user, passwordEncoder.encode(password));

        assertFalse(authenticationService.auth(user, "password" + "someth"));
    }

    @Test
    public void authTest() {
        LoginDao loginDao = new InMemoryLoginDao();
        PasswordEncoder passwordEncoder = new HashEncoder();
        AuditDao auditDao = new InMemoryAuditDao();
        String user = "user";
        String password = "password";

        AuthenticationService authenticationService = new AuthenticationServiceImpl(loginDao, passwordEncoder, auditDao, consoleReader, userRolesDao, userOffice, reader);
        loginDao.addNewUser(user, passwordEncoder.encode(password));

        assertTrue(authenticationService.auth(user, "password"));
    }
}
