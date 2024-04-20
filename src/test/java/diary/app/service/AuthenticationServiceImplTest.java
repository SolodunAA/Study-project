package diary.app.service;

import diary.app.auxiliaryfunctions.HashEncoder;
import diary.app.auxiliaryfunctions.PasswordEncoder;
import diary.app.dao.AuditDao;
import diary.app.dao.UserRolesDao;
import diary.app.dao.inmemory.InMemoryAuditDao;
import diary.app.dao.inmemory.InMemoryLoginDao;
import diary.app.dao.LoginDao;
import diary.app.dao.inmemory.InMemoryRolesDao;
import diary.app.in.ConsoleReader;
import diary.app.in.Reader;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class AuthenticationServiceImplTest {
    ConsoleReader crMock = Mockito.mock(ConsoleReader.class);
    @Test
    public void authUnknownUserTest() {
        LoginDao loginDao = new InMemoryLoginDao();
        PasswordEncoder passwordEncoder = new HashEncoder();
        AuditDao auditDao = new InMemoryAuditDao();

        AuthenticationService authenticationService = new AuthenticationServiceImpl(loginDao, passwordEncoder, auditDao, crMock);

        String login = "user1";
        String password = "password1";
        Mockito.when(crMock.read()).thenReturn(login, password);

        assertNull(authenticationService.auth());
    }

    @Test
    public void authWrongPasswordTest() {
        LoginDao loginDao = new InMemoryLoginDao();
        PasswordEncoder passwordEncoder = new HashEncoder();
        AuditDao auditDao = new InMemoryAuditDao();
        AuthenticationService authenticationService = new AuthenticationServiceImpl(loginDao, passwordEncoder, auditDao, crMock);
        PasswordEncoder encoder = new HashEncoder();
        UserRolesDao userRolesDao = new InMemoryRolesDao();
        RegistrationService registrationService = new RegistrationServiceImpl(encoder, loginDao, userRolesDao, auditDao, crMock);

        String loginReg = "user";
        String passwordReg = "password";
        String passwordWrong ="1234";
        Mockito.when(crMock.read()).thenReturn(loginReg, passwordReg, loginReg, passwordWrong);

        registrationService.register();

        assertNull(authenticationService.auth());
    }

    @Test
    public void authTest() {
        LoginDao loginDao = new InMemoryLoginDao();
        PasswordEncoder passwordEncoder = new HashEncoder();
        AuditDao auditDao = new InMemoryAuditDao();

        AuthenticationService authenticationService = new AuthenticationServiceImpl(loginDao, passwordEncoder, auditDao, crMock);

        String login = "user";
        String password = "password";
        Mockito.when(crMock.read()).thenReturn(login, password);

        assertEquals(login, authenticationService.auth());
    }
}
