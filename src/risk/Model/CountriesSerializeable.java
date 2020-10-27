package risk.Model;

import java.io.Serializable;
import java.util.HashMap;

public class CountriesSerializeable implements Serializable {
    private HashMap<String, Country> countries;

    public CountriesSerializeable(HashMap<String, Country> countries) {
        this.countries = countries;
    }
}
