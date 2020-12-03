package risk.Listener.Events;

import risk.Model.Country;

import java.awt.*;
import java.util.EventObject;

public class CountryEvent extends EventObject {
    private final Country country;
    private final String initialCountryName;

    public CountryEvent(Object source, Country country){
        super(source);
        this.country = country;
        initialCountryName = "";
    }

    public CountryEvent(Object source, Country country, String initialCountryName){
        super(source);
        this.country = country;
        this.initialCountryName = initialCountryName;
    }


    public Country getCountry(){
        return country;
    }

    public String getInitialCountryName(){
        return initialCountryName;
    }

}
