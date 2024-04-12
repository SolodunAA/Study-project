package diary.app.dao;

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
