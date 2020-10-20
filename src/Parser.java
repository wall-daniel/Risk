import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

    Scanner inputScanner;

    public Parser() {
        inputScanner = new Scanner(System.in);
    }

    public Command getCommand() {

        String input; // Will temporarily hold the entire command

        System.out.print("Enter command: ");
        input = inputScanner.nextLine();

        String[] inputWords = input.split(" ");

        ArrayList<CommandWord> commandWords = new ArrayList<CommandWord>();

        for (int i = 0; i < inputWords.length; i++) {
            commandWords.add(CommandWord.valueOf(inputWords[i]));
        }

        return new Command(commandWords);
    }
}
