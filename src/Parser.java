import java.util.Scanner;

public class Parser {

    Scanner inputScanner;

    public Parser() {
        inputScanner = new Scanner(System.in);
    }

    public Command getCommand() {
        System.out.print("Enter command: ");
        inputScanner.next();
        return new Command();
    }
}
