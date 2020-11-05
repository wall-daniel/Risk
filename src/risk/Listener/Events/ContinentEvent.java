package risk.Listener.Events;

import risk.Controller.Controller;
import risk.Model.Continent;
import risk.Model.Country;

import java.util.EventObject;

public class ContinentEvent extends EventObject {
    private Continent continent;

    public ContinentEvent(Object source, Continent continent){
        super(source);
        this.continent = continent;
    }
    public Continent getContinent(){
        return continent;
    }


}
