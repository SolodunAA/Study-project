package diary.app.dao.postgres;

import diary.app.dao.LoginDao;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PostgresLoginDaoTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    private final LoginDao dao = new PostgresLoginDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "admin_data");

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
    public void addNewUserTest() {
        dao.addNewUser("login1", 111);
        assertThat(dao.checkIfUserExist("login1")).isTrue();
    }

    @Test
    public void addDuplicateUserTest() {
        dao.addNewUser("login1", 111);
        assertThatThrownBy(() -> dao.addNewUser("login1", 222)).isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    public void getEncodedPasswordTest() {
        dao.addNewUser("login1", 111);
        assertThat(dao.getEncodedPassword("login1")).isEqualTo(111);
    }
    @Test
    public void checkIfUserExistTest() {
        String login = "login";
        int passwordEncoded = 111;
        dao.addNewUser(login, passwordEncoded);
        assertThat(dao.checkIfUserExist(login)).isTrue();
    }
}
