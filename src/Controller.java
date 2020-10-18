import Enums.ContinentEnum;
import Enums.CountryEnum;

import java.util.*;

public class Controller {

    private Map<ContinentEnum, Continent> continents;
    private Map<CountryEnum, Country> countries;
    private List<Player> players;

    public Controller(int numPlayers) {
        players = new ArrayList<>(numPlayers);
        countries = new HashMap<>(41);
        continents = new HashMap<>(6);

        for (int i = 0; i < numPlayers; i++)
            players.add(new Player("Player" + i));
        for (CountryEnum countryEnum : CountryEnum.values())
            countries.put(countryEnum, new Country(countryEnum));
        for (ContinentEnum continentEnum : ContinentEnum.values())
            continents.put(continentEnum, new Continent(continentEnum));
    }

    public String getMapValues() {
        StringBuilder sb = new StringBuilder();

        for (Player p : players) {
            sb.append(p.getName()).append(" controls:\n");

            for (CountryEnum c : p.getCountries()) {
                Country country = countries.get(c);
                sb.append(c).append(": ").append(country.getArmies()).append('\n');
            }

            sb.append('\n');
        }

        return sb.toString();
    }

    public void setupMap() {
        int currentPlayer = 0;
        int initArmies = Player.getInitialArmies(players.size());

        //Random allocation of countries to each player
        ArrayList<CountryEnum> tempCountryEnums = new ArrayList<CountryEnum>(countries.keySet());
        Random rand = new Random(System.currentTimeMillis());
        while (!tempCountryEnums.isEmpty()){
            CountryEnum countryEnum = tempCountryEnums.remove(rand.nextInt(tempCountryEnums.size()));
            countries.get(countryEnum).setPlayer(players.get(currentPlayer));
            currentPlayer = (currentPlayer + 1) % players.size();
        }

        // Choose how many armies are on each country.
        // Does this by randomly choosing a country and assigning 1
        // more army, until the player has no more armies left.
        for (Player player : players) {
            List<CountryEnum> countriesOwned = player.getCountries();
            int currentArmies = initArmies - countriesOwned.size();

            while (currentArmies > 0) {
                this.countries.get(countriesOwned.get(rand.nextInt(countriesOwned.size()))).addArmies(1);
                currentArmies -= 1;
            }
        }
    }

    public CountryEnum[] getCountryNames() {
        return CountryEnum.values();
    }

    public static int getInt(Scanner input, String message) {
        while (true) {
            try {
                System.out.print(message);

                return input.nextInt();
            } catch (Exception e) {
                System.out.println("That was not a number, now try again.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int numPlayers = getInt(input, "Welcome to Risk, how many people are playing?: ");
        Controller riskController = new Controller(numPlayers);

        System.out.println(  CountryEnum.ALBERTA.getContinentEnum());

        System.out.println("Countries: " + Arrays.toString(riskController.getCountryNames()) + '\n');
        riskController.setupMap();
        System.out.println(riskController.getMapValues());
    }
}
