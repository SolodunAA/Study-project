package diary.app.factory;

import diary.app.dao.*;

public class DaoFactory {
    private final AuditDao auditDao;
    private final LoginDao loginDao;
    private final TrainingDao trainingDao;
    private final UserRolesDao userRolesDao;

    public DaoFactory() {
        this.auditDao = new InMemoryAuditDao();
        this.loginDao = new InMemoryLoginDao();
        this.trainingDao = new InMemoryTrainingDao();
        this.userRolesDao = new InMemoryRolesDao();
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
