package diary.app.dao.inmemory;

import diary.app.dao.LoginDao;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InMemoryLoginDaoTest {

    @Test
    public void checkIfUserExistTest() {
        LoginDao loginDao = new InMemoryLoginDao();

        loginDao.addNewUser("user1", 12);

        assertThat(loginDao.checkIfUserExist("user1")).isTrue();
        assertThat(loginDao.checkIfUserExist("user2")).isFalse();
    }

    @Test
    public void getEncodedPasswordTest() {
        LoginDao loginDao = new InMemoryLoginDao();

        loginDao.addNewUser("user1", 12);
        assertThat(loginDao.getEncodedPassword("user1")).isEqualTo(12);
        assertThatThrownBy(() -> loginDao.getEncodedPassword("user2")).isExactlyInstanceOf(NullPointerException.class);
    }

}
