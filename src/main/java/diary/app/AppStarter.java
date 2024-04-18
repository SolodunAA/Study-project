package diary.app;

import diary.app.config.ConfigKeys;
import diary.app.config.ConfigReader;
import diary.app.dto.Role;
import diary.app.factory.AuxiliaryFunctionsFactory;
import diary.app.factory.DaoFactory;
import diary.app.factory.InOutFactory;
import diary.app.factory.ServicesFactory;

import java.util.Properties;

public class AppStarter {
    public static final DaoFactory daoFactory = new DaoFactory();
    public static final InOutFactory inOutFactory = new InOutFactory();
    public static final AuxiliaryFunctionsFactory auxiliaryFunctionsFactory= new AuxiliaryFunctionsFactory(inOutFactory, daoFactory);
    public static final ServicesFactory servicesFactory = new ServicesFactory(inOutFactory, auxiliaryFunctionsFactory, daoFactory);

    public static void main(String[] args) {
        setInitialConfigs();
        DiaryApp diaryApp = new DiaryApp(daoFactory, inOutFactory, servicesFactory);
        diaryApp.run();
    }
    private static void setInitialConfigs() {
        ConfigReader configReader = new ConfigReader();
        Properties properties = configReader.readProperties();

        String adminLogin = properties.getProperty(ConfigKeys.ADMIN_LOGIN);
        String adminEncryptedPasswordStr = properties.getProperty(ConfigKeys.ADMIN_ENCRYPTED_PASSWORD);
        int adminEncryptedPassword = Integer.parseInt(adminEncryptedPasswordStr);
        daoFactory.getLoginDao().addNewUser(adminLogin, adminEncryptedPassword);

        daoFactory.getUserRolesDao().addRoleForUser(adminLogin, Role.ADMIN);

        String[] trainingTypes = properties.getProperty(ConfigKeys.DEFAULT_TRAINING_TYPES).split(",");
        for (String trainingType : trainingTypes) {
            daoFactory.getTrainingDao().addTrainingType(trainingType);
        }
    }
}


