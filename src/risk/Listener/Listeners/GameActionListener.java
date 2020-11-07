package risk.Listener.Listeners;

import risk.Listener.Events.OneCountryEvent;
import risk.Listener.Events.TwoCountryEvent;
import risk.Model.GameModel;

public interface GameActionListener {
    void updateMap(GameModel gameModel);

    //map updates
    public void onPlaceTroops(OneCountryEvent oce);

    public void onAttack(TwoCountryEvent tce);

    public void onTroopMovement(TwoCountryEvent tce);
}
