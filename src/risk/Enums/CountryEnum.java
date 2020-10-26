package risk.Enums;

import risk.Country;

import java.util.ArrayList;

public enum CountryEnum {
    /*DECLARATION*/
    //NORTH AMERICAN
    ALASKA, ALBERTA, CENTRAL_AMERICA, EASTERN_UNITED_STATES, GREENLAND, NORTHWEST_TERRITORY, ONTARIO, QUEBEC, WESTERN_UNITED_STATES,

    //SOUTH AMERICA
    ARGENTINA, BRAZIL, PERU, VENEZUELA,

    //EUROPE
    GREAT_BRITAIN, ICELAND, NORTHERN_EUROPE, SCANDINAVIA, SOUTHERN_EUROPE, UKRAINE, WESTERN_EUROPE,

    //AFRICA
    CONGO, EAST_AFRICA, EGYPT, MADAGASCAR, NORTH_AFRICA, SOUTH_AFRICA,

    //ASIA
    AFGHANISTAN, CHINA, INDIA, IRKUTSK, JAPAN, KAMCHATKA, MIDDLE_EAST, MONGOLIA, SIAM, SIBERIA, URAL, YAKUTSK,

    //AUSTRALIA
    EASTERN_AUSTRALIA, INDONESIA, NEW_GUINEA, WESTERN_AUSTRALIA;

