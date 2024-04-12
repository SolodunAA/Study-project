package diary.app;

import diary.app.config.ConfigKeys;
import diary.app.auxiliaryfunctions.HashEncoder;
import diary.app.auxiliaryfunctions.PasswordEncoder;
import diary.app.config.ConfigReader;
import diary.app.dao.*;
import diary.app.dto.Role;
import diary.app.in.ConsoleReader;
import diary.app.service.*;

import java.util.Properties;

public class AppStarter {

    public static void main(String[] args) {
        AuditDao auditDao = new InMemoryAuditDao();
        TrainingDao trainingDao = new InMemoryTrainingDao();
        PasswordEncoder passwordEncoder = new HashEncoder();
        LoginDao loginDao = new InMemoryLoginDao();
        UserRolesDao userRolesDao = new InMemoryRolesDao();
        ConsoleReader consoleReader = new ConsoleReader();
        setInitialConfigs(loginDao, userRolesDao, trainingDao);
        AuthenticationService authenticationService =
                new AuthenticationServiceImpl(loginDao, passwordEncoder, auditDao);
        RegistrationService registrationService =
                new RegistrationServiceImpl(passwordEncoder, loginDao, userRolesDao, auditDao, consoleReader);
        UserOfficeService userOffice =
                new UserOfficeService(trainingDao, userRolesDao, auditDao, consoleReader);
        DiaryApp diaryApp =
                new DiaryApp(userRolesDao, registrationService, authenticationService, userOffice, auditDao, consoleReader);
        diaryApp.run();
    }

    private static void setInitialConfigs(LoginDao loginDao,
                                          UserRolesDao userRolesDao,
                                          TrainingDao trainingDao) {
        ConfigReader configReader = new ConfigReader();
        Properties properties = configReader.readProperties();

        String adminLogin = properties.getProperty(ConfigKeys.ADMIN_LOGIN);
        String adminEncryptedPasswordStr = properties.getProperty(ConfigKeys.ADMIN_ENCRYPTED_PASSWORD);
        int adminEncryptedPassword = Integer.parseInt(adminEncryptedPasswordStr);
        loginDao.addNewUser(adminLogin, adminEncryptedPassword);

        userRolesDao.addRoleForUser(adminLogin, Role.ADMIN);

        String[] trainingTypes = properties.getProperty(ConfigKeys.DEFAULT_TRAINING_TYPES).split(",");
        for (String trainingType : trainingTypes) {
            trainingDao.addTrainingType(trainingType);
        }
    }

}
