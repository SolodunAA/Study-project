package diary.app;

import diary.app.factory.*;

public class AppStarter {

    public static void main(String[] args) {
        DaoFactory daoFactory = DaoFactoryConfig.resolveDaoFactoryAndRunMigrations();
        InOutFactory inOutFactory = new InOutFactory();
        AuxiliaryFunctionsFactory auxiliaryFunctionsFactory= new AuxiliaryFunctionsFactory(inOutFactory, daoFactory);
        ServicesFactory servicesFactory = new ServicesFactory(inOutFactory, auxiliaryFunctionsFactory, daoFactory);
        DiaryApp diaryApp = new DiaryApp(daoFactory, inOutFactory, servicesFactory);
        diaryApp.run();
    }

}


