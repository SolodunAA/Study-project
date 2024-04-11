package diary.app.dao;

import diary.app.dto.Training;
import diary.app.out.ConsolePrinter;

import java.time.LocalDate;
import java.util.*;

public class InMemoryTrainingDao implements TrainingDao {

    private final Set<String> trainingTypes = new HashSet<>();
    private final Map<String, Map<TrainingKey, Training>> clientToTrainingCache = new HashMap<>();
    private final Comparator<Training> byDateComparator = Comparator.comparing(Training::getDate);

    @Override
    public void addNewTraining(String login, Training tng) {
        TrainingKey trainingKey = new TrainingKey(tng.getType(), tng.getDate());
        Map<TrainingKey, Training> allPersonTraining = clientToTrainingCache.get(login);
        if (allPersonTraining == null) {
            allPersonTraining = new HashMap<>();
            clientToTrainingCache.put(login, allPersonTraining);
        }

        if (allPersonTraining.containsKey(trainingKey)) {
            ConsolePrinter.print(trainingKey + " already exists");
        } else {
            allPersonTraining.put(trainingKey, tng);
        }
    }

    public Training getTraining(String login, LocalDate date, String type){
        TrainingKey trainingKey = new TrainingKey(type, date);
        Map<TrainingKey, Training> allPersonTraining = clientToTrainingCache.get(login);
        Training result = null;
        if (allPersonTraining == null) {
            ConsolePrinter.print("You don't have training for changing");
        } else {
            if (allPersonTraining.containsKey(trainingKey)) {
                result = allPersonTraining.get(trainingKey);
            } else {
                ConsolePrinter.print(trainingKey + "Doesn't exist");
            }
        }
        return result;
    }

    @Override
    public void deleteTraining(String login, LocalDate date, String type) {
        TrainingKey trainingKey = new TrainingKey(type, date);
        Map<TrainingKey, Training> allPersonTraining = clientToTrainingCache.get(login);
        if (allPersonTraining == null) {
            ConsolePrinter.print("You don't have training for deleting");
        } else {
            if (allPersonTraining.containsKey(trainingKey)) {
                allPersonTraining.remove(trainingKey);
            } else {
                ConsolePrinter.print(trainingKey + "Doesn't exist");
            }
        }
    }

    public void changeTraining(String login, LocalDate newDate, String newType, double newTimeInMinutes, int newCalories, String newAdditionalInfo){

    }

    @Override
    public List<Training> getAllTrainings(String login) {
        Map<TrainingKey, Training> allPersonTraining = clientToTrainingCache.get(login);
        List<Training> result = new ArrayList<>(allPersonTraining.values());
        result.sort(byDateComparator);
        return result;
    }

    @Override
    //Написано криво, так как дел полно, времени нет как красиво сделать
    public List<Training> getTrainingsFromThePeriod(String login, LocalDate startDate, LocalDate endDate) {
        Map<TrainingKey, Training> allPersonTraining = clientToTrainingCache.get(login);
        List<Training> intermediateResult = new ArrayList<>(allPersonTraining.values());
        intermediateResult.sort(byDateComparator);
        List<Training> result = new ArrayList<>();
        for(Training training: intermediateResult){
            if(training.getDate().isAfter(startDate) && training.getDate().isBefore(endDate)){
                result.add(training);
            }
        }
        return result;
    }

    @Override
    public Set<String> getTrainingTypes() {
        return Set.copyOf(trainingTypes);
    }

    @Override
    public void addTrainingType(String trainingType) {
        trainingTypes.add(trainingType);
    }

    @Override
    public void deleteTrainingType(String trainingType) {
        trainingTypes.remove(trainingType);
    }

    private static class TrainingKey {
        private final String type;
        private final LocalDate date;

        private TrainingKey(String type, LocalDate date) {
            this.type = type;
            this.date = date;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TrainingKey that = (TrainingKey) o;
            return Objects.equals(type, that.type) && Objects.equals(date, that.date);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, date);
        }

        @Override
        public String toString() {
            return "Training{" +
                    "type='" + type + '\'' +
                    ", date=" + date +
                    '}';
        }
    }
}
