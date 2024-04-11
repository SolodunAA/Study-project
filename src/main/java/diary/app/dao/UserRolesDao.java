package diary.app.dao;

import diary.app.dto.Role;

public interface UserRolesDao {
    void addRoleForUser(String login, Role role);
    Role getUserRole(String login);
}
