package diary.app.dao;

import diary.app.dto.Role;

import java.util.HashMap;
import java.util.Map;

/**
 * class stores users roles
 */
public class InMemoryRolesDao implements UserRolesDao {
    /**
     * map stores user login and it roles
     */
    private final Map<String, Role> rolesCache = new HashMap<>();

    /**
     *
     * @param login - user name
     * @param role - user role in the system
     */
    @Override
    public void addRoleForUser(String login, Role role) {
        rolesCache.put(login, role);
    }

    /**
     *
     * @param login user role in the system
     * @return user role
     */

    @Override
    public Role getUserRole(String login) {
        return rolesCache.get(login);
    }
}
