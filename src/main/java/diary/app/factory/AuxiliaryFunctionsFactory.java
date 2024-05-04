package diary.app.factory;

import diary.app.auxiliaryfunctions.HashEncoder;

public class AuxiliaryFunctionsFactory {
    private final HashEncoder hashEncoder;
    private final DaoFactory daoFactory;

    public AuxiliaryFunctionsFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.hashEncoder = new HashEncoder();
    }

    public HashEncoder getHashEncoder() {
        return hashEncoder;
    }
    public DaoFactory getDaoFactory() {
        return daoFactory;
    }
}
