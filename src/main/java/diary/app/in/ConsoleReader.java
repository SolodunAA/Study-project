package diary.app.in;

import java.util.Scanner;

public class ConsoleReader {
    public String read() {
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }
}
