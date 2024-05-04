package diary.app.dao;

import diary.app.dto.TrainingDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
/**
 * class stores information about trainings and o some actions on them: add, get,
 * delete and etc
 */
public interface TrainingDao {
    /**
     *Method adds information about new training to the storage
     * @param login username whose workout need to add
     * @param tng added training
     */
    void addNewTraining(String login, TrainingDto tng);

    /**
     * Method get training information from storage
     * @param login - login of the user
     * @param date - date of the training
     * @param type - training type
     * @return training record
     */
    Optional<TrainingDto> getTraining(String login, LocalDate date, String type);

    /**
     * Method delete information about selected training from the storage
     * @param login - login of the user
     * @param date - date of the training
     * @param type - training type
     */
    boolean deleteTraining(String login, LocalDate date, String type);

    /**
     * Method get all training records
     * @param login - login of the user
     * @return List which contains training records
     */
    List<TrainingDto> getAllTrainings(String login);

    /**
     * Method get all training records for the period
     * @param login - login of the user
     * @param startDate - period start date
     * @param endDate - period утв date
     * @return List which contains training records
     */
    List<TrainingDto> getTrainingsFromThePeriod(String login, LocalDate startDate, LocalDate endDate);

    /**
     * @return set of trainings type
     */
    Set<String> getTrainingTypes();

    /**
     * Method adds new training type to the set
     * @param trainingType - new training type
     */
    void addTrainingType(String trainingType);

    /**
     * Method deletes training type to the set
     * @param trainingType - deleted training type
     */
    void deleteTrainingType(String trainingType);
}
