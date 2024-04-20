package diary.app.service;

import diary.app.auxiliaryfunctions.TrainingInteractions;
import diary.app.dao.AuditDao;
import diary.app.dao.TrainingDao;
import diary.app.dto.AuditItem;
import diary.app.in.ConsoleReader;
import diary.app.in.Reader;
import diary.app.out.ConsolePrinter;

public class DefaultUserOfficeService {
    private final Reader reader;
    private final AuditDao auditDao;
    private final TrainingInteractions trainingInteractions;

    public DefaultUserOfficeService(Reader reader, AuditDao auditDao, TrainingInteractions trainingInteractions) {
        this.reader = reader;
        this.auditDao = auditDao;
        this.trainingInteractions = trainingInteractions;
    }


    public void seeUserTrainings(String login) {
        ConsolePrinter.print("Enter 3 if you want to see all your trainings");
        ConsolePrinter.print("Enter 4 if you want to see your training for the period");
        ConsolePrinter.print("Enter 6 if you want to see number of burned calories during the period");
        ConsolePrinter.print("Enter 7 if you want to see total amount of training time for the period");
        String userAnswer = reader.read();
        auditDao.addAuditItem(new AuditItem(login, login + " saw trainings for " + login, userAnswer));
        switch (userAnswer) {
            case ("3") -> trainingInteractions.showAllTrainings(login);
            case ("4") -> trainingInteractions.showAllTrainingsForThePeriod(login);
            case ("6") -> ConsolePrinter.print("Number of burned calories = " + trainingInteractions.allCaloriesForPeriod(login));
            case ("7") -> ConsolePrinter.print("Number of wasting time = " + trainingInteractions.allTimeForPeriod(login));
            default -> ConsolePrinter.print("Wrong enter. Try again.");
        }
    }

    public void editUserTrainings(String login) {
        ConsolePrinter.print("Enter 1 if you want to add new training");
        ConsolePrinter.print("Enter 2 if you want to delete new training");
        ConsolePrinter.print("Enter 5 if you want to change some training");
        String userAnswer = reader.read();
        auditDao.addAuditItem(new AuditItem(login, login + " changed trainings for " + login, userAnswer));
        switch (userAnswer) {
            case ("1") -> trainingInteractions.addNewTraining(login);
            case ("2") -> trainingInteractions.deleteTheTraining(login);
            case ("5") -> trainingInteractions.changeTheTraining(login);
            default -> ConsolePrinter.print("Wrong enter. Try again.");
        }
    }


}
