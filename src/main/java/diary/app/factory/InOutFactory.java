package diary.app.factory;

import diary.app.in.ConsoleReader;
import diary.app.in.Reader;

public class InOutFactory {
    private final Reader reader;

    public InOutFactory() {
        this.reader = new ConsoleReader();
    }

    public Reader getReader() {
        return reader;
    }
}

