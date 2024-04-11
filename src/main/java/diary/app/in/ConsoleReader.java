package diary.app.in;

import java.util.Scanner;

public class ConsoleReader {
    public static String read() {
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }
}
