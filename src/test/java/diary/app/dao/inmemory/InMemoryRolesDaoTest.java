package diary.app.dao.inmemory;

import diary.app.dao.UserRolesDao;
import diary.app.dto.Role;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryRolesDaoTest {
    @Test
    public void addAndGetRoleForUserTest(){
        UserRolesDao userRolesDao = new InMemoryRolesDao();
        String user = "user";
        Role role = Role.DEFAULT_USER;
        userRolesDao.addRoleForUser(user, role);

        assertThat(userRolesDao.getUserRole(user)).isEqualTo(role);
    }

    @Test
    public void getUnknownUserRole() {
        UserRolesDao userRolesDao = new InMemoryRolesDao();
        assertThat(userRolesDao.getUserRole("user")).isNull();
    }
}
