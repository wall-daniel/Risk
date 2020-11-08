package risk.View.Views;

import risk.Listener.Events.ContinentEvent;
import risk.Listener.Events.OneCountryEvent;

public interface GameModelListener {
    public void onNewCountry(OneCountryEvent oce);

    public void onNewContinent(ContinentEvent cce);

    //public void onNewPlayer(CreatePlayerEvent pce);
}
