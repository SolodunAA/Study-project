package diary.app.factory;

import diary.app.config.ConfigKeys;
import diary.app.config.ConfigReader;
import diary.app.dto.Role;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DaoFactoryConfig {

    public static DaoFactory resolveDaoFactoryAndRunMigrations() {
        ConfigReader configReader = new ConfigReader();
        Properties properties = configReader.readProperties();
        boolean useDb = properties.containsKey(ConfigKeys.USE_DB)
                && Boolean.parseBoolean(properties.getProperty(ConfigKeys.USE_DB));
        if (useDb) {
            System.out.println("creating postgres dao");
            String dbUrl = properties.getProperty(ConfigKeys.DB_URL);
            String dbUser = properties.getProperty(ConfigKeys.DB_USER);
            String dbPassword = properties.getProperty(ConfigKeys.DB_PASSWORD);
            String adminSchema = properties.getProperty(ConfigKeys.DB_ADMIN_SCHEMA);
            String dataSchema = properties.getProperty(ConfigKeys.DB_DATA_SCHEMA);
            runMigrations(dbUrl, dbUser ,dbPassword);
            return DaoFactory.createPostgreDaoFactory(dbUrl, dbUser, dbPassword, dataSchema, adminSchema);
        } else {
            System.out.println("creating in-memory dao");
            DaoFactory daoFactory = DaoFactory.createInMemoryDaoFactory();
            setInMemoryInitialConfigs(daoFactory, properties);
            return daoFactory;
        }
    }

    private static void runMigrations(String url, String user, String pswd) {
        try (Connection connection = DriverManager.getConnection(url, user, pswd)) {
            System.out.println("starting migration");
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase =
                    new Liquibase("db.changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("migration finished successfully");
        } catch (Exception e ){
            System.out.println("Got SQL Exception " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private static void setInMemoryInitialConfigs(DaoFactory daoFactory, Properties properties) {
        System.out.println("start in-memory dao initial filling form configs");
        String adminLogin = properties.getProperty(ConfigKeys.ADMIN_LOGIN);
        String adminEncryptedPasswordStr = properties.getProperty(ConfigKeys.ADMIN_ENCRYPTED_PASSWORD);
        int adminEncryptedPassword = Integer.parseInt(adminEncryptedPasswordStr);
        daoFactory.getLoginDao().addNewUser(adminLogin, adminEncryptedPassword);

        daoFactory.getUserRolesDao().addRoleForUser(adminLogin, Role.ADMIN);

        String[] trainingTypes = properties.getProperty(ConfigKeys.DEFAULT_TRAINING_TYPES).split(",");
        for (String trainingType : trainingTypes) {
            daoFactory.getTrainingDao().addTrainingType(trainingType);
        }
        System.out.println("finished in-memory dao initial filling");
    }
}
