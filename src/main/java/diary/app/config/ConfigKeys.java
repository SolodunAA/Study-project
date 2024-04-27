package diary.app.config;

/**
 * class that contains configuration constants
 */
public class ConfigKeys {
    public static final String USE_DB = "use_db";
    public static final String DB_URL = "db_url";
    public static final String DB_PASSWORD = "db_password";
    public static final String DB_USER = "db_user";
    public static final String DB_DATA_SCHEMA = "db_data_schema";
    public static final String DB_ADMIN_SCHEMA = "db_admin_schema";
    /**
     * description of constants and their values
     */
    public static final String ADMIN_LOGIN = "admin_login";
    public static final String ADMIN_ENCRYPTED_PASSWORD = "admin_encrypted_password";
    public static final String DEFAULT_TRAINING_TYPES = "training_types";

    /**
     * private construction prohibits creating instance of the class
     */
    private ConfigKeys() {
        //class to hold constants.
    }
}
