package risk.Model;

import risk.Players.Player;

import java.util.*;

public class GameBoard {


    private Random rand;

    public GameBoard(Random rand){
        this.rand = rand;
    }


    public void setupMap(ArrayList<Player> players) {
        int initArmies = getInitialArmies(players.size());
        int currentPlayerPosition = 0;

        //Random allocation of countries to each player
        ArrayList<String> tempCountryNames = new ArrayList<String>(Countries.getCountryNames());
        while (!tempCountryNames.isEmpty()){
            String countryName = tempCountryNames.remove(rand.nextInt(tempCountryNames.size()));
            Countries.getCountry(countryName).setPlayer(players.get(currentPlayerPosition));

            currentPlayerPosition = (currentPlayerPosition + 1) % players.size();
        }

        // Choose how many armies are on each country.
        // Does this by randomly choosing a country and assigning 1
        // more army, until the player has no more armies left.
        for (Player player : players) {
            ArrayList<String> countriesOwned = player.getCountries();
            int currentArmies = initArmies - countriesOwned.size();

            while (currentArmies > 0) {
                Countries.getCountry(countriesOwned.get(rand.nextInt(countriesOwned.size()))).addArmies(1);
                currentArmies -= 1;
            }
        }
    }

    public static int getInitialArmies(int players) {
        switch (players) {
            case 2:
                return 50;
            case 3:
                return 35;
            case 4:
                return 30;
            case 5:
                return 25;
            default:    // 6 players
                return 20;
        }
    }

}
