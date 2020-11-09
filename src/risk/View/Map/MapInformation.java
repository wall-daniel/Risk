package risk.View.Map;

import risk.Controller.Controller;
import risk.Enums.PlayerColor;
import risk.Model.GameModel;
import risk.View.Views.GameActionListener;

import javax.swing.*;
import java.awt.*;

public class MapInformation extends JPanel implements GameActionListener {

    private JButton backButton;
    private JLabel currentPlayer;
    private JLabel currentPhase;
    private JLabel phaseInfo;
    private JButton nextButton;

    public MapInformation(Controller controller) {
        setUp(controller);
    }

    private void setUp(Controller controller) {
        JToolBar toolBar = new JToolBar("Controls & Info");

        addButtons(toolBar, controller);

        nextButton.addActionListener(controller);

        add(toolBar);
    }

    protected void addButtons(JToolBar toolBar, Controller controller) {
        backButton = new JButton("Back");
        backButton.addActionListener(controller);
        backButton.setActionCommand("Back");
        toolBar.add(backButton);

        JPanel info = new JPanel(new GridLayout(3, 1));
        currentPlayer = new JLabel("CurrentPlayer");
        currentPhase = new JLabel("CurrentPhase");
        phaseInfo = new JLabel("PhaseInfo");
        info.add(currentPlayer);
        info.add(currentPhase);
        info.add(phaseInfo);
        toolBar.add(info);

        nextButton = new JButton("Next");
        nextButton.addActionListener(controller);
        nextButton.setActionCommand("Next");
        toolBar.add(nextButton);
    }


    @Override
    public void updateMap(GameModel gameModel) {
        currentPlayer.setText(gameModel.getCurrentPlayer().getName());
        currentPlayer.setForeground(PlayerColor.getPlayerColor(gameModel.getCurrentPlayer().getIndex()));
        currentPhase.setText(gameModel.gameStatus.toString());

        switch (gameModel.gameStatus) {
            case TROOP_PLACEMENT_PHASE:
                phaseInfo.setText(" " + gameModel.getCurrentPlayer().getPlaceableArmies());
                backButton.setEnabled(false);
                nextButton.setEnabled(false);
                break;
            case SELECT_ATTACKING_PHASE:
                phaseInfo.setText("Select country to attack from");
                backButton.setEnabled(false);
                nextButton.setEnabled(true);
                break;
            case SELECT_DEFENDING_PHASE:
                phaseInfo.setText("Select country to attack");
                backButton.setEnabled(true);
                nextButton.setEnabled(true);
                break;
            case SELECT_TROOP_MOVING_FROM_PHASE:
                phaseInfo.setText("Select country to move troops from");
                backButton.setEnabled(false);
                nextButton.setEnabled(true);
                break;
            case SELECT_TROOP_MOVING_TO_PHASE:
                phaseInfo.setText("Select country to move troops to");
                backButton.setEnabled(true);
                nextButton.setEnabled(true);
                break;
        }
        repaint();
    }
}
