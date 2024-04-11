package diary.app.dao;

import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryLoginDaoTest {

    @Test
    public void checkIfUserExistTest() {
        LoginDao loginDao = new InMemoryLoginDao();

        loginDao.addNewUser("user1", 12);

        assertTrue(loginDao.checkIfUserExist("user1"));
        assertFalse(loginDao.checkIfUserExist("user2"));
    }

    @Test
    public void getEncodedPasswordTest() {
        LoginDao loginDao = new InMemoryLoginDao();

        loginDao.addNewUser("user1", 12);
        assertEquals(12, loginDao.getEncodedPassword("user1"));
        assertThrows(NullPointerException.class, () -> loginDao.getEncodedPassword("user2"));
    }

}
