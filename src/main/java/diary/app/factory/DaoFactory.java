package diary.app.factory;

import diary.app.dao.*;
import diary.app.dao.postgres.*;

public class DaoFactory {
    private final AuditDao auditDao;
    private final LoginDao loginDao;
    private final TrainingDao trainingDao;
    private final UserRolesDao userRolesDao;
    private final TokenDao tokenDao;

    private DaoFactory(String url,
                       String user,
                       String pswd,
                       String dataSchema,
                       String adminSchema) {
        this.auditDao = new PostgresAuditDao(url, user, pswd, adminSchema);
        this.loginDao = new PostgresLoginDao(url, user, pswd, adminSchema);
        this.userRolesDao = new PostgresUserRolesDao(url, user, pswd, adminSchema);
        this.trainingDao = new PostgresTrainingDao(url, user, pswd, dataSchema);
        this.tokenDao = new PostgresTokenDao(url, user, pswd, adminSchema);
    }

    public static DaoFactory createPostgreDaoFactory(String url,
                                                     String user,
                                                     String pswd,
                                                     String dataSchema,
                                                     String adminSchema) {
        return new DaoFactory(url, user, pswd, dataSchema, adminSchema);
    }

    public AuditDao getAuditDao() {
        return auditDao;
    }

    public LoginDao getLoginDao() {
        return loginDao;
    }

    public TrainingDao getTrainingDao() {
        return trainingDao;
    }

    public UserRolesDao getUserRolesDao() {
        return userRolesDao;
    }

    public TokenDao getTokenDao() {
        return tokenDao;
    }
}
