package diary.app.config;
import diary.app.factory.*;

public class StaticConfig {

   public static final ServicesFactory SERVICES_FACTORY;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("can not create class for jdbc");
            throw new RuntimeException(e);
        }
        System.out.println("START INIT STATICS");
        DaoFactory daoFactory = DaoFactoryConfig.resolveDaoFactoryAndRunMigrations();
        System.out.println("INIT DAO");
        AuxiliaryFunctionsFactory auxiliaryFunctionsFactory= new AuxiliaryFunctionsFactory(daoFactory);
        SERVICES_FACTORY = new ServicesFactory(auxiliaryFunctionsFactory, daoFactory);
    }
}

