package diary.app.service;

import diary.app.dto.ChangeTrainingDto;
import diary.app.dto.TrainingDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
/**
 * used for interaction with trainingDao
 */
public interface TrainingService {
    /**
     * method add new training for this user in dao
     * @param user user login
     * @param training added training
     * @return is success or not
     */
    boolean addTraining(String user, TrainingDto training);

    /**
     * this method used for getting information about the training for user
     * @param login user login
     * @param date the date of the training
     * @param type the type of the training
     * @return Optional containing the training
     */
    Optional<TrainingDto> getTraining(String login, LocalDate date, String type);

    /**
     * this method used for getting all information about all trainings for this user
     * @param login user login
     * @return list of all trainings
     */
    List<TrainingDto> getAllTrainings(String login);

    /**
     * this method used for getting all information about trainings for this user for the period
     * @param login user login
     * @param startDate start date of period
     * @param endDate period end date
     * @return list of all trainings for the period
     */
    List<TrainingDto> getTrainingsFromThePeriod(String login, LocalDate startDate, LocalDate endDate);

    /**
     * used for deleting information about the training
     * @param login user login
     * @param date date of training
     * @param type type of training
     * @return is success or not
     */
    boolean deleteTraining(String login, LocalDate date, String type);

    /**
     * used for showing all trainings types
     * @return set of trainings types
     */
    Set<String> getTrainingTypes();

    /**
     * add new training types
     * @param training training type
     * @return
     */
    boolean addTrainingType(String training);

    /**
     * deleting training type
     * @param trainingType
     * @return is successful or not
     */
    boolean deleteTrainingType(String trainingType);

    /**
     * change information about the training
     * @param login user login
     * @param changeTrainingDto dto contains information about training will be changed
     *                          and parameters will be changed
     * @return is successfull or not
     */
    boolean changeTheTraining(String login, ChangeTrainingDto changeTrainingDto);

}
