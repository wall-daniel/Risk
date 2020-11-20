package risk.Controller;

import risk.Action.ActionBuilder;
import risk.Model.Country;
import risk.Model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class PlayerController extends Controller implements ActionListener {

    public PlayerController(GameModel gameModel, JFrame view) {
        super(gameModel, view);
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

    public void clickedInCountry(Country country) {
        if (country.isClickable()) {
            switch (gameModel.gameStatus) {
                case TROOP_PLACEMENT_PHASE:
                    gameModel.getCurrentPlayer().setFirstCountryOfAction(country);
                    gameModel.getCurrentPlayer().inputTroopCount(
                            "How many troops do you want to place?", 1, gameModel.getCurrentPlayer().getPlaceableArmies());
                    gameModel.doAction(gameModel.getCurrentPlayer().getAction());
                    break;
                case SELECT_ATTACKING_PHASE:
                case SELECT_TROOP_MOVING_FROM_PHASE:
                    gameModel.getCurrentPlayer().setFirstCountryOfAction(country);
                    gameModel.continuePhase();
                    gameModel.updateGame();
                    break;
                case SELECT_DEFENDING_PHASE:
                    gameModel.getCurrentPlayer().setSecondCountryOfAction(country);
                    Country attackingCountry = gameModel.getCurrentPlayer().getFirstCountryOfAction();
                    gameModel.getCurrentPlayer().inputTroopCount(
                            "How many troops do you want to attack with?", 1, Math.min(attackingCountry.getArmies() - 1, 3));
                    gameModel.doAction(gameModel.getCurrentPlayer().getAction());
                    break;
                case SELECT_TROOP_MOVING_TO_PHASE:
                    gameModel.getCurrentPlayer().setSecondCountryOfAction(country);
                    Country fromCountry = gameModel.getCurrentPlayer().getFirstCountryOfAction();
                    gameModel.getCurrentPlayer().inputTroopCount("How many troops do you want to move?", 1, fromCountry.getArmies() - 1);

                    // Make sure the user actually entered a valid number
                    if (gameModel.getCurrentPlayer().isTroopActionSet()) {
                        gameModel.doAction(gameModel.getCurrentPlayer().getAction());
                    }
                    break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals("Back")) {
            gameModel.doAction(new ActionBuilder().buildReset());
        } else if (actionEvent.getActionCommand().equals("Next")) {
            gameModel.doAction(new ActionBuilder().buildEnd());
        }

        gameModel.updateGame();
    }

}
