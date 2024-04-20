package diary.app.dao.postgres;

import diary.app.dao.LoginDao;

import java.sql.*;

public class PostgresLoginDao implements LoginDao {
    private static final String SELECT_COUNT_PASSWORD =
            "SELECT count(*) " +
                    "FROM :SCHEMA.\"LoginPasswordTable\" " +
                    "WHERE login = ?";
    private static final String INSERT_LOGIN_AND_PASSWORD =
            "INSERT INTO :SCHEMA.\"LoginPasswordTable\" " +
                    "(login, \"encodedPassword\") VALUES (?,?)";
    private static final String SELECT_PASSWORD =
            "SELECT \"encodedPassword\" " +
                    "FROM :SCHEMA.\"LoginPasswordTable\" " +
                    "WHERE login = ?";
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
        try (Connection connection = DriverManager.getConnection(url, userName, password)) {
            PreparedStatement ps = SqlUtils.createPreparedStatement(connection, SELECT_COUNT_PASSWORD, schema);
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
        try (Connection connection = DriverManager.getConnection(url, userName, password)) {
            PreparedStatement ps = SqlUtils.createPreparedStatement(connection, INSERT_LOGIN_AND_PASSWORD, schema);
            ps.setString(1, login);
            ps.setInt(2, encodedPswd);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getEncodedPassword(String login) {
        try (Connection connection = DriverManager.getConnection(url, userName, password)) {

            PreparedStatement ps = SqlUtils.createPreparedStatement(connection, SELECT_PASSWORD, schema);
            ps.setString(1, login);
            ResultSet resultSet = ps.executeQuery();

            resultSet.next();
            return resultSet.getInt("encodedPassword");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
