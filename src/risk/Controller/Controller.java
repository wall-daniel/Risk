package risk.Controller;

import risk.Enums.GameStatusEnum;
import risk.Enums.NumPlayers;
import risk.Model.GameModel;
import risk.Model.Player;

import java.util.*;

public class Controller {

    private static int currentPlayerNum = 0;

    private Random rand;

    private static GameStatusEnum gameStatus;

    //creates GUI
    public Controller() {


    }

    public static GameStatusEnum getGameStatus(){
        return gameStatus;
    }

    public static int getCurrentPlayerNum(){
        return currentPlayerNum;
    }

    public static Player getCurrentPlayer(){
        return GameModel.getPlayers().get(currentPlayerNum);
    }



    public void setupGame(int numPlayers){
        rand = new Random(System.currentTimeMillis());
        gameStatus = GameStatusEnum.TROOP_PLACEMENT_PHASE;

        //int numPlayers = parser.getInt("How many GameModel.getPlayers() (2-6)?: ", 2, 6);

        // Create the GameModel.getPlayers()
        for (int i = 0; i < numPlayers; i++){
            GameModel.getPlayers().add(new Player("Player" + i));
        }

        autoSetupMap(NumPlayers.SIX_PLAYERS);
        currentPlayerNum = 0;
    }




    public void autoSetupMap(NumPlayers numPlayers){
        int initArmies = numPlayers.getInitialArmySize();

        //Random allocation of countries to each player
        ArrayList<String> tempCountryNames = new ArrayList<String>(GameModel.getCountryNames());
        while (!tempCountryNames.isEmpty()){
            String countryName = tempCountryNames.remove(rand.nextInt(tempCountryNames.size()));
            GameModel.getCountry(countryName).setPlayer(currentPlayerNum);

            Controller.currentPlayerNum = (Controller.currentPlayerNum + 1) % GameModel.getPlayers().size();
        }

        // Choose how many armies are on each country.
        // Does this by randomly choosing a country and assigning 1
        // more army, until the player has no more armies left.
        for (Player player : GameModel.getPlayers()) {
            ArrayList<String> countriesOwned = player.getCountries();
            int currentArmies = initArmies - countriesOwned.size();

            while (currentArmies > 0) {
                GameModel.getCountry(countriesOwned.get(rand.nextInt(countriesOwned.size()))).addArmies(1);
                currentArmies -= 1;
            }
        }
    }



    /**
     * Gets next player, checks if player has lost.
     *
     * @return next player
     */
    private Player getNextPlayer() {
        currentPlayerNum = (currentPlayerNum + 1) % GameModel.getPlayers().size();
        Player nextPlayer = GameModel.getPlayers().get(currentPlayerNum);

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
            while (countryName.equals("") || !GameModel.countryExists(countryName)){
                countryName = "mhm"; //TODO
                      //  parser.getInput("What country do you want to place your army on (list)?: ").toUpperCase();
            }

            int n = 3; //TODO
            //parser.getInt("How many armies do you want to place here (0-" + numArmies + "): ", 0, numArmies);
            if (n >= 0 && n <= numArmies && player.addArmies(countryName, n)) {
                numArmies -= n;
            } else {
                System.out.println("That wasn't a valid number, try again.");
            }
        }

        // Print out what they own
        System.out.println(player.getCountriesAsStringWithArmies());
    }

    public static void placeTroops(){


    }



    /**
     * Uses info from command to determine which territory is attacking which territory with how many dice.
     * info needed : territory attack, territory defending, die count (1 - 3)
     * restrictions : (for example (territory 1 has to be adjacent to territory 2))
     * after execution : (if reserve troops are 0, )
     */
    public void attack(){
        AttackController attackController = new AttackController(currentPlayerNum, rand);
        // This method does all the attack and returns true if the defender loses game.
        boolean result = attackController.startAttackSequence();

        // Check if the defender has lost.
        if (result) {
            // Check if the attacker is only one left
            int playersLeft = 0;
            for (Player ignored : GameModel.getPlayers()) {
                playersLeft++;
            }

            if (playersLeft <= 1) {
                gameOver();
            }
        }
    }

    public void endAttack(){


    }
    /**
        * Uses info from command to determine which territory is moving troops to which territory
     * info needed : territory 1 -> territory 2, troop count
    * restrictions : (for example ( territory 1 has to be connected to territory 2))
     */
    public void moveTroops(){


    }

    public void endTurn(){


    }



    private void gameOver() {
        System.out.println("The game is over, " + GameModel.getPlayers().get(currentPlayerNum).getName() + " won. Congrats!");
        System.exit(0);
    }

    public static void main(String[] args) {
        Controller riskController = new Controller();
    }
}
