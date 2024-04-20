package diary.app.service;

import diary.app.dao.*;
import diary.app.dto.Role;
import diary.app.dto.Training;
import diary.app.in.ConsoleReader;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class UserOfficeServiceTest {
    ConsoleReader crMock = Mockito.mock(ConsoleReader.class);
    @Test
    public void exitTest(){
        TrainingDao trainingDao = new InMemoryTrainingDao();
        UserRolesDao userRolesDao = new InMemoryRolesDao();
        AuditDao auditDao = new InMemoryAuditDao();
        DefaultUserOfficeService defaultUserOfficeService = new DefaultUserOfficeService(trainingDao, crMock, auditDao, trainingInteractions);
        AdminOfficeService adminOfficeService = new AdminOfficeService(crMock, auditDao, userRolesDao, trainingDao, defaultUserOfficeService, trainingInteractions);
        UserOfficeService userOfficeService = new UserOfficeService(defaultUserOfficeService, adminOfficeService, crMock, reader, auditDao);

        Mockito.when(crMock.read()).thenReturn("exit");

        userOfficeService.run("login", Role.DEFAULT_USER);

        assertEquals(1, auditDao.getAuditItems(10).size());
    }

    @Test
    public void addTrainingTest(){
        TrainingDao trainingDao = new InMemoryTrainingDao();
        UserRolesDao userRolesDao = new InMemoryRolesDao();
        AuditDao auditDao = new InMemoryAuditDao();
        DefaultUserOfficeService defaultUserOfficeService = new DefaultUserOfficeService(trainingDao, crMock, auditDao, trainingInteractions);
        AdminOfficeService adminOfficeService = new AdminOfficeService(crMock, auditDao, userRolesDao, trainingDao, defaultUserOfficeService, trainingInteractions);
        UserOfficeService userOfficeService = new UserOfficeService(defaultUserOfficeService, adminOfficeService, crMock, reader, auditDao);

        String user = "login";
        String type = "swimming";
        String date = "2024-04-12";
        String time = "10";
        String calories = "234";
        String extraInfo = "nothing";

        trainingDao.addTrainingType(type);

        Mockito.when(crMock.read()).thenReturn("2", "1", date, type, time, calories, extraInfo, "exit");

        userOfficeService.run(user, Role.DEFAULT_USER);

        assertEquals(2, auditDao.getAuditItems(100).size());
        Training actualTraining = trainingDao.getTraining(user, LocalDate.parse(date), type);
        assertEquals(actualTraining.getDate(), LocalDate.parse(date));
        assertEquals(actualTraining.getType(), type);
        assertEquals(actualTraining.getTimeInMinutes(), Double.parseDouble(time), 0.001);
        assertEquals(actualTraining.getCalories(), Integer.parseInt(calories));
        assertEquals(actualTraining.getAdditionalInfo(), extraInfo);
    }
}
