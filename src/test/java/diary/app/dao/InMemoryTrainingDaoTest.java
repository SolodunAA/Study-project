package diary.app.dao;

import diary.app.dto.Training;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
}
