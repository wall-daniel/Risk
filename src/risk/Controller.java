package risk;

import risk.Enums.*;
import risk.Players.Player;

import java.util.*;

public class Controller {

    private Map<ContinentEnum, Continent> continents;
    private Map<CountryEnum, Country> countries;
    private Map<PlayerEnum, Player> players;

    private Parser parser;
    private Random rand;

    private GameStatusEnum gameStatus;
    private PlayerEnum currentPlayer;

    public Controller() {
        parser = new Parser();

        int numPlayers = parser.getInt("How many players (2-6)?: ");
        players = new HashMap<>(numPlayers);
        countries = new HashMap<>(41);
        continents = new HashMap<>(6);

        gameStatus = GameStatusEnum.PLACING;
        currentPlayer = PlayerEnum.PLAYER_1;

        int i = 1;
        for (PlayerEnum playerEnum : PlayerEnum.values()){
            if (i > numPlayers) break;
            players.put(playerEnum, new Player("Player" + i));
            i++;
        }
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

        for (Player p : players.values()) {
            sb.append(p.getName()).append("'s countries:\n").append(p.getCountriesAsStringWithArmies()).append('\n');
        }

        return sb.toString();
    }


    /**
     * just requests commands from user and then executes them in processCommand
     */
    public void playGame(){
        startOfTurn(currentPlayer);

        while (!isGameOver()){
            Command command = parser.getCommand();
            boolean finishedTurn = processCommand(command);
            if (finishedTurn) {//finishedTurn is only true if player has called PASS
                currentPlayer = currentPlayer.getNextPlayer(players.size());
                startOfTurn(currentPlayer);
            }
        }
    }

    /**
     * Process things the user need to do at the start of the turn
     */
    private void startOfTurn(PlayerEnum playerEnum) {
        System.out.println("Start of " + currentPlayer.name() + "'s turn...");

        // Add armies
        Player player = players.get(playerEnum);
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
     * true if game is over, false otherwise
     * @return
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
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;
            case HELP:
                printHelp();
                break;
            case PLACE:
                placeTroops(command);
                break;
            case ATTACK:
                attack(command);
                break;
            case END_ATTACK:
                endAttack(command);
                break;
            case MOVE:
                moveTroops(command);
                break;
            case END_MOVE:
                endMoveTroops(command);
                break;
            case PASS:
                finishedTurn = pass(command);
                break;
            case INFO:
                printMapInfo();
                break;
        }
        return finishedTurn;
    }


    /**
     * Prints all the commands a user can type
     */
    public void printHelp(){

    }

    /**
     * Prints the current state of the map.
     */
    public void printMapInfo(){

    }


    /**
     * Uses info from command to determine which territory to place reserve troops on
     * info needed : territory 1, troop count
     * restrictions : (for example (troop count <= reserve troop counts
     * after execution : (if reserve troops are 0, move gamestatus from PLACING to ATTACKING)
     */
    public void placeTroops(Command command){

    }

    /**
     * Uses info from command to determine which territory is attacking which territory with how many dice.
     * info needed : territory attack, territory defending, die count (1 - 3)
     * restrictions : (for example (territory 1 has to be adjacent to territory 2))
     * after execution : (if reserve troops are 0, )
     */
    public void attack(Command command){
        AttackController attackController = new AttackController(players.get(currentPlayer), parser, rand);
        attackController.startAttackSequence();
    }

    /**
     * ends the attacking phase
     */
    public void endAttack(Command command){
        gameStatus = GameStatusEnum.MOVING;
    }

    /**
     * Uses info from command to determine which territory is moving troops to which territory
     * info needed : territory 1 -> territory 2, troop count
     * restrictions : (for example ( territory 1 has to be connected to territory 2))
     */
    public void moveTroops(Command command){


    }

    /**
     * ends the moving phase
     */
    public void endMoveTroops(Command command){
        gameStatus = GameStatusEnum.PLACING;
    }

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
        }
        else {
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
            countries.get(countryEnum).setPlayer(players.get(currentPlayer));
            currentPlayer = currentPlayer.getNextPlayer(players.size());
        }

        // Choose how many armies are on each country.
        // Does this by randomly choosing a country and assigning 1
        // more army, until the player has no more armies left.
        for (Player player : players.values()) {
            List<CountryEnum> countriesOwned = player.getCountries();
            int currentArmies = initArmies - countriesOwned.size();

            while (currentArmies > 0) {
                countriesOwned.get(rand.nextInt(countriesOwned.size())).country.addArmies(1);
                currentArmies -= 1;
            }
        }
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
