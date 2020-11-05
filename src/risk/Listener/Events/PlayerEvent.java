package risk.Listener.Events;

import risk.Players.Player;

import java.util.EventObject;

public class PlayerEvent extends EventObject {
    private Player player;

    public PlayerEvent(Object source, Player player){
        super(source);
        this.player = player;
    }
    public Player getPlayer(){
        return player;
    }


}
