package diary.app.dao.postgres;

import diary.app.dao.LoginDao;

import java.sql.*;

public class PostgresLoginDao implements LoginDao {
    private static final String LOGIN_EXISTS = """
            SELECT count(*)
            FROM :SCHEMA.login_table
            WHERE login = ?
            """;
    private static final String INSERT_LOGIN = """
            INSERT INTO :SCHEMA.login_table
            (login) VALUES (?)
            """;
    private static final String INSERT_LOGIN_AND_PASSWORD = """
            INSERT INTO :SCHEMA.login_password_table
            (login_id, encoded_password) VALUES ((SELECT login_id FROM :SCHEMA.login_table WHERE login = ?),?)
            """;
    private static final String SELECT_PASSWORD = """
            SELECT encoded_password
            FROM :SCHEMA.login_password_table
            WHERE login_id = (SELECT login_id FROM :SCHEMA.login_table WHERE login = ?)
            """;
    private final String url;
    private final String userName;
    private final String password;
    private final String schema;

    public PostgresLoginDao(String url, String userName, String password, String schema) {
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.schema = schema;
    }

    @Override
    public boolean checkIfUserExist(String login) {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = SqlUtils.createPreparedStatement(connection, LOGIN_EXISTS, schema)) {
            ps.setString(1, login);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addNewUser(String login, int encodedPswd) {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps1 = SqlUtils.createPreparedStatement(connection, INSERT_LOGIN, schema);
             PreparedStatement ps2 = SqlUtils.createPreparedStatement(connection, INSERT_LOGIN_AND_PASSWORD, schema)) {
            ps1.setString(1, login);
            ps1.executeUpdate();
            ps2.setString(1, login);
            ps2.setInt(2, encodedPswd);
            ps2.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getEncodedPassword(String login) {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = SqlUtils.createPreparedStatement(connection, SELECT_PASSWORD, schema)) {
            ps.setString(1, login);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getInt("encoded_password");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
