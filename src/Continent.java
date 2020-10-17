import java.util.ArrayList;
import java.util.List;

public class Continent {

    private String name;
    private List<Country> countries;

    public Continent(String name) {
        this.name = name;
        this.countries = new ArrayList<>();
    }
}
