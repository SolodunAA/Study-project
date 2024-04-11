package diary.app.dao;

import diary.app.dto.Role;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRolesDao implements UserRolesDao {

    private final Map<String, Role> rolesCache = new HashMap<>();

    @Override
    public void addRoleForUser(String login, Role role) {
        rolesCache.put(login, role);
    }

    @Override
    public Role getUserRole(String login) {
        return rolesCache.get(login);
    }
}
