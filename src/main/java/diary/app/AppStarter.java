package diary.app;

import diary.app.factory.*;

public class AppStarter {

    public static void main(String[] args) {
        DaoFactory daoFactory = DaoFactoryConfig.resolveDaoFactoryAndRunMigrations();
        AuxiliaryFunctionsFactory auxiliaryFunctionsFactory= new AuxiliaryFunctionsFactory( daoFactory);
        ServicesFactory servicesFactory = new ServicesFactory( auxiliaryFunctionsFactory, daoFactory);
        DiaryApp diaryApp = new DiaryApp(daoFactory, servicesFactory);
        diaryApp.run();
    }

}


