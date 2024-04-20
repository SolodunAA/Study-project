package diary.app.dao.inmemory;

import diary.app.dao.UserRolesDao;
import diary.app.dao.inmemory.InMemoryRolesDao;
import diary.app.dto.Role;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemoryRolesDaoTest {
    @Test
    public void addAndGetRoleForUserTest(){
        UserRolesDao userRolesDao = new InMemoryRolesDao();
        String user = "user";
        Role role = Role.DEFAULT_USER;
        userRolesDao.addRoleForUser(user, role);

        assertEquals(role, userRolesDao.getUserRole(user));
    }

    @Test
    public void getUnknownUserRole() {
        UserRolesDao userRolesDao = new InMemoryRolesDao();
        assertNull(userRolesDao.getUserRole("user"));
    }
}