    /*INITIALIZATION*/
    static{
        //NORTH AMERICA
        ALASKA.setNeighbours(NORTHWEST_TERRITORY, ONTARIO, WESTERN_UNITED_STATES);
        ALBERTA.setNeighbours(NORTHWEST_TERRITORY, ONTARIO, WESTERN_UNITED_STATES);
        CENTRAL_AMERICA.setNeighbours(WESTERN_UNITED_STATES, EASTERN_UNITED_STATES);
        EASTERN_UNITED_STATES.setNeighbours(WESTERN_UNITED_STATES, ONTARIO, QUEBEC, CENTRAL_AMERICA);
        GREENLAND.setNeighbours(NORTHWEST_TERRITORY, ONTARIO, WESTERN_UNITED_STATES);
        NORTHWEST_TERRITORY.setNeighbours(ALASKA, ALBERTA, ONTARIO, GREENLAND);
        ONTARIO.setNeighbours(NORTHWEST_TERRITORY, ALBERTA, WESTERN_UNITED_STATES, EASTERN_UNITED_STATES, QUEBEC, GREENLAND);
        QUEBEC.setNeighbours(ONTARIO, EASTERN_UNITED_STATES, GREENLAND);
        WESTERN_UNITED_STATES.setNeighbours(ALBERTA, ONTARIO, EASTERN_UNITED_STATES);

        //SOUTH AMERICA
        ARGENTINA.setNeighbours(PERU, BRAZIL);
        BRAZIL.setNeighbours(ARGENTINA, PERU, VENEZUELA, NORTH_AFRICA);
        PERU.setNeighbours(ARGENTINA, BRAZIL, VENEZUELA);
        VENEZUELA.setNeighbours(PERU, BRAZIL, CENTRAL_AMERICA);

        //EUROPE
        GREAT_BRITAIN.setNeighbours(ICELAND, SCANDINAVIA, NORTHERN_EUROPE, WESTERN_EUROPE);
        ICELAND.setNeighbours(GREENLAND, GREAT_BRITAIN, SCANDINAVIA);
        NORTHERN_EUROPE.setNeighbours(GREAT_BRITAIN, SCANDINAVIA, UKRAINE, SOUTHERN_EUROPE, WESTERN_EUROPE);
        SCANDINAVIA.setNeighbours(ICELAND, GREAT_BRITAIN, NORTHERN_EUROPE, UKRAINE);
        SOUTHERN_EUROPE.setNeighbours(NORTHERN_EUROPE, UKRAINE, MIDDLE_EAST, EGYPT, NORTH_AFRICA, WESTERN_EUROPE);
        UKRAINE.setNeighbours(SCANDINAVIA, URAL, AFGHANISTAN, MIDDLE_EAST, SOUTHERN_EUROPE, NORTHERN_EUROPE);
        WESTERN_EUROPE.setNeighbours(GREAT_BRITAIN, NORTHERN_EUROPE, SOUTHERN_EUROPE, NORTH_AFRICA);

        //AFRICA
        CONGO.setNeighbours(SOUTH_AFRICA, EAST_AFRICA, NORTH_AFRICA);
        EAST_AFRICA.setNeighbours(CONGO, NORTH_AFRICA, EGYPT, MIDDLE_EAST, MADAGASCAR, SOUTH_AFRICA);
        EGYPT.setNeighbours(NORTH_AFRICA, SOUTHERN_EUROPE, MIDDLE_EAST, EAST_AFRICA);
        MADAGASCAR.setNeighbours(SOUTH_AFRICA, EAST_AFRICA);
        NORTH_AFRICA.setNeighbours(BRAZIL, WESTERN_EUROPE, SOUTHERN_EUROPE, EGYPT, EAST_AFRICA, CONGO);
        SOUTH_AFRICA.setNeighbours(MADAGASCAR, CONGO, EAST_AFRICA);

        //ASIA
        AFGHANISTAN.setNeighbours(MIDDLE_EAST, UKRAINE, URAL, CHINA, INDIA);
        CHINA.setNeighbours(AFGHANISTAN, URAL, SIBERIA, MONGOLIA, SIAM, INDIA);
        INDIA.setNeighbours(MIDDLE_EAST, AFGHANISTAN, CHINA, SIAM);
        IRKUTSK.setNeighbours(SIBERIA, YAKUTSK, KAMCHATKA, MONGOLIA);
        JAPAN.setNeighbours(KAMCHATKA, MONGOLIA);
        KAMCHATKA.setNeighbours(ALASKA, JAPAN, IRKUTSK, YAKUTSK);
        MIDDLE_EAST.setNeighbours(EAST_AFRICA, EGYPT, NORTHERN_EUROPE, UKRAINE, AFGHANISTAN, INDIA);
        MONGOLIA.setNeighbours(SIBERIA, IRKUTSK, KAMCHATKA, JAPAN, CHINA);
        SIAM.setNeighbours(INDIA, CHINA, INDONESIA);
        SIBERIA.setNeighbours(URAL, YAKUTSK, IRKUTSK, MONGOLIA, CHINA);
        URAL.setNeighbours(UKRAINE, SIBERIA, CHINA, AFGHANISTAN);
        YAKUTSK.setNeighbours(SIBERIA, IRKUTSK, KAMCHATKA);

        //AUSTRALIA
        EASTERN_AUSTRALIA.setNeighbours(WESTERN_AUSTRALIA, NEW_GUINEA);
        INDONESIA.setNeighbours(SIAM, NEW_GUINEA, WESTERN_AUSTRALIA);
        NEW_GUINEA.setNeighbours(INDONESIA, WESTERN_AUSTRALIA, EASTERN_AUSTRALIA);
        WESTERN_AUSTRALIA.setNeighbours(EASTERN_AUSTRALIA, NEW_GUINEA, INDONESIA);
    }

    private ArrayList<CountryEnum> neighbours = new ArrayList<>();
    public Country country;

    private ContinentEnum continentEnum; //THIS IS SET IN CONTINENT ENUM, yes now its coupled but do i care (other option is to pass continent to each enum of country when initializing)

    public void setContinentEnum(ContinentEnum continentEnum){
        this.continentEnum = continentEnum;
    }

    public ContinentEnum getContinentEnum(){
        return continentEnum;
    }

    public void setNeighbours(CountryEnum... countries){
        for (CountryEnum country : countries)
            neighbours.add(country);
    }

    public ArrayList<CountryEnum> getNeighbours(){
        return neighbours;
    }

    public static CountryEnum getEnumFromString(String name) {
        name = name.toUpperCase();

        for (CountryEnum c : CountryEnum.values()) {
            if (c.name().equals(name)) {
                return c;
            }
        }

        return null;
    }
}
