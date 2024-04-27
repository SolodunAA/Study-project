package diary.app.dao.inmemory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import diary.app.dao.TrainingDao;
import diary.app.dto.Training;
import org.junit.Test;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryTrainingDaoTest {
    @Test
    public void getNullTrainingTest(){
        TrainingDao trainingDao = new InMemoryTrainingDao();
        String login = "user";
        LocalDate date = LocalDate.parse("2024-04-12");
        String type = "swimming";

        assertThat(trainingDao.getTraining(login, date, type).isEmpty()).isTrue();
    }

    @Test
    public void addAndGetTrainingTest() {
        TrainingDao trainingDao = new InMemoryTrainingDao();
        trainingDao.addTrainingType("swimming");

        String login = "user";
        LocalDate date = LocalDate.parse("2024-04-12");
        String type = "swimming";
        Training training = new Training(date, type, 44, 54, "no");

        trainingDao.addNewTraining(login, training);

        assertThat(trainingDao.getTraining(login, date, type).orElseThrow()).isEqualTo(training);
    }

    @Test
    public void addTrainingWithInvalidTypeTest() {
        TrainingDao trainingDao = new InMemoryTrainingDao();

        String login = "user";
        LocalDate date = LocalDate.parse("2024-04-12");
        String type = "swimming";
        Training training = new Training(date, type, 44, 54, "no");

        trainingDao.addNewTraining(login, training);

        assertThat(trainingDao.getTraining(login, date, type).isEmpty()).isTrue();
    }

    @Test
    public void tst() throws JsonProcessingException {
        LocalDate date = LocalDate.parse("2024-04-12");
        String type = "swimming";
        Training training = new Training(date, type, 44, 54, "no");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(df);

        String json = objectMapper.writeValueAsString(training);
        System.out.println(URLEncoder.encode(json));
        Training obj = objectMapper.readValue(json, Training.class);

        assertThat(training).isEqualTo(obj);

    }

    @Test
    public void addNewTrainingTwiceTest(){
        TrainingDao trainingDao = new InMemoryTrainingDao();
        trainingDao.addTrainingType("swimming");

        String login = "user";
        LocalDate date = LocalDate.parse("2024-04-12");
        String type = "swimming";


        double timeInMinutes1 = 45.5;
        int calories1 = 100;
        String additionalInfo1 = "no";
        Training training1 = new Training(date, type, timeInMinutes1,  calories1, additionalInfo1);

        trainingDao.addNewTraining(login, training1);

        double timeInMinutes2 = 587;
        int calories2 = 345;
        String additionalInfo2= "no";
        Training training2 = new Training(date, type, timeInMinutes2,  calories2, additionalInfo2);

        trainingDao.addNewTraining(login, training2);

        List<Training> expectedTrainingList = List.of(training1);

        assertThat(trainingDao.getAllTrainings(login)).isEqualTo(expectedTrainingList);
    }
    @Test
    public void deleteTraining(){
        TrainingDao trainingDao = new InMemoryTrainingDao();
        trainingDao.addTrainingType("swimming");
        trainingDao.addTrainingType("gym");

        String login = "user";
        LocalDate date1 = LocalDate.parse("2024-04-12");
        String type1 = "swimming";
        double timeInMinutes1 = 45.5;
        int calories1 = 100;
        String additionalInfo1 = "no";

        Training training1 = new Training(date1, type1, timeInMinutes1,  calories1, additionalInfo1);

        LocalDate date2 = LocalDate.parse("2024-04-17");
        String type2 = "gym";
        double timeInMinutes2 = 60;
        int calories2 = 150;
        String additionalInfo2 = "no";

        Training training2 = new Training(date2, type2, timeInMinutes2,  calories2, additionalInfo2);

        LocalDate date3 = LocalDate.parse("2024-02-19");
        String type3 = "gym";
        double timeInMinutes3 = 30;
        int calories3 = 200;
        String additionalInfo3 = "no";

        Training training3 = new Training(date3, type3, timeInMinutes3,  calories3, additionalInfo3);

        trainingDao.addNewTraining(login, training1);
        trainingDao.addNewTraining(login, training2);
        trainingDao.addNewTraining(login, training3);

        List<Training> expectedTrainingsList = List.of(training1, training2);

        trainingDao.deleteTraining(login, date3, type3);

        assertThat(trainingDao.getAllTrainings(login)).isEqualTo(expectedTrainingsList);
    }
    @Test
    public void getAllTrainingsTest(){
        TrainingDao trainingDao = new InMemoryTrainingDao();
        trainingDao.addTrainingType("swimming");
        trainingDao.addTrainingType("gym");

        String login = "user";
        LocalDate date1 = LocalDate.parse("2024-04-12");
        String type1 = "swimming";
        double timeInMinutes1 = 45.5;
        int calories1 = 100;
        String additionalInfo1 = "no";

        Training training1 = new Training(date1, type1, timeInMinutes1,  calories1, additionalInfo1);

        LocalDate date2 = LocalDate.parse("2024-04-17");
        String type2 = "gym";
        double timeInMinutes2 = 60;
        int calories2 = 150;
        String additionalInfo2 = "no";

        Training training2 = new Training(date2, type2, timeInMinutes2,  calories2, additionalInfo2);

        LocalDate date3 = LocalDate.parse("2024-02-19");
        String type3 = "gym";
        double timeInMinutes3 = 30;
        int calories3 = 200;
        String additionalInfo3 = "no";

        Training training3 = new Training(date3, type3, timeInMinutes3,  calories3, additionalInfo3);

        trainingDao.addNewTraining(login, training1);
        trainingDao.addNewTraining(login, training2);
        trainingDao.addNewTraining(login, training3);

        //is ordered by date
        List<Training> trainingsList = List.of(training3, training1, training2);

        assertThat(trainingDao.getAllTrainings(login)).isEqualTo(trainingsList);
    }
    @Test
    public void getTrainingsFromThePeriodTest(){
        TrainingDao trainingDao = new InMemoryTrainingDao();
        trainingDao.addTrainingType("swimming");
        trainingDao.addTrainingType("gym");

        String login = "user";
        LocalDate date1 = LocalDate.parse("2024-04-12");
        String type1 = "swimming";
        double timeInMinutes1 = 45.5;
        int calories1 = 100;
        String additionalInfo1 = "no";

        Training training1 = new Training(date1, type1, timeInMinutes1,  calories1, additionalInfo1);

        LocalDate date2 = LocalDate.parse("2024-04-17");
        String type2 = "gym";
        double timeInMinutes2 = 60;
        int calories2 = 150;
        String additionalInfo2 = "no";

        Training training2 = new Training(date2, type2, timeInMinutes2,  calories2, additionalInfo2);

        LocalDate date3 = LocalDate.parse("2020-02-19");
        String type3 = "gym";
        double timeInMinutes3 = 30;
        int calories3 = 200;
        String additionalInfo3 = "no";

        Training training3 = new Training(date3, type3, timeInMinutes3,  calories3, additionalInfo3);

        LocalDate dateStart = LocalDate.parse("2024-01-01");
        LocalDate dateFinish = LocalDate.parse("2025-01-01");

        trainingDao.addNewTraining(login, training1);
        trainingDao.addNewTraining(login, training2);
        trainingDao.addNewTraining(login, training3);

        List<Training> daoListTrainings = trainingDao.getTrainingsFromThePeriod(login, dateStart, dateFinish);
        List<Training> expectedTrainingsList = List.of(training1,training2);

        assertThat(daoListTrainings).isEqualTo(expectedTrainingsList);
    }

    @Test
    public void addAndGetTrainingTypeTest(){
        TrainingDao trainingDao = new InMemoryTrainingDao();
        String trainingType = "swimming";
        trainingDao.addTrainingType(trainingType);

        assertThat(trainingDao.getTrainingTypes()).contains(trainingType);
    }

    @Test
    public void deleteTrainingTypeTest(){
        TrainingDao trainingDao = new InMemoryTrainingDao();
        trainingDao.addTrainingType("swimming");
        trainingDao.addTrainingType("gym");
        trainingDao.addTrainingType("running");
        trainingDao.addTrainingType("workout");

        String deleteType = "newTrainingType";

        trainingDao.addTrainingType("swimming");

        assertThat(trainingDao.getTrainingTypes()).doesNotContain(deleteType);
    }

}
