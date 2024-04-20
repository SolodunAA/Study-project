package diary.app.dao;

import diary.app.dto.Role;

/**
 * working with roles
 */
public interface UserRolesDao {
    /**
     * Add user role to the storage
     * @param login - login of user
     * @param role - user role
     */
    void addRoleForUser(String login, Role role);

    /**
     * Show user role
     * @param login - login of user
     * @return - user role
     */
    Role getUserRole(String login);
}
