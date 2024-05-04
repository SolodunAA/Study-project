package diary.app.dao.postgres;
import diary.app.dao.AuditDao;
import diary.app.dto.AuditItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresAuditDao implements AuditDao {
    private static final String INSERT_AUDIT_ITEM_SQL = """
            INSERT INTO :SCHEMA.audit_table
            (login, time, action, user_input) VALUES (?,?,?,?)
            """;
    private static final String RETRIEVE_AUDIT_RECORD_SQL = """
            SELECT login, time, action, user_input
            FROM :SCHEMA.audit_table
            LIMIT ?
            """;
    private static final String SELECT_COUNT = "SELECT COUNT(*) FROM :SCHEMA.audit_table";
    private final String url;
    private final String userName;
    private final String password;
    private final String schema;

    public PostgresAuditDao(String url, String userName, String password, String schema) {
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.schema = schema;
    }

    @Override
    public void addAuditItem(AuditItem auditItem) {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = SqlUtils.createPreparedStatement(connection, INSERT_AUDIT_ITEM_SQL, schema)) {
            ps.setString(1, auditItem.getUser());
            ps.setLong(2, auditItem.getTimestamp());
            ps.setString(3, auditItem.getAction());
            ps.setString(4, auditItem.getUserInput());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int AuditItemsSize() {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             Statement statement = connection.createStatement()) {
            String statementWithSchema = SqlUtils.setSchema(SELECT_COUNT, schema);
            ResultSet resultSet = statement.executeQuery(statementWithSchema);

            resultSet.next();
            return resultSet.getInt(1);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<AuditItem> getAuditItems(int limit) {
        List<AuditItem> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = SqlUtils.createPreparedStatement(connection, RETRIEVE_AUDIT_RECORD_SQL, schema)) {
            ps.setInt(1, limit);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String login = resultSet.getString("login");
                long timestamp = resultSet.getLong("time");
                String action = resultSet.getString("action");
                String userInput = resultSet.getString("user_input");
                AuditItem auditItem = new AuditItem(login, timestamp, action, userInput);
                list.add(auditItem);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
