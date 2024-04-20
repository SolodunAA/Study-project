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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
        RegistrationService registrationService = new RegistrationServiceImpl(encoder, loginDao, userRolesDao, auditDao, crMock);

        Mockito.when(crMock.read()).thenReturn(login, password);

        registrationService.register();

        int encodedPassword = loginDao.getEncodedPassword(login);
        int expectedEncodedPassword = encoder.encode(password);
        assertEquals(expectedEncodedPassword, encodedPassword);
        assertEquals(2, auditDao.getAuditItems(10).size());
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
        RegistrationService registrationService = new RegistrationServiceImpl(encoder, loginDao, userRolesDao, auditDao, crMock);


        //successfully register
        Mockito.when(crMock.read()).thenReturn(login, password1);
        registrationService.register();

        int encodedPassword1 = loginDao.getEncodedPassword(login);
        int expectedEncodedPassword1 = encoder.encode(password1);
        assertEquals(expectedEncodedPassword1, encodedPassword1);
        assertEquals(2, auditDao.getAuditItems(10).size());

        //fail to register with same login
        Mockito.when(crMock.read()).thenReturn(login, password2);
        registrationService.register();

        int encodedPassword2 = loginDao.getEncodedPassword(login);
        int expectedEncodedPassword2 = encoder.encode(password2);
        assertNotEquals(expectedEncodedPassword2, encodedPassword2);
        assertEquals(expectedEncodedPassword1, encodedPassword2);
        assertEquals(3, auditDao.getAuditItems(10).size());
    }

}
