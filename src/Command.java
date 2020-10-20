import java.util.ArrayList;

public class Command {

    private ArrayList<CommandWord> words;

    public Command(ArrayList<CommandWord> words){
        this.words = words;
    }

    /**
     * Returns the first word of the command.
     *
     * @return the first word of the command
     */
    public CommandWord getCommandWord() {
        return words.get(0);
    }

    /**
     * Returns whether or not the command has more than one word.
     *
     * @return true if the command has multiple words, false otherwise
     */
    public boolean hasSecondWord() {
        return !words.get(1).equals(null);
    }



}
