package risk.Enums;

import java.util.ArrayList;

import static risk.Enums.CountryEnum.*;

public enum ContinentEnum {
    NORTH_AMERICA(10, ALASKA, ALBERTA, CENTRAL_AMERICA, EASTERN_UNITED_STATES, GREENLAND, NORTHWEST_TERRITORY, ONTARIO, QUEBEC, WESTERN_UNITED_STATES),
    SOUTH_AMERICA(10, ARGENTINA, BRAZIL, PERU, VENEZUELA),
    EUROPE(5, GREAT_BRITAIN, ICELAND, NORTHERN_EUROPE, SCANDINAVIA, SOUTHERN_EUROPE, UKRAINE, WESTERN_EUROPE),
    AFRICA(5, CONGO, EAST_AFRICA, EGYPT, MADAGASCAR, NORTH_AFRICA, SOUTH_AFRICA),
    ASIA(6, AFGHANISTAN, CHINA, INDIA, IRKUTSK, JAPAN, KAMCHATKA, MIDDLE_EAST, MONGOLIA, SIAM, SIBERIA, URAL, YAKUTSK),
    AUSTRALIA(6, EASTERN_AUSTRALIA, INDONESIA, NEW_GUINEA, WESTERN_AUSTRALIA);

    private final int continent_bonus;
    private final ArrayList<CountryEnum> countries = new ArrayList<>();

    ContinentEnum(int continent_bonus, CountryEnum... countries) {
        this.continent_bonus = continent_bonus;
        for (CountryEnum country : countries) {
            this.countries.add(country);
            country.setContinentEnum(this);
        }
    }

    /**
     * Adds all country names, player controlled by, and number of armies for the continent.
     */
    public void printContinentHelper(StringBuilder sb) {
        for (CountryEnum countryEnum : countries) {
            sb.append(countryEnum.name())
                    .append(": ")
                    .append(countryEnum.country.getPlayer().getName())
                    .append(", ")
                    .append(countryEnum.country.getArmies())
                    .append("\n");
        }
    }
}
