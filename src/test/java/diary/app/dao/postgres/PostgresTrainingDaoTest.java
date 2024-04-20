package diary.app.dao.postgres;

import diary.app.dto.Training;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.ClientInfoStatus;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class PostgresTrainingDaoTest {
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
            st.execute("TRUNCATE TABLE dairy.\"TrainingsTable\"");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            var st = connection.createStatement();
            st.execute("TRUNCATE TABLE dairy.\"TrainingsTypeTable\"");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void addNewUserTest() {
        var dao = new PostgresTrainingDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "dairy");
        String login = "login";
        LocalDate date = LocalDate.parse("2024-04-20");
        String type = "swimming";
        int time = 45;
        int calories = 150;
        String addInfo = "no";
        Training training = new Training(date, type, time, calories, addInfo);
        List<Training> list = new ArrayList<>();

        list.add(training);
        dao.addTrainingType(type);
        dao.addNewTraining(login, training);

        assertEquals(list, dao.getAllTrainings(login));
    }
    @Test
    public void getTrainingTest() {
        var dao = new PostgresTrainingDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "dairy");
        String login = "login";
        LocalDate date = LocalDate.parse("2024-04-20");
        String type = "swimming";
        int time = 45;
        int calories = 150;
        String addInfo = "no";
        Training training = new Training(date, type, time, calories, addInfo);

        dao.addTrainingType(type);
        dao.addNewTraining(login, training);

        assertEquals(training, dao.getTraining(login, date, type));
    }
    @Test
    public void deleteTrainingTest() {
        var dao = new PostgresTrainingDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "dairy");
        String login = "login";
        LocalDate date = LocalDate.parse("2024-04-20");
        String type = "swimming";
        int time = 45;
        int calories = 150;
        String addInfo = "no";
        Training training = new Training(date, type, time, calories, addInfo);

        dao.addTrainingType(type);
        dao.addNewTraining(login, training);
        dao.deleteTraining(login, date, type);

        assertFalse(dao.getAllTrainings(login).contains(training));
    }
    @Test
    public void getAllTrainingTest() {
        var dao = new PostgresTrainingDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "dairy");

        String login = "login";

        LocalDate date1 = LocalDate.parse("2024-04-20");
        String type1 = "swimming";
        int time1 = 45;
        int calories1 = 150;
        String addInfo1 = "no";
        Training training1 = new Training(date1, type1, time1, calories1, addInfo1);


        LocalDate date2 = LocalDate.parse("2024-04-15");
        String type2 = "gym";
        int time2 = 65;
        int calories2 = 500;
        String addInfo2 = "59km";
        Training training2 = new Training(date2, type2, time2, calories2, addInfo2);

        List<Training> list = new ArrayList<>();

        dao.addTrainingType(type1);
        dao.addTrainingType(type2);
        dao.addNewTraining(login, training1);
        dao.addNewTraining(login, training2);
        list.add(training2);
        list.add(training1);

        assertEquals(list, dao.getAllTrainings(login));
    }
    @Test
    public void getTrainingFromThePeriodTest() {
        var dao = new PostgresTrainingDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "dairy");

        String login = "login";
        LocalDate dateStart = LocalDate.parse("2024-01-01");
        LocalDate dateEnd = LocalDate.parse("2025-01-01");

        LocalDate date1 = LocalDate.parse("2024-04-20");
        String type1 = "swimming";
        int time1 = 45;
        int calories1 = 150;
        String addInfo1 = "no";
        Training training1 = new Training(date1, type1, time1, calories1, addInfo1);

        LocalDate date2 = LocalDate.parse("2024-04-15");
        String type2 = "gym";
        int time2 = 65;
        int calories2 = 500;
        String addInfo2 = "59km";
        Training training2 = new Training(date2, type2, time2, calories2, addInfo2);

        LocalDate date3 = LocalDate.parse("2023-04-15");
        String type3 = "jogging";
        int time3 = 35;
        int calories3 = 230;
        String addInfo3 = "no";
        Training training3 = new Training(date3, type3, time3, calories3, addInfo3);

        List<Training> list = new ArrayList<>();

        dao.addTrainingType(type1);
        dao.addTrainingType(type2);
        dao.addTrainingType(type3);
        dao.addNewTraining(login, training1);
        dao.addNewTraining(login, training2);
        dao.addNewTraining(login, training3);
        list.add(training2);
        list.add(training1);

        assertEquals(list, dao.getTrainingsFromThePeriod(login, dateStart, dateEnd));
    }
    @Test
    public void getTrainingTypeTest() {
        var dao = new PostgresTrainingDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "dairy");

        String type1 = "swimming";
        String type2 = "gym";
        String type3 = "jogging";

        dao.addTrainingType(type1);
        dao.addTrainingType(type2);
        dao.addTrainingType(type3);

        Set<String> set = new HashSet<>();

        set.add(type1);
        set.add(type2);
        set.add(type3);

        assertEquals(set, dao.getTrainingTypes());
    }
    @Test
    public void addTrainingTypeTest() {
        var dao = new PostgresTrainingDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "dairy");
        String type1 = "swimming";
        dao.addTrainingType(type1);
        assertTrue(dao.getTrainingTypes().contains(type1));
    }
    @Test
    public void deleteTrainingTypeTest() {
        var dao = new PostgresTrainingDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "dairy");
        String type1 = "swimming";
        String type2 = "gym";
        String type3 = "jogging";

        dao.addTrainingType(type1);
        dao.addTrainingType(type2);
        dao.addTrainingType(type3);

        dao.deleteTrainingType(type1);

        assertFalse(dao.getTrainingTypes().contains(type1));
    }

}