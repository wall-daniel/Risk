package risk.Controller;

import risk.View.Map.CountryPanel;
import risk.View.MapCreator.EditableCountryPanel;
import risk.View.MapCreator.MapEditorGUI;
import risk.View.Views.GameActionListener;
import risk.View.Views.GameModelListener;

import risk.Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;
import java.util.*;

public class Controller implements MouseListener, ActionListener {

    protected GameModel gameModel;

    protected JFrame gameView;

    private AttackController attackController;
    private MovementController movementController;

    public Controller(GameModel gameModel, JFrame view) {
        this.gameModel = gameModel;
        this.gameView = view;
        this.attackController = new AttackController(gameModel, view);
        this.movementController = new MovementController(gameModel, view);
    }

    public GameModel getGameModel(){
        return gameModel;
    }

    public void clickedInCountry(Country country) {
        switch (gameModel.gameStatus) {
            case TROOP_PLACEMENT_PHASE:
                placeArmies(country);
                break;
            case SELECT_ATTACKING_PHASE:
                attackController.setAttackingCountry(country);
                break;
            case SELECT_DEFENDING_PHASE:
                attackController.setDefendingCountry(country);
                break;
            case SELECT_TROOP_MOVING_FROM_PHASE:
                movementController.setMovingFromCountry(country);
                break;
            case SELECT_TROOP_MOVING_TO_PHASE:
                movementController.setMovingToCountry(country);
                break;
        }
    }

    private void placeArmies(Country country) {
        // Make sure that the country is owned by the player
        if (country.getPlayer() == gameModel.getCurrentPlayer()) {
            try {
                String result = JOptionPane.showInputDialog(
                        gameView,
                        "How many armies do you want to place in " + country.getName() + "?",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // If it returns null then they closed or cancelled
                if (result == null) {
                    return;
                }

                // Update the armies in the country
                int armies = Integer.parseInt(result);
                if (armies < 1) {
                    showErrorMessage("Have to supply a number greater than 0.");
                    return;
                }
                gameModel.placeArmies(country, armies);

                // Check if done placing troops
                if (gameModel.donePlacingArmies()) {
                    gameModel.nextPhase();
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorMessage("Error getting number: " + e.getMessage());
            }
        }
    }

    /**
     * @param errorMessage to be displayed to user.
     */
    private void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(gameView, errorMessage, "Error", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Finds highest layered country and sends a click of that country to determine what action is to be done.
     *
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        // Find the thing clicked on that has the highest layer (e.g. one on top)
        if (mouseEvent.getSource() instanceof CountryPanel){
            int highestLayer = -1;
            Country clickedCountry = null;


            for (Country country : gameModel.getCountries()) {
                Polygon translated = new Polygon(country.getPolygon().xpoints, country.getPolygon().ypoints, country.getPolygon().npoints);
                translated.translate(country.getPolygonPoint().x, country.getPolygonPoint().y);


                if (translated.contains(mouseEvent.getX(), mouseEvent.getY()) && country.getLayer() > highestLayer) {
                    highestLayer = country.getLayer();
                    clickedCountry = country;
                }
            }

            // Make sure that a country was clicked
            if (clickedCountry != null) clickedInCountry(clickedCountry);
        }
    }



    @Override
    public void mousePressed(MouseEvent mouseEvent) { }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) { }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) { }

    @Override
    public void mouseExited(MouseEvent mouseEvent) { }

    public void startGame() {
        gameModel.startGame();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals("Back")){
            switch (gameModel.gameStatus) {
                case TROOP_PLACEMENT_PHASE:
                case SELECT_ATTACKING_PHASE:
                    break;
                case SELECT_DEFENDING_PHASE:
                    attackController.resetController();
                    gameModel.resetPhase();
                    break;
                case SELECT_TROOP_MOVING_FROM_PHASE:
                    break;
                case SELECT_TROOP_MOVING_TO_PHASE:
                    movementController.resetController();
                    gameModel.resetPhase();
                    break;
            }
        } else {
            switch (gameModel.gameStatus) {
                case TROOP_PLACEMENT_PHASE:
                    break;
                case SELECT_ATTACKING_PHASE:
                case SELECT_TROOP_MOVING_FROM_PHASE:
                    gameModel.nextPhase();
                    break;
                case SELECT_DEFENDING_PHASE:
                    attackController.resetController();
                    gameModel.nextPhase();
                    break;
                case SELECT_TROOP_MOVING_TO_PHASE:
                    movementController.resetController();
                    gameModel.nextPhase();
                    break;
            }
        }

        gameModel.updateGame();
    }

    public ArrayList<String> getClickableCountries(){
        switch (gameModel.gameStatus) {
            case TROOP_PLACEMENT_PHASE:
            case SELECT_ATTACKING_PHASE:
            case SELECT_TROOP_MOVING_FROM_PHASE:
                return gameModel.getPlaceableCountries();
            case SELECT_DEFENDING_PHASE:
                return gameModel.getAttackableCountries(attackController.getAttackingCountry());
            case SELECT_TROOP_MOVING_TO_PHASE:
                return gameModel.getMoveTroopsToCountries(movementController.getFromCountry());
            default:
                return new ArrayList<>();
        }
    }
}
