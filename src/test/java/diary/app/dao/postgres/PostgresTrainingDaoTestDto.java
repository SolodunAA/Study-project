package diary.app.dao.postgres;

import diary.app.dao.LoginDao;
import diary.app.dao.TrainingDao;
import diary.app.dto.TrainingDto;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PostgresTrainingDaoTestDto {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );
    private final TrainingDao trainingDao = new PostgresTrainingDao(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), "dairy");
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
    public void prepareBeforeTest() {
        clearDb();
        loginDao.addNewUser("login", 111);
        trainingDao.addTrainingType("swimming");
        trainingDao.addTrainingType("gym");
        trainingDao.addTrainingType("jogging");
    }

    private void clearDb() {
        try (Connection connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            var st = connection.createStatement();
            st.execute("TRUNCATE TABLE dairy.trainings_type_table CASCADE");
            var st2 = connection.createStatement();
            st2.execute("TRUNCATE TABLE admin_data.login_table CASCADE");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void addNewTrainingTest() {
        String login = "login";
        LocalDate date = LocalDate.parse("2024-04-20");
        String type = "swimming";
        int time = 45;
        int calories = 150;
        String addInfo = "no";
        TrainingDto training = new TrainingDto(date, type, time, calories, addInfo);
        List<TrainingDto> list = new ArrayList<>();

        list.add(training);
        trainingDao.addNewTraining(login, training);

        assertThat(trainingDao.getAllTrainings(login)).isEqualTo(list);
    }

    @Test
    public void getTrainingTest() {
        String login = "login";
        LocalDate date = LocalDate.parse("2024-04-20");
        String type = "swimming";
        int time = 45;
        int calories = 150;
        String addInfo = "no";
        TrainingDto training = new TrainingDto(date, type, time, calories, addInfo);
        trainingDao.addNewTraining(login, training);

        assertThat(trainingDao.getTraining(login, date, type).orElseThrow()).isEqualTo(training);
    }

    @Test
    public void deleteTrainingTest() {
        String login = "login";
        LocalDate date = LocalDate.parse("2024-04-20");
        String type = "swimming";
        int time = 45;
        int calories = 150;
        String addInfo = "no";
        TrainingDto training = new TrainingDto(date, type, time, calories, addInfo);

        trainingDao.addNewTraining(login, training);
        trainingDao.deleteTraining(login, date, type);

        assertThat(trainingDao.getAllTrainings(login)).doesNotContain(training);
    }

    @Test
    public void getAllTrainingTest() {
        String login = "login";
        LocalDate date1 = LocalDate.parse("2024-04-20");
        String type1 = "swimming";
        int time1 = 45;
        int calories1 = 150;
        String addInfo1 = "no";
        TrainingDto training1 = new TrainingDto(date1, type1, time1, calories1, addInfo1);

        LocalDate date2 = LocalDate.parse("2024-04-15");
        String type2 = "gym";
        int time2 = 65;
        int calories2 = 500;
        String addInfo2 = "59km";
        TrainingDto training2 = new TrainingDto(date2, type2, time2, calories2, addInfo2);

        List<TrainingDto> list = new ArrayList<>();

        trainingDao.addNewTraining(login, training1);
        trainingDao.addNewTraining(login, training2);
        list.add(training2);
        list.add(training1);

        assertThat(trainingDao.getAllTrainings(login)).isEqualTo(list);
    }

    @Test
    public void getTrainingFromThePeriodTest() {
        String login = "login";
        LocalDate dateStart = LocalDate.parse("2024-01-01");
        LocalDate dateEnd = LocalDate.parse("2025-01-01");

        LocalDate date1 = LocalDate.parse("2024-04-20");
        String type1 = "swimming";
        int time1 = 45;
        int calories1 = 150;
        String addInfo1 = "no";
        TrainingDto training1 = new TrainingDto(date1, type1, time1, calories1, addInfo1);

        LocalDate date2 = LocalDate.parse("2024-04-15");
        String type2 = "gym";
        int time2 = 65;
        int calories2 = 500;
        String addInfo2 = "59km";
        TrainingDto training2 = new TrainingDto(date2, type2, time2, calories2, addInfo2);

        LocalDate date3 = LocalDate.parse("2023-04-15");
        String type3 = "jogging";
        int time3 = 35;
        int calories3 = 230;
        String addInfo3 = "no";
        TrainingDto training3 = new TrainingDto(date3, type3, time3, calories3, addInfo3);

        List<TrainingDto> list = new ArrayList<>();

        trainingDao.addNewTraining(login, training1);
        trainingDao.addNewTraining(login, training2);
        trainingDao.addNewTraining(login, training3);
        list.add(training2);
        list.add(training1);

        assertThat(trainingDao.getTrainingsFromThePeriod(login, dateStart, dateEnd)).isEqualTo(list);
    }

    @Test
    public void getTrainingTypeTest() {
        clearDb();
        String type1 = "swimming";
        String type2 = "gym";
        String type3 = "jogging";

        trainingDao.addTrainingType(type1);
        trainingDao.addTrainingType(type2);
        trainingDao.addTrainingType(type3);

        Set<String> set = new HashSet<>();

        set.add(type1);
        set.add(type2);
        set.add(type3);

        assertThat(trainingDao.getTrainingTypes()).isEqualTo(set);
    }

    @Test
    public void addTrainingTypeTest() {
        clearDb();
        String type1 = "swimming";
        trainingDao.addTrainingType(type1);
        assertThat(trainingDao.getTrainingTypes()).contains(type1);
    }

    @Test
    public void deleteTrainingTypeTest() {
        clearDb();
        String type1 = "swimming";
        String type2 = "gym";
        String type3 = "jogging";

        trainingDao.addTrainingType(type1);
        trainingDao.addTrainingType(type2);
        trainingDao.addTrainingType(type3);

        trainingDao.deleteTrainingType(type1);

        assertThat(trainingDao.getTrainingTypes()).doesNotContain(type1);
    }
}