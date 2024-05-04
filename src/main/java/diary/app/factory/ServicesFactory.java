package diary.app.factory;

import diary.app.service.*;
import diary.app.service.Impl.AuthenticationServiceImpl;
import diary.app.service.Impl.RegistrationServiceImpl;
import diary.app.service.Impl.TokenServiceImpl;
import diary.app.service.Impl.TrainingServiceImpl;

public class ServicesFactory {
    private final AuthenticationService authenticationService;
    private final RegistrationService registrationService;
    private final AuxiliaryFunctionsFactory auxiliaryFunctionsFactory;
    private final DaoFactory daoFactory;
    private final TrainingServiceImpl trainingServiceImpl;
    private final TokenServiceImpl tokenService;

    public ServicesFactory(AuxiliaryFunctionsFactory auxiliaryFunctionsFactory, DaoFactory daoFactory){
        this.auxiliaryFunctionsFactory = auxiliaryFunctionsFactory;
        this.daoFactory = daoFactory;
        this.authenticationService = new AuthenticationServiceImpl(daoFactory.getLoginDao(), auxiliaryFunctionsFactory.getHashEncoder());
        this.registrationService = new RegistrationServiceImpl(auxiliaryFunctionsFactory.getHashEncoder(), daoFactory.getLoginDao(), daoFactory.getUserRolesDao());
        this.trainingServiceImpl = new TrainingServiceImpl(daoFactory.getTrainingDao());
        this.tokenService = new TokenServiceImpl(daoFactory.getTokenDao());
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public RegistrationService getRegistrationService() {
        return registrationService;
    }

    public TrainingServiceImpl getTrainingService() {
        return trainingServiceImpl;
    }

    public TokenServiceImpl getTokenService() {
        return tokenService;
    }

    public DaoFactory getDaoFactory() {
        return daoFactory;
    }
}
