package diary.app.factory;

import diary.app.auxiliaryfunctions.HashEncoder;
import diary.app.auxiliaryfunctions.TrainingInteractions;
import diary.app.dao.InMemoryTrainingDao;

public class AuxiliaryFunctionsFactory {
    private final HashEncoder hashEncoder;
    private final TrainingInteractions trainingInteractions;
    private final InOutFactory inOutFactory;
    private final DaoFactory daoFactory;

    public AuxiliaryFunctionsFactory(InOutFactory inOutFactory, DaoFactory daoFactory) {
        this.inOutFactory = inOutFactory;
        this.daoFactory = daoFactory;
        this.hashEncoder = new HashEncoder();
        this.trainingInteractions = new TrainingInteractions(inOutFactory.getReader(),
                daoFactory.getTrainingDao());
    }

    public HashEncoder getHashEncoder() {
        return hashEncoder;
    }

    public TrainingInteractions getTrainingInteractions() {
        return trainingInteractions;
    }

    public InOutFactory getInOutFactory() {
        return inOutFactory;
    }

    public DaoFactory getDaoFactory() {
        return daoFactory;
    }
}
