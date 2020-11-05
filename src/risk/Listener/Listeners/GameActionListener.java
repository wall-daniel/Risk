package risk.Listener.Listeners;

import risk.Listener.Events.OneCountryEvent;
import risk.Listener.Events.TwoCountryEvent;

public interface GameActionListener {
    //map updates
    public void onPlaceTroops(OneCountryEvent oce);

    public void onAttack(TwoCountryEvent tce);

    public void onTroopMovement(TwoCountryEvent tce);
}
