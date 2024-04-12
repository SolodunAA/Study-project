package diary.app.dao;

import diary.app.dto.Training;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class InMemoryTrainingDaoTest {
    @Test
    public void getNullTrainingTest(){
        TrainingDao trainingDao = new InMemoryTrainingDao();
        String login = "user";
        LocalDate date = LocalDate.parse("2024-04-12");
        String type = "swimming";

        assertNull(trainingDao.getTraining(login, date, type));
    }

    @Test
    public void getAndAddTrainingTest() {
        TrainingDao trainingDao = new InMemoryTrainingDao();
        String login = "user";
        LocalDate date = LocalDate.parse("2024-04-12");
        String type = "swimming";
        Training training = new Training(date, type, 44, 54, "no");

        trainingDao.addNewTraining(login, training);

        assertEquals(training, trainingDao.getTraining(login, date, type));
    }
    @Test
    public void addNewTrainingTwiceTest(){

        TrainingDao trainingDao = new InMemoryTrainingDao();
        String login = "user";
        LocalDate date1 = LocalDate.parse("2024-04-12");
        String type1 = "swimming";
        double timeInMinutes1 = 45.5;
        int calories1 = 100;
        String additionalInfo1 = "no";

        Training training1 = new Training(date1, type1, timeInMinutes1,  calories1, additionalInfo1);

        trainingDao.addNewTraining(login, training1);

        LocalDate date2 = LocalDate.parse("2024-04-12");
        String type2 = "swimming";
        double timeInMinutes2 = 587;
        int calories2 = 345;
        String additionalInfo2= "no";

        Training training2 = new Training(date1, type1, timeInMinutes1,  calories1, additionalInfo1);

        trainingDao.addNewTraining(login, training2);

        List<Training> trainingsList = new ArrayList<>();
        trainingsList.add(training1);

        assertEquals(trainingsList, trainingDao.getAllTrainings(login));

    }
    @Test
    public void deleteTraining(){
        TrainingDao trainingDao = new InMemoryTrainingDao();
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

        List<Training> daoListTrainings = trainingDao.getAllTrainings(login);
        List<Training> trainingsList = new ArrayList<>();
        trainingsList.add(training1);
        trainingsList.add(training2);

        trainingDao.deleteTraining(login, date3, type3);

        assertEquals(trainingsList, trainingDao.getAllTrainings(login));
    }
    @Test
    public void getAllTrainingsTest(){
        TrainingDao trainingDao = new InMemoryTrainingDao();
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

        List<Training> daoListTrainings = trainingDao.getAllTrainings(login);
        List<Training> trainingsList = new ArrayList<>();
        trainingsList.add(training1);
        trainingsList.add(training2);
        trainingsList.add(training3);

        assertEquals(trainingsList , daoListTrainings);

    }
    @Test
    public void getTrainingsFromThePeriodTest(){
        TrainingDao trainingDao = new InMemoryTrainingDao();
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
        List<Training> trainingsList = new ArrayList<>();
        trainingsList.add(training1);
        trainingsList.add(training2);

        assertEquals(trainingsList , daoListTrainings);
    }

    @Test
    public void addAndGetTrainingTypeTest(){
        TrainingDao trainingDao = new InMemoryTrainingDao();
        String trainingType = "swimming";
        trainingDao.addTrainingType(trainingType);

        assertTrue(trainingDao.getTrainingTypes().contains(trainingType));
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

        assertFalse(trainingDao.getTrainingTypes().contains(deleteType));
    }

}
