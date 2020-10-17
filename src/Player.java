import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {

    private String name;
    private List<Country> countries;

    public Player(String name) {
        this.name = name;
        countries = new ArrayList<>();
    }

    public String getName() { return name; }
    public List<Country> getCountries() { return countries; }

    public void removeCountry(Country c) {
        countries.remove(c);
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
