package risk.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Not useful right now, but later for bonuses and gui will be.
public class Continent {

    private String name;
    private int bonus = 0;
    private Set<Country> countries;

    public Continent(String name, int bonus) {
        this.name = name;
        this.bonus = bonus;

        this.countries = new HashSet<>();
    }

    public Continent(JsonObject json) {
        // Get name
        this.name = json.get("name").getAsString();
        this.bonus = json.get("bonus").getAsInt();

        // Get countries
        this.countries = new HashSet<>();
        JsonArray countryArr = json.get("countries").getAsJsonArray();
        countryArr.forEach(country -> {
            Country c = new Country(country.getAsJsonObject());
            c.setContinent(this);
            countries.add(c);
        });
    }

    public String getName() {
        return this.name;
    }

    public Set<Country> getCountries() {
        return this.countries;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        // Set continent values
        json.addProperty("name", name);
        json.addProperty("bonus", bonus);

        // Add the countries
        JsonArray countryArr = new JsonArray();
        countries.forEach(it -> countryArr.add(it.toJson()));
        json.add("countries", countryArr);

        return json;
    }

    @Override
    public String toString() {
        return name;
    }

    public void addCountry(Country country) {
        countries.add(country);
    }

    public void removeCountry(Country country) {
        countries.remove(country);
    }
}
