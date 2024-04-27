package diary.app.factory;

import diary.app.service.*;

public class ServicesFactory {
    private final AuthenticationService authenticationService;
    private final RegistrationService registrationService;
    private final UserOfficeService userOfficeService;
    private final DefaultUserOfficeService defaultUserOfficeService;
    private final AdminOfficeService adminOfficeService;

    private final InOutFactory inOutFactory;
    private final AuxiliaryFunctionsFactory auxiliaryFunctionsFactory;
    private final DaoFactory daoFactory;
    private final TrainingService trainingService;
    private final TokenService tokenService;

    public ServicesFactory(InOutFactory inOutFactory, AuxiliaryFunctionsFactory auxiliaryFunctionsFactory, DaoFactory daoFactory){
        this.inOutFactory = inOutFactory;
        this.auxiliaryFunctionsFactory = auxiliaryFunctionsFactory;
        this.daoFactory = daoFactory;
        this.authenticationService = new AuthenticationServiceImpl(daoFactory.getLoginDao(), auxiliaryFunctionsFactory.getHashEncoder());
        this.registrationService = new RegistrationServiceImpl(auxiliaryFunctionsFactory.getHashEncoder(), daoFactory.getLoginDao(), daoFactory.getUserRolesDao());
        this.defaultUserOfficeService = new DefaultUserOfficeService(inOutFactory.getReader(), daoFactory.getAuditDao(), auxiliaryFunctionsFactory.getTrainingInteractions());
        this.adminOfficeService = new AdminOfficeService(inOutFactory.getReader(), daoFactory.getAuditDao(), daoFactory.getUserRolesDao(), daoFactory.getTrainingDao(), auxiliaryFunctionsFactory.getTrainingInteractions());
        this.userOfficeService = new UserOfficeService(this.defaultUserOfficeService, this.adminOfficeService, inOutFactory.getReader(), daoFactory.getAuditDao());
        this.trainingService = new TrainingService(daoFactory.getTrainingDao());
        this.tokenService = new TokenService(daoFactory.getTokenDao());
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public RegistrationService getRegistrationService() {
        return registrationService;
    }

    public UserOfficeService getUserOfficeService() {
        return userOfficeService;
    }

    public DefaultUserOfficeService getDefaultUserOfficeService() {
        return defaultUserOfficeService;
    }

    public AdminOfficeService getAdminOfficeService() {
        return adminOfficeService;
    }

    public TrainingService getTrainingService() {
        return trainingService;
    }

    public TokenService getTokenService() {
        return tokenService;
    }

    public DaoFactory getDaoFactory() {
        return daoFactory;
    }
}
