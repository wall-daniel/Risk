package risk.Controller;

import risk.Command;
import risk.CommandWord;
import risk.Model.Continents;
import risk.Model.Countries;
import risk.Model.GameBoard;
import risk.Parser;
import risk.Players.Player;

import java.util.*;

public class Controller {

    private final ArrayList<Player> players;

    private GameBoard gameBoard;

    private final Parser parser;

    private int currentPlayerPosition = 0;

    private Random rand;


    public Controller() {
        parser = new Parser();
        rand = new Random(System.currentTimeMillis());

        gameBoard = new GameBoard(rand);

        int numPlayers = parser.getInt("How many players (2-6)?: ", 2, 6);
        players = new ArrayList<>(numPlayers);

        // Create the players
        for (int i = 0; i < numPlayers; i++){
            players.add(new Player("Player" + i));
        }

        gameBoard.setupMap(players);


    }


    /**
     * Returns a string that shows who owns which country
     * @return country ownership string
     */
    public String mapString() {
        StringBuilder sb = new StringBuilder();

        for (Player p : players) {
            sb.append(p.getName()).append("'s countries:\n").append(p.getCountriesAsStringWithArmies()).append('\n');
        }

        return sb.toString();
    }


    /**
     * just requests commands from user and then executes them in processCommand
     */
    public void playGame(){
        Player currentPlayer = players.get(currentPlayerPosition);
        startOfTurn(currentPlayer);

        while (true) {
            Command command = parser.getCommand();
            boolean finishedTurn = processCommand(command);
            if (finishedTurn) {
                //finishedTurn is only true if player has called PASS
                currentPlayer = getNextPlayer();
                startOfTurn(currentPlayer);
            }
        }
    }

    /**
     * Gets next player, checks if player has lost.
     *
     * @return next player
     */
    private Player getNextPlayer() {
        currentPlayerPosition = (currentPlayerPosition + 1) % players.size();
        Player nextPlayer = players.get(currentPlayerPosition);

        // Check if player has lost, if they have then skip to next player.
        if (nextPlayer.hasLost()) {
            return getNextPlayer();
        } else {
            return nextPlayer;
        }
    }

    /**
     * Process things the user need to do at the start of the turn
     */
    private void startOfTurn(Player player) {
        System.out.println("Start of " + player.getName() + "'s turn...");

        // Add armies
        int numArmies = player.newArmiesOnTurn();

        System.out.println("The countries you own are: " + player.getCountriesAsString());
        System.out.println("You have " + numArmies + " to place.");
        while (numArmies > 0) {
            String countryName = "";
            while (countryName.equals("") || !Countries.countryExists(countryName)){
                countryName = parser.getInput("What country do you want to place your army on (list)?: ").toUpperCase();
            }

            int n = parser.getInt("How many armies do you want to place here (0-" + numArmies + "): ", 0, numArmies);
            if (n >= 0 && n <= numArmies && player.addArmies(countryName, n)) {
                numArmies -= n;
            } else {
                System.out.println("That wasn't a valid number, try again.");
            }
        }

        // Print out what they own
        System.out.println(player.getCountriesAsStringWithArmies());
    }

    /**
     * processes the commands.
     * @param command to be executed
     * @return true if player has passed, false otherwise
     */
    public boolean processCommand(Command command){
        CommandWord commandWord = command.getCommandWord();
        boolean finishedTurn = false;
        switch(commandWord){
            case HELP:
                printHelp();
                break;
            case ATTACK:
                attack(command);
                break;
//            case MOVE:        Not needed for this milestone.
//                moveTroops(command);
//                break;
//            case END_MOVE:
//                endMoveTroops(command);
//                break;
            case PASS:
                finishedTurn = pass(command);
                break;
            case INFO:
                printMapInfo();
                break;
            case UNKNOWN:
            default:
                System.out.println("I don't know what you mean...");
                break;
        }
        return finishedTurn;
    }


    /**
     * Prints all the commands a user can type
     */
    public void printHelp(){
        System.out.println("\nThe commands you can enter are:\n" + Arrays.toString(CommandWord.values()));
    }

    /**
     * Prints the current state of the map.
     */
    public void printMapInfo(){
        StringBuilder sb = new StringBuilder();

        // Print the info by continent to get a better strategic view.
        for (String continentName : Continents.getContinentNames()) {
            sb.append(continentName).append(":\n");

            // Use the helper method of continents
            Continents.getContinent(continentName).printContinentHelper(sb);

            sb.append("\n");
        }

        System.out.println(sb.toString());
    }

    /**
     * Uses info from command to determine which territory is attacking which territory with how many dice.
     * info needed : territory attack, territory defending, die count (1 - 3)
     * restrictions : (for example (territory 1 has to be adjacent to territory 2))
     * after execution : (if reserve troops are 0, )
     */
    public void attack(Command command){
        AttackController attackController = new AttackController(players.get(currentPlayerPosition), parser, rand);
        // This method does all the attack and returns true if the defender loses game.
        boolean result = attackController.startAttackSequence();

        // Check if the defender has lost.
        if (result) {
            // Check if the attacker is only one left
            int playersLeft = 0;
            for (Player ignored : players) {
                playersLeft++;
            }

            if (playersLeft <= 1) {
                gameOver();
            }
        }
    }

//    /**
//     * Uses info from command to determine which territory is moving troops to which territory
//     * info needed : territory 1 -> territory 2, troop count
//     * restrictions : (for example ( territory 1 has to be connected to territory 2))
//     */
//    Not needed for this milestone, to be added by next one.
//    public void moveTroops(Command command){
//
//    }

//    /**
//     * ends the moving phase
//     */
//    public void endMoveTroops(Command command){
//        gameStatus = GameStatusEnum.PLACING;
//    }

    /**
     * "Pass" was entered. Check the rest of the command to see
     * whether we really want to pass turn.
     * @return true, if this command passes the turn, false otherwise.
     */
    private boolean pass(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Pass what?");
            return false;
        } else {
            return true;  // signal that we want to pass turn
        }
    }

    private void gameOver() {
        System.out.println("The game is over, " + players.get(currentPlayerPosition).getName() + " won. Congrats!");
        System.exit(0);
    }


    public static void main(String[] args) {
        Controller riskController = new Controller();

        System.out.println("Countries: " + Countries.getCountryNames() + '\n');

        System.out.println(riskController.mapString());
        riskController.playGame();
    }
}
