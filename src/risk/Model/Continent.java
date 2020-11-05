package risk.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

// Not useful right now, but later for bonuses and gui will be.
public class Continent {

    private String name;
    private int bonus = 0;
    private List<Country> countries;

    public Continent(String name, int bonus) {
        this.name = name;
        this.bonus = bonus;
    }
    public Continent(JsonObject json) {
        // Get name
        this.name = json.get("name").getAsString();

        // Get countries
        this.countries = new ArrayList<>();
        JsonArray countryArr = json.get("countries").getAsJsonArray();
        countryArr.forEach(country -> {
            Country c = new Country(country.getAsJsonObject());
            c.setContinent(this);
        });
    }

    public String getName() {
        return this.name;
    }

    public List<Country> getCountries() {
        return this.countries;
    }
}
