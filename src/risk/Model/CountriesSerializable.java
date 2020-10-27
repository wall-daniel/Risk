package risk.Model;

import java.io.Serializable;
import java.util.HashMap;

public class CountriesSerializable implements Serializable {
    private HashMap<String, Country> countries;

    public CountriesSerializable(HashMap<String, Country> countries) {
        this.countries = countries;
    }
}
