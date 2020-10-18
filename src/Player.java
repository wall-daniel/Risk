import Enums.CountryEnum;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private List<CountryEnum> countriesOwned;

    public Player(String name) {
        this.name = name;
        countriesOwned = new ArrayList<>();
    }

    public String getName() { return name; }
    public List<CountryEnum> getCountries() { return countriesOwned; }

    public void removeCountry(CountryEnum c) {
        countriesOwned.remove(c);
    }
    public void addCountry(CountryEnum c) {
        countriesOwned.add(c);
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
