package risk.Listener;

import risk.Model.Country;

import java.util.EventObject;

public class TwoCountryEvent extends OneCountryEvent {
    private Country country2;

    public TwoCountryEvent(Object source, Country country1, Country country2){
        super(source, country1);
        this.country2 = country2;
    }

    public Country getSecondCountry(){
        return country2;
    }


}
