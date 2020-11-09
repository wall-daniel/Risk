package risk.View.Views;

import risk.Listener.Events.ContinentEvent;
import risk.Listener.Events.CountryEvent;

public interface GameModelListener {
    public void onNewCountry(CountryEvent oce);

    public void onNewContinent(ContinentEvent cce);

    //public void onNewPlayer(CreatePlayerEvent pce);
}
