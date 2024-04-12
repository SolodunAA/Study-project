package diary.app.service;

import diary.app.dao.AuditDao;
import diary.app.dto.AuditItem;
import diary.app.dto.Training;
import diary.app.dao.TrainingDao;
import diary.app.dao.UserRolesDao;
import diary.app.dto.Role;
import diary.app.in.ConsoleReader;
import diary.app.out.ConsolePrinter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserOfficeService {
    private final TrainingDao trainingDao;
    private final UserRolesDao userRolesDao;
    private final AuditDao auditDao;
    private final ConsoleReader consoleReader;

    public UserOfficeService(TrainingDao trainingDao,
                             UserRolesDao userRolesDao,
                             AuditDao auditDao,
                             ConsoleReader consoleReader) {
        this.trainingDao = trainingDao;
        this.userRolesDao = userRolesDao;
        this.auditDao = auditDao;
        this.consoleReader = consoleReader;
    }

    public void run(String login, Role role) {
        ConsolePrinter.print("Welcome " + login + " to your personal account!");
        ConsolePrinter.print("Here you can add your trainings and see your statistics.");
        ConsolePrinter.print("Let's start!");
        boolean exit = false;
        while (!exit) {
            var allowedActions = role.getAllowedActions();
            allowedActions.forEach(action -> ConsolePrinter.print(action.getActionDescriptionForUser()));
            String userAnswer = consoleReader.read();
            var actionOptional = UserAction.actionByAlias(userAnswer);

            if (actionOptional.isEmpty() || !role.isActionAllowed(actionOptional.get())) {
                ConsolePrinter.print("Unknown action. Please try again");
            } else {
                switch (actionOptional.get()) {

                    case SEE_USER_TRAININGS -> seeUserTrainings(login, login);
                    case EDIT_USER_TRAININGS -> editUserTrainings(login, login);
                    case SEE_OTHER_USERS_TRAININGS -> {
                        ConsolePrinter.print("Enter username to see trainings");
                        seeUserTrainings(login, consoleReader.read());
                    }
                    case EDIT_OTHER_USERS_TRAININGS -> {
                        ConsolePrinter.print("Enter username to edit trainings");
                        editUserTrainings(login, consoleReader.read());
                    }
                    case CHANGE_APP_SETTINGS -> changeAppSettings(login);
                    case CHANGE_USER_PERMISSIONS -> changeUserPermissions(login);
                    case GET_AUDIT -> getAudit(login);
                    case EXIT -> exit = true;
                }
            }
        }
        auditDao.addAuditItem(new AuditItem(login, "EXIT", "exit"));
    }

    private void getAudit(String login) {
        ConsolePrinter.print("number of latest audit items to print");
        String countStr = consoleReader.read();
        auditDao.addAuditItem(new AuditItem(login, "requested audit", "items to print " + countStr));
        int count = Integer.parseInt(countStr);
        auditDao.getAuditItems(count).forEach(item -> ConsolePrinter.print(item.toString()));
    }

    private void changeUserPermissions(String login) {
        ConsolePrinter.print("Enter username to change permissions");
        String username = consoleReader.read();
        ConsolePrinter.print("Enter comma separated aliases from below map to give to this user");
        for (UserAction action : UserAction.values()) {
            ConsolePrinter.print("alias=" + action.getActionAlias() + " action=" + action.name());
        }
        String csvAliases = consoleReader.read();
        var actionsToAdd = Arrays.stream(csvAliases.split(","))
                .map(UserAction::actionByAlias)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        auditDao.addAuditItem(new AuditItem(login, "changed permissions for " + username, actionsToAdd.toString()));
        userRolesDao.addRoleForUser(username, new Role(actionsToAdd));
    }

    private void changeAppSettings(String login) {
        ConsolePrinter.print("Enter 1 if you want to add new training type");
        ConsolePrinter.print("Enter 2 if you want to delete training type");
        String actionCase = consoleReader.read();
        ConsolePrinter.print("Enter training type");
        String trainingType = consoleReader.read();
        auditDao.addAuditItem(new AuditItem(login, "changed app settings", "action=" + actionCase + " input=" + trainingType));
        switch (actionCase) {
            case ("1") -> trainingDao.addTrainingType(trainingType);
            case ("2") -> trainingDao.deleteTrainingType(trainingType);
        }
    }

    private void editUserTrainings(String currentUser, String userToChangeFor) {
        ConsolePrinter.print("Enter 1 if you want to add new training");
        ConsolePrinter.print("Enter 2 if you want to delete new training");
        ConsolePrinter.print("Enter 5 if you want to change some training");
        String userAnswer = consoleReader.read();
        auditDao.addAuditItem(new AuditItem(currentUser, currentUser + " changed trainings for " + userToChangeFor, userAnswer));
        switch (userAnswer) {
            case ("1") -> addNewTraining(userToChangeFor);
            case ("2") -> deleteTheTraining(userToChangeFor);
            case ("5") -> changeTheTraining(userToChangeFor);
        }
    }

    private void seeUserTrainings(String currentUser, String userToGetInfo) {
        ConsolePrinter.print("Enter 3 if you want to see all trainings");
        ConsolePrinter.print("Enter 4 if you want to see training for the period");
        ConsolePrinter.print("Enter 6 if you want to see number of calories burned during the period");
        ConsolePrinter.print("Enter 7 if you want to see total amount of training time for the period");
        String userAnswer = consoleReader.read();
        auditDao.addAuditItem(new AuditItem(currentUser, currentUser + " saw trainings for " + userToGetInfo, userAnswer));
        switch (userAnswer) {
            case ("3") -> showAllTrainings(userToGetInfo);
            case ("4") -> showAllTrainingsForThePeriod(userToGetInfo);
            case ("6") -> ConsolePrinter.print("Number of burned calories = " + allCaloriesForPeriod(userToGetInfo));
            case ("7") -> ConsolePrinter.print("Number of wasting time = " + allTimeForPeriod(userToGetInfo));
        }
    }

    private double allTimeForPeriod(String login){
        double sum = 0;
        ConsolePrinter.print("Enter start period day");
        LocalDate dateStart = LocalDate.parse(consoleReader.read());
        ConsolePrinter.print("Enter end period day");
        LocalDate dateEnd = LocalDate.parse(consoleReader.read());
        List<Training> result = trainingDao.getTrainingsFromThePeriod(login, dateStart, dateEnd);
        for (Training training : result) {
            sum = sum + training.getTimeInMinutes();
        }
        return sum;
    }

    private int allCaloriesForPeriod(String login) {
        int sum = 0;
        ConsolePrinter.print("Enter start period day");
        LocalDate dateStart = LocalDate.parse(consoleReader.read());
        ConsolePrinter.print("Enter end period day");
        LocalDate dateEnd = LocalDate.parse(consoleReader.read());
        List<Training> result = trainingDao.getTrainingsFromThePeriod(login, dateStart, dateEnd);
        for (Training training : result) {
            sum = sum + training.getCalories();
        }
        return sum;
    }

    private void changeTheTraining(String login) {
        ConsolePrinter.print("Enter the day of the changing training");
        LocalDate date = LocalDate.parse(consoleReader.read());
        ConsolePrinter.print("Enter the type of the changing training");
        String type = consoleReader.read();
        ConsolePrinter.print("What fo you want to change?");
        ConsolePrinter.print("Enter 1 if it is date");
        ConsolePrinter.print("Enter 2 if it is type");
        ConsolePrinter.print("Enter 3 if it is time");
        ConsolePrinter.print("Enter 4 if it is calories");
        ConsolePrinter.print("Enter 5 if it is additional info");
        String userAnswer = consoleReader.read();
        Training training = trainingDao.getTraining(login, date, type);
        trainingDao.deleteTraining(login, date, type);
        LocalDate newDate = training.getDate();
        String newType = training.getType();
        double newTimeInMinutes = training.getTimeInMinutes();
        int newCalories = training.getCalories();
        String newAdditionalInfo = training.getAdditionalInfo();
        switch (userAnswer) {
            case ("1") -> {
                ConsolePrinter.print("Enter new date");
                newDate = LocalDate.parse(consoleReader.read());
            }
            case ("2") -> {
                Set<String> trainingTypes = trainingDao.getTrainingTypes();
                ConsolePrinter.print("Enter new type of your training from " + trainingTypes);
                newType = consoleReader.read();
            }
            case ("3") -> {
                ConsolePrinter.print("Enter new time your training");
                newTimeInMinutes = Double.parseDouble(consoleReader.read());
            }
            case ("4") -> {
                ConsolePrinter.print("Enter time your calories");
                newCalories = Integer.parseInt(consoleReader.read());
            }
            case ("5") -> {
                ConsolePrinter.print("If you want to add additional info, enter it here");
                newAdditionalInfo = consoleReader.read();
            }
        }

        Training newTraining = new Training(newDate, newType, newTimeInMinutes, newCalories, newAdditionalInfo);
        trainingDao.addNewTraining(login, newTraining);
    }

    private void showAllTrainingsForThePeriod(String login) {
        ConsolePrinter.print("Enter start period day");
        LocalDate dateStart = LocalDate.parse(consoleReader.read());
        ConsolePrinter.print("Enter end period day");
        LocalDate dateEnd = LocalDate.parse(consoleReader.read());
        List<Training> result = trainingDao.getTrainingsFromThePeriod(login, dateStart, dateEnd);
        for (Training training : result) {
            ConsolePrinter.print(training.toString());
        }
    }

    private void showAllTrainings(String login) {
        List<Training> result = trainingDao.getAllTrainings(login);
        for (Training training : result) {
            ConsolePrinter.print(training.toString());
        }
    }

    private void deleteTheTraining(String login) {
        ConsolePrinter.print("Which training you want to delete?");
        ConsolePrinter.print("Enter the date of the training");
        LocalDate date = LocalDate.parse(consoleReader.read());
        ConsolePrinter.print("Enter type of your training");
        String type = consoleReader.read();
        trainingDao.deleteTraining(login, date, type);
    }

    private void addNewTraining(String login) {
        Set<String> trainingTypes = trainingDao.getTrainingTypes();
        ConsolePrinter.print("Let's start to add new training");
        ConsolePrinter.print("Enter date");
        LocalDate date = LocalDate.parse(consoleReader.read());
        ConsolePrinter.print("Enter type of your training. Valid types=" + trainingTypes);
        String type = consoleReader.read();
        ConsolePrinter.print("Enter time your training");
        double timeInMinutes = Double.parseDouble(consoleReader.read());
        ConsolePrinter.print("Enter waste your calories");
        int calories = Integer.parseInt(consoleReader.read());
        ConsolePrinter.print("If you want to add additional info, enter it here");
        String additionalInfo = consoleReader.read();
        Training newTraining = new Training(date, type, timeInMinutes, calories, additionalInfo);
        trainingDao.addNewTraining(login, newTraining);
    }
}
