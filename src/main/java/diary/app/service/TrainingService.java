package diary.app.service;

import diary.app.dao.TrainingDao;
import diary.app.dao.UserRolesDao;
import diary.app.dto.Training;
import diary.app.dto.UserAction;
import diary.app.out.ConsolePrinter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TrainingService {

    private final TrainingDao trainingDao;

    public TrainingService(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public boolean addTraining(String user, Training training) {
        try {
            trainingDao.addNewTraining(user, training);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<Training> getTraining(String login, LocalDate date, String type) {
        try {
            return trainingDao.getTraining(login, date, type);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Training> getAllTrainings(String login) {
        try {
            return trainingDao.getAllTrainings(login);
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Training> getTrainingsFromThePeriod(String login, LocalDate startDate, LocalDate endDate){
        try {
            return trainingDao.getTrainingsFromThePeriod(login, startDate, endDate);
        } catch (Exception e) {
            return List.of();
        }
    }

    public boolean deleteTraining(String login, LocalDate date, String type){
        try {
            return trainingDao.deleteTraining(login, date, type);
        } catch (Exception e) {
            return false;
        }
    }

    public Set<String> getTrainingTypes(){
        try {
            return trainingDao.getTrainingTypes();
        } catch (Exception e) {
            return Set.of();
        }
    }
    public boolean addTrainingType(String training){
        try {
            trainingDao.addTrainingType(training);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteTrainingType(String trainingType){
        try {
            trainingDao.deleteTrainingType(trainingType);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
