package risk;

import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

    Scanner inputScanner;

    public Parser() {
        inputScanner = new Scanner(System.in);
    }

    public Command getCommand() {
        // Will temporarily hold the entire command
        System.out.print("Enter command: ");
        String input = inputScanner.nextLine();

        // Split the command words from spaces because
        String[] inputWords = input.split(" ");

        ArrayList<CommandWord> commandWords = new ArrayList<>(inputWords.length);

        for (String inputWord : inputWords) {
            commandWords.add(getCommandWordFromString(inputWord));
        }

        return new Command(commandWords);
    }

    /**
     * Loops through the command words and tries to find one equal to the input.
     * If it does not find one it returns unknown.
     *
     * @param input is string of command word entered by user.
     * @return enum command word of entered string, or unknown enum.
     */
    private CommandWord getCommandWordFromString(String input) {
        input = input.toUpperCase();

        for (CommandWord commandWord : CommandWord.values()) {
            if (commandWord.name().equals(input)) {
                return commandWord;
            }
        }

        return CommandWord.UNKNOWN;
    }

    /**
     * @param message displayed to user.
     * @return what user entered.
     */
    public String getInput(String message) {
        System.out.print(message);
        return inputScanner.nextLine();
    }

    /**
     * Returns the next int the user enters.
     * If the user does not enter an int, it loops until they do.
     *
     * @param message for user, will be displayed each loop.
     * @param lowerBound lower bound inclusive.
     * @param upperBound upper bound inclusive.
     * @return int value that user enters.
     */
    public int getInt(String message, int lowerBound, int upperBound) {
        while (true) {
            try {
                System.out.print(message);

                int result = Integer.parseInt(inputScanner.nextLine());
                if (result >= lowerBound && result <= upperBound) {
                    return result;
                }
            } catch (Exception e) {
                System.out.println("That was not a number, now try again.");
            }
        }
    }
}
