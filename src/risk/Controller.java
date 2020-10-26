package risk;

import risk.Enums.*;
import risk.Players.Player;

import java.util.*;

public class Controller {

    private Map<ContinentEnum, Continent> continents;
    private final Map<CountryEnum, Country> countries;
    private final ArrayList<Player> players;

    private final Parser parser;
    private Random rand;

    private GameStatusEnum gameStatus;
    private int currentPlayerPosition = 0;

    public Controller() {
        parser = new Parser();

        int numPlayers = parser.getInt("How many players (2-6)?: ");
        players = new ArrayList<>(numPlayers);
        countries = new HashMap<>(41);
        continents = new HashMap<>(6);

        gameStatus = GameStatusEnum.PLACING;

        // Create the players
        for (int i = 0; i < numPlayers; i++){
            players.add(new Player("Player" + i));
        }

        // Create the map
        for (CountryEnum countryEnum : CountryEnum.values())
            countries.put(countryEnum, new Country(countryEnum));
        for (ContinentEnum continentEnum : ContinentEnum.values())
            continents.put(continentEnum, new Continent(continentEnum));
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

        while (!isGameOver()){
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
            String country = parser.getInput("What country do you want to place your army on (list)?: ").toUpperCase();

            // List what they currently have
            if (country.toLowerCase().equals("list")) {
                System.out.println(player.getCountriesAsStringWithArmies());
                continue;
            }

            CountryEnum countryEnum = CountryEnum.getEnumFromString(country);
            if (countryEnum != null) {
                int n = parser.getInt("How many armies do you want to place here (0-" + numArmies + "): ");
                if (n >= 0 && n <= numArmies && player.addArmies(countryEnum, n)) {
                    numArmies -= n;
                } else {
                    System.out.println("That wasn't a valid number, try again.");
                }
            } else {
                System.out.println("That's not a country??");
            }
        }

        // Print out what they own
        System.out.println(player.getCountriesAsStringWithArmies());
    }

    /**
     * @return true if game is over, false otherwise
     */
    public boolean isGameOver(){
        return gameStatus == GameStatusEnum.GAME_OVER;
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
        for (ContinentEnum continent : ContinentEnum.values()) {
            sb.append(continent.name()).append(":\n");

            // Use the helper method of continents
            continent.printContinentHelper(sb);

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
        boolean result = attackController.startAttackSequence();

        // Check if the attacker won
        if (result) {
            // Check if the attacker is only one left
            int playersLeft = 0;
            for (Player player : players) {
                playersLeft++;
            }

            if (playersLeft <= 1) {
                gameStatus = GameStatusEnum.GAME_OVER;
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



    public void setupMap() {
        int initArmies = Player.getInitialArmies(players.size());

        //Random allocation of countries to each player
        ArrayList<CountryEnum> tempCountryEnums = new ArrayList<CountryEnum>(countries.keySet());
        rand = new Random(System.currentTimeMillis());
        while (!tempCountryEnums.isEmpty()){
            CountryEnum countryEnum = tempCountryEnums.remove(rand.nextInt(tempCountryEnums.size()));
            countries.get(countryEnum).setPlayer(players.get(currentPlayerPosition));

            currentPlayerPosition = (currentPlayerPosition + 1) % players.size();
        }

        // Choose how many armies are on each country.
        // Does this by randomly choosing a country and assigning 1
        // more army, until the player has no more armies left.
        for (Player player : players) {
            List<CountryEnum> countriesOwned = player.getCountries();
            int currentArmies = initArmies - countriesOwned.size();

            while (currentArmies > 0) {
                countriesOwned.get(rand.nextInt(countriesOwned.size())).country.addArmies(1);
                currentArmies -= 1;
            }
        }
    }

    private void gameOver() {
        System.out.println("The game is over, " + players.get(currentPlayerPosition).getName() + " won. Congrats!");
        System.exit(0);
    }

    public CountryEnum[] getCountryNames() {
        return CountryEnum.values();
    }

    public static void main(String[] args) {
        Controller riskController = new Controller();

        System.out.println("Countries: " + Arrays.toString(riskController.getCountryNames()) + '\n');
        riskController.setupMap();
        System.out.println(riskController.mapString());
        riskController.playGame();
    }
}
