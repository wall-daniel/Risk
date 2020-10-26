package risk;

import java.util.ArrayList;

public class Command {

    private ArrayList<CommandWord> words;

    public Command(ArrayList<CommandWord> words){
        this.words = words;
    }

    /**
     * Returns the main command word entered.
     *
     * @return the command word.
     */
    public CommandWord getCommandWord() {
        return words.get(0);
    }

    /**
     * Returns true if there are more than one command word.
     */
    public boolean hasSecondWord() {
        return words.size() > 1;
    }
}
