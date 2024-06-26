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
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(authenticationService.auth()).isNull();
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

        assertThat(authenticationService.auth()).isNull();
    }

    @Test
    public void authTest() {
        String login = "user";
        String password = "password";
        LoginDao loginDao = new InMemoryLoginDao();
        PasswordEncoder passwordEncoder = new HashEncoder();
        loginDao.addNewUser(login, passwordEncoder.encode(password));
        AuditDao auditDao = new InMemoryAuditDao();

        AuthenticationService authenticationService = new AuthenticationServiceImpl(loginDao, passwordEncoder, auditDao, crMock);

        Mockito.when(crMock.read()).thenReturn(login, password);

        assertThat(authenticationService.auth()).isEqualTo(login);
    }
}
