package diary.app.dao.postgres;

import diary.app.dao.UserRolesDao;
import diary.app.dto.Role;
import diary.app.dto.UserAction;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class PostgresUserRolesDao implements UserRolesDao {

    private final String url;
    private final String userName;
    private final String password;
    private final String schema;

    private final static String INSERT_USER_ROLE_SQL = "INSERT INTO :SCHEMA.\"UserRoleTable\" " +
            "(login, " +
            "see_user_trainings, " +
            "edit_user_trainings, " +
            "see_other_user_trainings, " +
            "edit_other_user_trainings, " +
            "change_app_settings, " +
            "change_user_permission, " +
            "get_audit, " +
            "exit) " +
            "VALUES (?,?,?,?,?,?,?,?,?)";
    private final static String RETRIEVE_USER_ROLE_SQL = "SELECT " +
            "see_user_trainings, " +
            "edit_user_trainings, " +
            "see_other_user_trainings, " +
            "edit_other_user_trainings, " +
            "change_app_settings, " +
            "change_user_permission, " +
            "get_audit, " +
            "exit " +
            "FROM :SCHEMA.\"UserRoleTable\" " +
            "WHERE login = ?";

    public PostgresUserRolesDao(String url, String userName, String password, String schema) {
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.schema = schema;
    }

    @Override
    public void addRoleForUser(String login, Role role) {
        try (Connection connection = DriverManager.getConnection(url, userName, password)) {
            PreparedStatement ps = SqlUtils.createPreparedStatement(connection, INSERT_USER_ROLE_SQL, schema);
            ps.setString(1, login);
            ps.setBoolean(2, role.getAllowedActions().contains(UserAction.SEE_USER_TRAININGS));
            ps.setBoolean(3, role.getAllowedActions().contains(UserAction.EDIT_USER_TRAININGS));
            ps.setBoolean(4, role.getAllowedActions().contains(UserAction.SEE_OTHER_USERS_TRAININGS));
            ps.setBoolean(5, role.getAllowedActions().contains(UserAction.EDIT_OTHER_USERS_TRAININGS));
            ps.setBoolean(6, role.getAllowedActions().contains(UserAction.CHANGE_APP_SETTINGS));
            ps.setBoolean(7, role.getAllowedActions().contains(UserAction.CHANGE_USER_PERMISSIONS));
            ps.setBoolean(8, role.getAllowedActions().contains(UserAction.GET_AUDIT));
            ps.setBoolean(9, role.getAllowedActions().contains(UserAction.EXIT));
            ps.executeUpdate();
        } catch (Exception e) {
            //System.out.println("");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Role getUserRole(String login) {
        Set<UserAction> set = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, userName, password)) {
            PreparedStatement ps = SqlUtils.createPreparedStatement(connection, RETRIEVE_USER_ROLE_SQL, schema);;
            ps.setString(1, login);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            if (resultSet.getBoolean("see_user_trainings")) set.add(UserAction.SEE_USER_TRAININGS);
            if (resultSet.getBoolean("edit_user_trainings")) set.add(UserAction.EDIT_USER_TRAININGS);
            if (resultSet.getBoolean("see_other_user_trainings")) set.add(UserAction.SEE_OTHER_USERS_TRAININGS);
            if (resultSet.getBoolean("edit_other_user_trainings")) set.add(UserAction.EDIT_OTHER_USERS_TRAININGS);
            if (resultSet.getBoolean("change_app_settings")) set.add(UserAction.CHANGE_APP_SETTINGS);
            if (resultSet.getBoolean("change_user_permission")) set.add(UserAction.CHANGE_USER_PERMISSIONS);
            if (resultSet.getBoolean("get_audit")) set.add(UserAction.GET_AUDIT);
            if (resultSet.getBoolean("exit")) set.add(UserAction.EXIT);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new Role(set);
    }
}
