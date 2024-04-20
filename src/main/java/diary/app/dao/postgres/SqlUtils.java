package diary.app.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlUtils {

    public static String SCHEMA_PLACEHOLDER = ":SCHEMA";

    private SqlUtils() {
    }

    public static String setSchema(String sql, String schema) {
        return sql.replace(SCHEMA_PLACEHOLDER, schema);
    }

    public static PreparedStatement createPreparedStatement(Connection conn, String sql, String schema) throws SQLException {
        String statementWithSchema = setSchema(sql, schema);
        return conn.prepareStatement(statementWithSchema);
    }
}
