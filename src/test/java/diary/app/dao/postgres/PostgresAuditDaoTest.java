package diary.app.dao.postgres;

import diary.app.dto.AuditItem;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PostgresAuditDaoTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeClass
    public static void beforeAll() {
        postgres.start();
        runMigrations();
    }

    private static void runMigrations() {
        try (Connection connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            System.out.println("starting migration");
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase =
                    new Liquibase("db.changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("migration finished successfully");
        } catch (Exception e) {
            System.out.println("Got SQL Exception " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public static void afterAll() {
        postgres.stop();
    }

    @Before
    public void clearDb() {
        try (Connection connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            var st = connection.createStatement();
            st.execute("TRUNCATE TABLE admin_data.\"AuditTable\"");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void addAndGetAuditDaoTest() {
        var dao = new PostgresAuditDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "admin_data");
        String login = "user";
        String action = "registraton";
        String userInput = "user";

        AuditItem auditItem = new AuditItem(login, action, userInput);
        dao.addAuditItem(auditItem);

        assertTrue(dao.getAuditItems(dao.AuditItemsSize()).contains(auditItem));
    }
    @Test
    public void getAuditDaoTest() {
        var dao = new PostgresAuditDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "admin_data");
        String login1 = "user";
        String action1 = "registraton";
        String userInput1 = "user";

        AuditItem auditItem1 = new AuditItem(login1, action1, userInput1);

        String login2 = "admin";
        String action2 = "changeInfo";
        String userInput2 = "setNewType";

        AuditItem auditItem2 = new AuditItem(login2, action2, userInput2);

        dao.addAuditItem(auditItem1);
        dao.addAuditItem(auditItem2);

        List<AuditItem> list = new ArrayList<>();
        list.add(auditItem1);
        list.add(auditItem2);

        assertEquals(list, dao.getAuditItems(dao.AuditItemsSize()));
    }
    @Test
    public void getAuditItemsSizeTest() {
        var dao = new PostgresAuditDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "admin_data");
        String login1 = "user";
        String action1 = "registraton";
        String userInput1 = "user";

        AuditItem auditItem1 = new AuditItem(login1, action1, userInput1);

        String login2 = "admin";
        String action2 = "changeInfo";
        String userInput2 = "setNewType";

        AuditItem auditItem2 = new AuditItem(login2, action2, userInput2);

        dao.addAuditItem(auditItem1);
        dao.addAuditItem(auditItem2);

        assertEquals(2, dao.AuditItemsSize());
    }
}