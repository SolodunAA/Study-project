package diary.app.auxiliaryfunctions;
import diary.app.dao.TrainingDao;
import diary.app.dto.Training;
import diary.app.in.Reader;
import diary.app.out.ConsolePrinter;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * This class work with trainings data and show, edit or change a
 * user trainings.
 */
public class TrainingInteractions{
    private final Reader reader;
    private final TrainingDao trainingDao;

    public TrainingInteractions(Reader reader, TrainingDao trainingDao) {
        this.reader = reader;
        this.trainingDao = trainingDao;
    }

    /**
     * Method adds data about one training of the selected user to the storage
     * @param login login of the selected user
     */
    public void addNewTraining(String login) {
        Set<String> trainingTypes = trainingDao.getTrainingTypes();
        ConsolePrinter.print("Let's start to add new training");
        ConsolePrinter.print("Enter date. Date format YYYY-MM-DD");
        LocalDate date = LocalDate.parse(reader.read());
        ConsolePrinter.print("Enter type of your training. Valid types=" + trainingTypes);
        String type = reader.read();
        ConsolePrinter.print("Enter time your training");
        double timeInMinutes = Double.parseDouble(reader.read());
        ConsolePrinter.print("Enter waste your calories");
        int calories = Integer.parseInt(reader.read());
        ConsolePrinter.print("If you want to add additional info, enter it here");
        String additionalInfo = reader.read();
        Training newTraining = new Training(date, type, timeInMinutes, calories, additionalInfo);
        trainingDao.addNewTraining(login, newTraining);
    }
    /**
     * Method delete information about one training of the selected user from the storage
     * @param login login of the selected user
     */
    public void deleteTheTraining(String login) {
        ConsolePrinter.print("Which training you want to delete?");
        ConsolePrinter.print("Enter the date of the training. Date format YYYY-MM-DD");
        LocalDate date = LocalDate.parse(reader.read());
        ConsolePrinter.print("Enter type of your training");
        String type = reader.read();
        trainingDao.deleteTraining(login, date, type);
    }
    /**
     * Method show information about all users trainings
     * @param login login of the selected user
     */
    public void showAllTrainings(String login) {
        List<Training> result = trainingDao.getAllTrainings(login);
        for (Training training : result) {
            ConsolePrinter.print(training.toString());
        }
    }
    /**
     * Method show information about all users trainings for the selected period
     * @param login login of the selected user
     */
    public void showAllTrainingsForThePeriod(String login) {
        ConsolePrinter.print("Enter start period day. Date format YYYY-MM-DD");
        LocalDate dateStart = LocalDate.parse(reader.read());
        ConsolePrinter.print("Enter end period day. Date format YYYY-MM-DD");
        LocalDate dateEnd = LocalDate.parse(reader.read());
        List<Training> result = trainingDao.getTrainingsFromThePeriod(login, dateStart, dateEnd);
        for (Training training : result) {
            ConsolePrinter.print(training.toString());
        }
    }
    /**
     * Method change information about one training of the selected user
     * @param login login of the selected user
     */
    public void changeTheTraining(String login) {
        ConsolePrinter.print("Enter the day of the changing training. Date format YYYY-MM-DD");
        LocalDate date = LocalDate.parse(reader.read());
        ConsolePrinter.print("Enter the type of the changing training");
        String type = reader.read();
        ConsolePrinter.print("What do you want to change?");
        ConsolePrinter.print("Enter 1 if it is date");
        ConsolePrinter.print("Enter 2 if it is type");
        ConsolePrinter.print("Enter 3 if it is time");
        ConsolePrinter.print("Enter 4 if it is calories");
        ConsolePrinter.print("Enter 5 if it is additional info");
        String userAnswer = reader.read();
        Training training = trainingDao.getTraining(login, date, type);
        if (training == null) {
            ConsolePrinter.print("this training doe not exists");
            return;
        }
        trainingDao.deleteTraining(login, date, type);
        LocalDate newDate = training.getDate();
        String newType = training.getType();
        double newTimeInMinutes = training.getTimeInMinutes();
        int newCalories = training.getCalories();
        String newAdditionalInfo = training.getAdditionalInfo();
        switch (userAnswer) {
            case ("1") -> {
                ConsolePrinter.print("Enter new date. Date format YYYY-MM-DD");
                newDate = LocalDate.parse(reader.read());
            }
            case ("2") -> {
                Set<String> trainingTypes = trainingDao.getTrainingTypes();
                ConsolePrinter.print("Enter new type of your training from " + trainingTypes);
                newType = reader.read();
            }
            case ("3") -> {
                ConsolePrinter.print("Enter new time your training");
                newTimeInMinutes = Double.parseDouble(reader.read());
            }
            case ("4") -> {
                ConsolePrinter.print("Enter time your calories");
                newCalories = Integer.parseInt(reader.read());
            }
            case ("5") -> {
                ConsolePrinter.print("If you want to add additional info, enter it here");
                newAdditionalInfo = reader.read();
            }
            default -> ConsolePrinter.print("Wrong enter. Try again.");
        }

        Training newTraining = new Training(newDate, newType, newTimeInMinutes, newCalories, newAdditionalInfo);
        trainingDao.addNewTraining(login, newTraining);
    }
    /**
     * Method shows how much time the user spent on training for selected period
     * @param login login of the selected user
     */
    public double allTimeForPeriod(String login){
        double sum = 0;
        ConsolePrinter.print("Enter start period day. Date format YYYY-MM-DD");
        LocalDate dateStart = LocalDate.parse(reader.read());
        ConsolePrinter.print("Enter end period day");
        LocalDate dateEnd = LocalDate.parse(reader.read());
        List<Training> result = trainingDao.getTrainingsFromThePeriod(login, dateStart, dateEnd);
        for (Training training : result) {
            sum = sum + training.getTimeInMinutes();
        }
        return sum;
    }
    /**
     * Method shows how much calories the user burned on trainings for selected period
     * @param login login of the selected user
     */
    public int allCaloriesForPeriod(String login) {
        int sum = 0;
        ConsolePrinter.print("Enter start period day. Date format YYYY-MM-DD");
        LocalDate dateStart = LocalDate.parse(reader.read());
        ConsolePrinter.print("Enter end period day");
        LocalDate dateEnd = LocalDate.parse(reader.read());
        List<Training> result = trainingDao.getTrainingsFromThePeriod(login, dateStart, dateEnd);
        for (Training training : result) {
            sum = sum + training.getCalories();
        }
        return sum;
    }
}
