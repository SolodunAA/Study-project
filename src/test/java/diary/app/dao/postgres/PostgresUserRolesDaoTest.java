package diary.app.dao.postgres;

import diary.app.dao.LoginDao;
import diary.app.dao.UserRolesDao;
import diary.app.dto.Role;
import diary.app.dto.UserAction;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.EnumSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PostgresUserRolesDaoTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );
    private final UserRolesDao userRolesDao = new PostgresUserRolesDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "admin_data");
    private final LoginDao loginDao = new PostgresLoginDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "admin_data");
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
        } catch (Exception e ){
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
            st.execute("TRUNCATE TABLE admin_data.login_table CASCADE");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void addAndGetRoleForUserTest() {
        String login = "login";
        loginDao.addNewUser(login, 11);
        Role role = new Role(EnumSet.of(UserAction.SEE_USER_TRAININGS));

        userRolesDao.addRoleForUser(login, role);
        assertEquals(role, userRolesDao.getUserRole(login));
    }

}
