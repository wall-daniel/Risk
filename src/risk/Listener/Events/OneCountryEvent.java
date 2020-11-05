package risk.Listener.Events;

import risk.Model.Country;

import java.awt.*;
import java.util.EventObject;

public class OneCountryEvent extends EventObject {
    private Country country;

    public OneCountryEvent(Object source, Country country){
        super(source);
        this.country = country;
    }
    public Country getFirstCountry(){
        return country;
    }

}
