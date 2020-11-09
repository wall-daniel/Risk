package risk.Listener.Events;

import risk.Model.Continent;

import java.util.EventObject;

public class ContinentEvent extends EventObject {
    private final Continent continent;

    public ContinentEvent(Object source, Continent continent) {
        super(source);
        this.continent = continent;
    }

    public Continent getContinent() {
        return continent;
    }

}
