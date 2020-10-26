package risk;

import risk.Enums.ContinentEnum;
import risk.Enums.CountryEnum;
import risk.Players.Player;

import java.util.*;

public class GameBoard {
    private Map<ContinentEnum, Continent> continents;
    private Map<CountryEnum, Country> countries;

    private Random rand;

    public GameBoard(Random rand){
        this.rand = rand;
        countries = new HashMap<>(41);
        continents = new HashMap<>(6);
        rand = new Random(System.currentTimeMillis());

        // Create the map
        for (CountryEnum countryEnum : CountryEnum.values())
            countries.put(countryEnum, new Country(countryEnum));
        for (ContinentEnum continentEnum : ContinentEnum.values())
            continents.put(continentEnum, new Continent(continentEnum));
    }


    public void setupMap(ArrayList<Player> players) {
        int initArmies = getInitialArmies(players.size());
        int currentPlayerPosition = 0;

        //Random allocation of countries to each player
        ArrayList<CountryEnum> tempCountryEnums = new ArrayList<CountryEnum>(countries.keySet());
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
