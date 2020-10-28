package risk.Model;

import java.io.Serializable;
import java.util.HashMap;

public class ContinentsSerializable implements Serializable {
    private HashMap<String, Continent> continents;

    public ContinentsSerializable(HashMap<String, Continent> continents){
        this.continents = continents;
    }

}
