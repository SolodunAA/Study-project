package diary.app.factory;

import diary.app.dao.*;
import diary.app.dao.inmemory.InMemoryAuditDao;
import diary.app.dao.inmemory.InMemoryLoginDao;
import diary.app.dao.inmemory.InMemoryRolesDao;
import diary.app.dao.inmemory.InMemoryTrainingDao;
import diary.app.dao.postgres.PostgresAuditDao;
import diary.app.dao.postgres.PostgresLoginDao;
import diary.app.dao.postgres.PostgresTrainingDao;
import diary.app.dao.postgres.PostgresUserRolesDao;

public class DaoFactory {
    private final AuditDao auditDao;
    private final LoginDao loginDao;
    private final TrainingDao trainingDao;
    private final UserRolesDao userRolesDao;

    private DaoFactory() {
        this.auditDao = new InMemoryAuditDao();
        this.loginDao = new InMemoryLoginDao();
        this.trainingDao = new InMemoryTrainingDao();
        this.userRolesDao = new InMemoryRolesDao();
    }

    private DaoFactory(String url,
                       String user,
                       String pswd,
                       String dataSchema,
                       String adminSchema) {
        this.auditDao = new PostgresAuditDao(url, user, pswd, adminSchema);
        this.loginDao = new PostgresLoginDao(url, user, pswd, adminSchema);
        this.userRolesDao = new PostgresUserRolesDao(url, user, pswd, adminSchema);
        this.trainingDao = new PostgresTrainingDao(url, user, pswd, dataSchema);
    }

    public static DaoFactory createInMemoryDaoFactory() {
        return new DaoFactory();
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
}
