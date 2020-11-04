package risk.Listener;

import risk.Model.Country;
import risk.Players.Player;

public interface GameModelListener {
    //when a new country is created update the mapeditor
    public void onNewCountry(OneCountryEvent oce);

    //map updates
    public void onPlaceTroops(OneCountryEvent oce);

    public void onAttack(TwoCountryEvent tce);

    public void onTroopMovement(TwoCountryEvent tce);

}
