package risk.Model;

import risk.Players.Player;

import java.util.*;

public class GameBoard {

    public static int NUM_COUNTRIES = 41;
    public static int NUM_CONTINENTS = 6;

    public static int NUM_ARMIES_2_PLAYERS = 50;
    public static int NUM_ARMIES_3_PLAYERS = 35;
    public static int NUM_ARMIES_4_PLAYERS = 30;
    public static int NUM_ARMIES_5_PLAYERS = 25;
    public static int NUM_ARMIES_6_PLAYERS = 20;


   // private Map<ContinentEnum, Continent> continents;
   // private Map<CountryEnum, Country> countries;

    private Random rand;

    public GameBoard(Random rand){
        this.rand = rand;
       // countries = new HashMap<>(NUM_COUNTRIES);
       // continents = new HashMap<>(NUM_CONTINENTS);
        rand = new Random(System.currentTimeMillis());

        // Create the map
        /*
        for (CountryEnum countryEnum : CountryEnum.values())
            countries.put(countryEnum, new Country(countryEnum));
        for (ContinentEnum continentEnum : ContinentEnum.values())
            continents.put(continentEnum, new Continent(continentEnum));
            */
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
                return NUM_ARMIES_2_PLAYERS;
            case 3:
                return NUM_ARMIES_3_PLAYERS;
            case 4:
                return NUM_ARMIES_4_PLAYERS;
            case 5:
                return NUM_ARMIES_5_PLAYERS;
            default:    // 6 players
                return NUM_ARMIES_6_PLAYERS;
        }
    }

}
