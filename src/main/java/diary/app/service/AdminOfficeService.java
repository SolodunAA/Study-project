package diary.app.service;

import diary.app.auxiliaryfunctions.TrainingInteractions;
import diary.app.dao.AuditDao;
import diary.app.dao.TrainingDao;
import diary.app.dao.UserRolesDao;
import diary.app.dto.AuditItem;
import diary.app.dto.Role;
import diary.app.dto.UserAction;
import diary.app.in.Reader;
import diary.app.out.ConsolePrinter;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdminOfficeService {

    private final Reader reader;
    private final AuditDao auditDao;
    private  final UserRolesDao userRolesDao;
    private final TrainingDao trainingDao;
    private final TrainingInteractions trainingInteractions;

    public AdminOfficeService(Reader reader, AuditDao auditDao, UserRolesDao userRolesDao, TrainingDao trainingDao, TrainingInteractions trainingInteractions) {
        this.reader = reader;
        this.auditDao = auditDao;
        this.userRolesDao = userRolesDao;
        this.trainingDao = trainingDao;
        this.trainingInteractions = trainingInteractions;
    }

    public void editUserTrainings(String currentUser, String userToChangeFor) {
        ConsolePrinter.print("Enter 1 if you want to add new training");
        ConsolePrinter.print("Enter 2 if you want to delete new training");
        ConsolePrinter.print("Enter 5 if you want to change some training");
        String userAnswer = reader.read();
        auditDao.addAuditItem(new AuditItem(currentUser, currentUser + " changed trainings for " + userToChangeFor, userAnswer));
        switch (userAnswer) {
            case ("1") -> trainingInteractions.addNewTraining(userToChangeFor);
            case ("2") -> trainingInteractions.deleteTheTraining(userToChangeFor);
            case ("5") -> trainingInteractions.changeTheTraining(userToChangeFor);
            default -> ConsolePrinter.print("Wrong enter. Try again.");
        }
    }

    public void seeUserTrainings(String currentUser, String userToGetInfo) {
        ConsolePrinter.print("Enter 3 if you want to see all trainings");
        ConsolePrinter.print("Enter 4 if you want to see training for the period");
        ConsolePrinter.print("Enter 6 if you want to see number of calories burned during the period");
        ConsolePrinter.print("Enter 7 if you want to see total amount of training time for the period");
        String userAnswer = reader.read();
        auditDao.addAuditItem(new AuditItem(currentUser, currentUser + " saw trainings for " + userToGetInfo, userAnswer));
        switch (userAnswer) {
            case ("3") -> trainingInteractions.showAllTrainings(userToGetInfo);
            case ("4") -> trainingInteractions.showAllTrainingsForThePeriod(userToGetInfo);
            case ("6") -> ConsolePrinter.print("Number of burned calories = " + trainingInteractions.allCaloriesForPeriod(userToGetInfo));
            case ("7") -> ConsolePrinter.print("Number of wasting time = " + trainingInteractions.allTimeForPeriod(userToGetInfo));
            default -> ConsolePrinter.print("Wrong enter. Try again.");
        }
    }

    public void getAudit(String login) {
        ConsolePrinter.print("Number of latest audit items to print. Max limit is " + auditDao.AuditItemsSize());
        String countStr = reader.read();
        auditDao.addAuditItem(new AuditItem(login, "requested audit", "items to print " + countStr));
        int count = Integer.parseInt(countStr);
        auditDao.getAuditItems(count).forEach(item -> ConsolePrinter.print(item.toString()));
    }

    public void changeUserPermissions(String login) {
        ConsolePrinter.print("Enter username to change permissions");
        String username = reader.read();
        ConsolePrinter.print("Enter comma separated aliases from below map to give to this user");
        for (UserAction action : UserAction.values()) {
            ConsolePrinter.print("alias=" + action.getActionAlias() + " action=" + action.name());
        }
        String csvAliases = reader.read();
        var actionsToAdd = Arrays.stream(csvAliases.split(","))
                .map(UserAction::actionByAlias)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        auditDao.addAuditItem(new AuditItem(login, "changed permissions for " + username, actionsToAdd.toString()));
        userRolesDao.addRoleForUser(username, new Role(actionsToAdd));
    }

    public void changeAppSettings(String login) {
        ConsolePrinter.print("Enter 1 if you want to add new training type");
        ConsolePrinter.print("Enter 2 if you want to delete training type");
        String actionCase = reader.read();
        ConsolePrinter.print("Enter training type");
        String trainingType = reader.read();
        auditDao.addAuditItem(new AuditItem(login, "changed app settings", "action=" + actionCase + " input=" + trainingType));
        switch (actionCase) {
            case ("1") -> trainingDao.addTrainingType(trainingType);
            case ("2") -> trainingDao.deleteTrainingType(trainingType);
            default -> ConsolePrinter.print("Wrong enter. Try again.");
        }
    }

}
