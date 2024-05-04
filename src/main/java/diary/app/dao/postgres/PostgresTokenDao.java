package diary.app.dao.postgres;

import diary.app.dao.TokenDao;
import diary.app.dto.Token;
import diary.app.out.ConsolePrinter;

import java.sql.*;
import java.util.Optional;

public class PostgresTokenDao implements TokenDao {
    private final String url;
    private final String userName;
    private final String password;
    private final String schema;
    private static final String INSERT_TOKEN = """
            INSERT INTO :SCHEMA.token_table
            (login_id, token, time) VALUES ((SELECT login_id FROM :SCHEMA.login_table WHERE login = ?),?,?)
            """;
    private static final String SELECT_TOKEN = """
            SELECT token, time
            FROM :SCHEMA.token_table
            WHERE login_id = (SELECT login_id FROM :SCHEMA.login_table WHERE login = ?)
            """;

    private final static String DELETE_TOKEN = """
            DELETE FROM :SCHEMA.token_table
            WHERE login_id = (SELECT login_id FROM :SCHEMA.login_table WHERE login = ?)
            """;

    public PostgresTokenDao(String url, String userName, String password, String schema) {
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.schema = schema;
    }


    @Override
    public void deleteToken(String user) {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = SqlUtils.createPreparedStatement(connection, DELETE_TOKEN, schema)) {
            ps.setString(1, user);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addToken(Token token) {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = SqlUtils.createPreparedStatement(connection, INSERT_TOKEN, schema)) {
            ps.setString(1, token.getLogin());
            ps.setString(2, token.getToken());
            ps.setLong(3, token.getTimestamp());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Token> getToken(String user) {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = SqlUtils.createPreparedStatement(connection, SELECT_TOKEN, schema)) {
            ps.setString(1, user);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                String tokenString = resultSet.getString("token");
                long timestemp = resultSet.getLong("time");
                Token token = new Token(user, tokenString, timestemp);
                return Optional.of(token);
            } else {
                ConsolePrinter.print("token does not exists");
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
