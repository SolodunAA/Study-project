package diary.app.service.Impl;

import diary.app.dao.TrainingDao;
import diary.app.dto.ChangeTrainingDto;
import diary.app.dto.TrainingDto;
import diary.app.out.ConsolePrinter;
import diary.app.service.TrainingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TrainingServiceImpl implements TrainingService {

    private final TrainingDao trainingDao;

    public TrainingServiceImpl(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public boolean addTraining(String user, TrainingDto training) {
        try {
            trainingDao.addNewTraining(user, training);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<TrainingDto> getTraining(String login, LocalDate date, String type) {
        try {
            return trainingDao.getTraining(login, date, type);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<TrainingDto> getAllTrainings(String login) {
        try {
            return trainingDao.getAllTrainings(login);
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<TrainingDto> getTrainingsFromThePeriod(String login, LocalDate startDate, LocalDate endDate){
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

    public boolean changeTheTraining(String login, ChangeTrainingDto changeTrainingDto) {
        Optional<TrainingDto> trainingOpt = trainingDao.getTraining(login, changeTrainingDto.getDate(), changeTrainingDto.getType());
        if (trainingOpt.isEmpty()) {
            return false;
        }
        TrainingDto training = trainingOpt.get();
        trainingDao.deleteTraining(login, changeTrainingDto.getDate(), changeTrainingDto.getType());

        switch (changeTrainingDto.getReplace()) {
            case ("date") -> {
                TrainingDto newTraining = new TrainingDto(LocalDate.parse(changeTrainingDto.getNewValue()), training.getType(),
                        training.getTimeInMinutes(), training.getCalories(), training.getAdditionalInfo());
                trainingDao.addNewTraining(login, newTraining);
            }
            case ("type") -> {
                TrainingDto newTraining = new TrainingDto(training.getDate(), changeTrainingDto.getNewValue(),
                        training.getTimeInMinutes(), training.getCalories(), training.getAdditionalInfo());
                trainingDao.addNewTraining(login, newTraining);
            }
            case ("timeInMinutes") -> {
                TrainingDto newTraining = new TrainingDto(training.getDate(), training.getType(),
                        Double.parseDouble(changeTrainingDto.getNewValue()), training.getCalories(), training.getAdditionalInfo());
                trainingDao.addNewTraining(login, newTraining);
            }
            case ("calories") -> {
                TrainingDto newTraining = new TrainingDto(training.getDate(), training.getType(),
                        training.getTimeInMinutes(), Integer.parseInt(changeTrainingDto.getNewValue()), training.getAdditionalInfo());
                trainingDao.addNewTraining(login, newTraining);
            }
            case ("additionalInfo") -> {
                TrainingDto newTraining = new TrainingDto(training.getDate(), training.getType(),
                        training.getTimeInMinutes(), training.getCalories(), changeTrainingDto.getNewValue());
                trainingDao.addNewTraining(login, newTraining);
            }
        }
        return true;
    }


}
