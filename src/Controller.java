import java.io.*;
import java.util.*;

public class Controller {

    private Map<String, Country> countries;
    private List<Continent> continents;
    private List<Player> players;

    public Controller(int numPlayers) {
        players = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("Player" + i));
        }

        countries = new HashMap<>(41);
        continents = new ArrayList<>(6);
    }

    public void loadCountries(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            String line;
            Continent continent = null;
            while ((line = br.readLine()) != null) {
                String[] strArr = line.split(",");

                if (strArr.length == 1) {
                    continent = new Continent(strArr[0]);
                    continents.add(continent);
                } else {
                    Country country = new Country(strArr[0], continent);

                    for (int i = 1; i < strArr.length; i++) {
                        country.addNeighbour(strArr[i]);
                    }

                    countries.put(strArr[0], country);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMapValues() {
        StringBuilder sb = new StringBuilder();

        for (Player p : players) {
            sb.append(p.getName()).append(" controls:\n");

            for (Country c : p.getCountries()) {
                sb.append(c.getName()).append(": ").append(c.getArmies()).append('\n');
            }

            sb.append('\n');
        }

        return sb.toString();
    }

    public void setupMap() {
        int currentPlayer = 0;
        int initArmies = Player.getInitialArmies(players.size());

        // Choose countries that a player has
        for (Country country : countries.values()) {
            country.setPlayer(players.get(currentPlayer));

            currentPlayer = (currentPlayer + 1) % players.size();
        }

        // Choose how many armies are on each country.
        // Does this by randomly choosing a country and assigning 1
        // more army, until the player has no more armies left.
        Random rand = new Random(System.currentTimeMillis());
        for (Player player : players) {
            List<Country> countries = player.getCountries();
            int currentArmies = initArmies - countries.size();

            while (currentArmies > 0) {
                countries.get(rand.nextInt(countries.size())).addArmies(1);

                currentArmies -= 1;
            }
        }
    }

    public Set<String> getCountryNames() {
        return countries.keySet();
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
        riskController.loadCountries("countries.csv");

        System.out.println("Countries: " + Arrays.toString(riskController.getCountryNames().toArray()) + '\n');
        riskController.setupMap();
        System.out.println(riskController.getMapValues());
    }
}
