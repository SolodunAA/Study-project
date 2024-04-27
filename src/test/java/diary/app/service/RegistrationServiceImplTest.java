package diary.app.service;

import diary.app.auxiliaryfunctions.HashEncoder;
import diary.app.auxiliaryfunctions.PasswordEncoder;
import diary.app.dao.*;
import diary.app.dao.inmemory.InMemoryAuditDao;
import diary.app.dao.inmemory.InMemoryLoginDao;
import diary.app.dao.inmemory.InMemoryRolesDao;
import diary.app.in.ConsoleReader;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationServiceImplTest {

    private final ConsoleReader crMock = Mockito.mock(ConsoleReader.class);
    @Test
    public void registerTest() {
        String login = "login";
        String password = "password";
        PasswordEncoder encoder = new HashEncoder();
        LoginDao loginDao = new InMemoryLoginDao();
        UserRolesDao userRolesDao = new InMemoryRolesDao();
        AuditDao auditDao = new InMemoryAuditDao();
        RegistrationService registrationService = new RegistrationServiceImpl(encoder, loginDao, userRolesDao);


        registrationService.register(login, password);

        int encodedPassword = loginDao.getEncodedPassword(login);
        int expectedEncodedPassword = encoder.encode(password);
        assertThat(encodedPassword).isEqualTo(expectedEncodedPassword);
        assertThat(auditDao.getAuditItems(10).size()).isEqualTo(2);
    }


    @Test
    public void registerFailTest() {
        String login = "login";
        String password1 = "password1";
        String password2 = "password2";
        PasswordEncoder encoder = new HashEncoder();
        LoginDao loginDao = new InMemoryLoginDao();
        UserRolesDao userRolesDao = new InMemoryRolesDao();
        AuditDao auditDao = new InMemoryAuditDao();
        RegistrationService registrationService = new RegistrationServiceImpl(encoder, loginDao, userRolesDao);

        registrationService.register(login, password1);

        int encodedPassword1 = loginDao.getEncodedPassword(login);
        int expectedEncodedPassword1 = encoder.encode(password1);
        assertThat(encodedPassword1).isEqualTo(expectedEncodedPassword1);
        assertThat(auditDao.getAuditItems(10).size()).isEqualTo(2);

        registrationService.register(login, password2);

        int encodedPassword2 = loginDao.getEncodedPassword(login);
        int expectedEncodedPassword2 = encoder.encode(password2);
        assertThat(encodedPassword2).isNotEqualTo(expectedEncodedPassword2);
        assertThat(encodedPassword2).isEqualTo(expectedEncodedPassword1);
        assertThat(auditDao.getAuditItems(10).size()).isEqualTo(3);
    }

}
