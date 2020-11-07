package risk.View.Map;

import risk.Controller.Controller;
import risk.Listener.Events.OneCountryEvent;
import risk.Listener.Events.TwoCountryEvent;
import risk.Listener.Listeners.GameActionListener;
import risk.Model.GameModel;
import javax.swing.*;
import java.awt.*;

public class MapInformation extends JPanel implements GameActionListener {

    JLabel currentPlayer;
    JLabel currentPhase;
    JLabel phaseInfo;
    JButton endPhase;

    public MapInformation(Controller controller){
         setUp(controller);
     }
     
    private void setUp(Controller controller) {
         setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
         currentPlayer = new JLabel("CurrentPlayer");
         currentPhase = new JLabel("CurrentPhase");
         phaseInfo = new JLabel("PhaseInfo");
         endPhase = new JButton("End Phase");
         endPhase.addActionListener(controller);

         add(currentPlayer);
         add(currentPhase);
         add(phaseInfo);
         add(endPhase);
    }


    @Override
    public void updateMap(GameModel gameModel) {
        currentPlayer.setText(gameModel.getCurrentPlayer().getName());
        currentPhase.setText(gameModel.gameStatus.toString());

        switch (gameModel.gameStatus) {
            case TROOP_PLACEMENT_PHASE:
                phaseInfo.setText(" " + gameModel.getCurrentPlayer().getPlaceableArmies());
                endPhase.setEnabled(false);
                break;
            case SELECT_ATTACKING_PHASE:
                phaseInfo.setText("Select country to attack from");
                endPhase.setEnabled(true);
                break;
            case SELECT_DEFENDING_PHASE:
                phaseInfo.setText("Select country to attack");
                endPhase.setEnabled(true);
                break;
            case SELECT_TROOP_MOVING_FROM_PHASE:
                phaseInfo.setText("Select country to move troops from");
                endPhase.setEnabled(true);
                break;
            case SELECT_TROOP_MOVING_TO_PHASE:
                phaseInfo.setText("Select country to move troops to");
                endPhase.setEnabled(true);
                break;
            case WAITING:
                break;
        }
        repaint();
    }

    @Override
    public void onPlaceTroops(OneCountryEvent oce) {

    }

    @Override
    public void onAttack(TwoCountryEvent tce) {

    }

    @Override
    public void onTroopMovement(TwoCountryEvent tce) {

    }
}
