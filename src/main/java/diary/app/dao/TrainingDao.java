package diary.app.dao;

import diary.app.dto.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface TrainingDao {
    void addNewTraining(String login, Training tng);
    Training getTraining(String login, LocalDate date, String type);
    void deleteTraining(String login, LocalDate date, String type);
    void changeTraining(String login, LocalDate newDate, String newType, double newTimeInMinutes,
                        int newCalories, String newAdditionalInfo);
    List<Training> getAllTrainings(String login);
    List<Training> getTrainingsFromThePeriod(String login, LocalDate startDate, LocalDate endDate);
    Set<String> getTrainingTypes();
    void addTrainingType(String trainingType);
    void deleteTrainingType(String trainingType);
}
