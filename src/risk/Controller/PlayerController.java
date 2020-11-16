package risk.Controller;

import risk.Model.Country;
import risk.Model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PlayerController extends Controller implements ActionListener {


    private AttackController attackController;
    private MovementController movementController;

    public PlayerController(GameModel gameModel, JFrame view) {
        super(gameModel, view);
        this.attackController = new AttackController(gameModel, view);
        this.movementController = new MovementController(gameModel, view);
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

    @Override
    protected void countryClicked(MouseEvent mouseEvent) {
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
