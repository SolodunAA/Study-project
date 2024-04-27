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

import static org.assertj.core.api.Assertions.assertThat;

public class PostgresAuditDaoTest {

    private final PostgresAuditDao dao = new PostgresAuditDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "admin_data");
    private final PostgresLoginDao postgresLoginDao = new PostgresLoginDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "admin_data");


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
        try (Connection connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
             var st = connection.createStatement()) {
            st.execute("TRUNCATE TABLE admin_data.login_table CASCADE");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void addAndGetAuditDaoTest() {
        String login = "user";
        String action = "registraton";
        String userInput = "user";

        postgresLoginDao.addNewUser(login, 111);

        AuditItem auditItem = new AuditItem(login, action, userInput);
        dao.addAuditItem(auditItem);

        assertThat(dao.getAuditItems(dao.AuditItemsSize())).contains(auditItem);
    }

    @Test
    public void getAuditDaoTest() {
        String login1 = "user";
        String action1 = "registraton";
        String userInput1 = "user";

        postgresLoginDao.addNewUser(login1, 111);
        AuditItem auditItem1 = new AuditItem(login1, action1, userInput1);

        String login2 = "admin";
        String action2 = "changeInfo";
        String userInput2 = "setNewType";

        postgresLoginDao.addNewUser(login2, 112);
        AuditItem auditItem2 = new AuditItem(login2, action2, userInput2);

        dao.addAuditItem(auditItem1);
        dao.addAuditItem(auditItem2);

        List<AuditItem> list = new ArrayList<>();
        list.add(auditItem1);
        list.add(auditItem2);

        assertThat(dao.getAuditItems(dao.AuditItemsSize())).isEqualTo(list);
    }

    @Test
    public void getAuditItemsSizeTest() {
        String login1 = "user";
        String action1 = "registraton";
        String userInput1 = "user";
        postgresLoginDao.addNewUser(login1, 111);

        AuditItem auditItem1 = new AuditItem(login1, action1, userInput1);

        String login2 = "admin";
        String action2 = "changeInfo";
        String userInput2 = "setNewType";
        postgresLoginDao.addNewUser(login2, 112);

        AuditItem auditItem2 = new AuditItem(login2, action2, userInput2);

        dao.addAuditItem(auditItem1);
        dao.addAuditItem(auditItem2);

        assertThat(dao.AuditItemsSize()).isEqualTo(2);
    }
}