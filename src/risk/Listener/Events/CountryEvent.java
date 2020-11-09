package risk.Listener.Events;

import risk.Model.Country;

import java.awt.*;
import java.util.EventObject;

public class CountryEvent extends EventObject {
    private final Country country;

    public CountryEvent(Object source, Country country){
        super(source);
        this.country = country;
    }
    public Country getFirstCountry(){
        return country;
    }

}
