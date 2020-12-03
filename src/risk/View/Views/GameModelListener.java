package risk.View.Views;

import risk.Listener.Events.ContinentEvent;
import risk.Listener.Events.CountryEvent;
import risk.Model.Country;

public interface GameModelListener {
    void onNewCountry(CountryEvent oce);

    void onEditCountry(CountryEvent oce);

    void onDeleteCountry(Country oce);

    //public void onNewPlayer(CreatePlayerEvent pce);
}
