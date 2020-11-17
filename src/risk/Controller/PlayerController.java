package risk.Controller;

import risk.Action.ActionBuilder;
import risk.Action.Attack;
import risk.Action.Fortify;
import risk.Model.Country;
import risk.Model.GameModel;
import risk.Players.HumanPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PlayerController extends Controller implements ActionListener {

    private Country countryClicked;

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
        switch (gameModel.gameStatus) {
            case TROOP_PLACEMENT_PHASE:
                gameModel.getCurrentPlayer().setFirstCountryOfAction(country);
                gameModel.getCurrentPlayer().inputTroopCount(
                        "How many troops do you want to place?", 1, gameModel.getCurrentPlayer().getPlaceableArmies());
                gameModel.doAction(gameModel.getCurrentPlayer().getAction());
                break;
            case SELECT_ATTACKING_PHASE:
                gameModel.getCurrentPlayer().setFirstCountryOfAction(country);
                System.out.println("selecting country to attack");
                gameModel.updateGame();
                break;
            case SELECT_DEFENDING_PHASE:
                gameModel.getCurrentPlayer().setSecondCountryOfAction(country);
                System.out.println("selecting country to defend");
                Country attackingCountry = gameModel.getCurrentPlayer().getFirstCountryOfAction();
                gameModel.getCurrentPlayer().inputTroopCount(
                        "How many troops do you want to attack with?", 1,  Math.min(attackingCountry.getArmies() - 1, 3));
                gameModel.doAction(gameModel.getCurrentPlayer().getAction());
                break;
            case SELECT_TROOP_MOVING_FROM_PHASE:
                gameModel.getCurrentPlayer().setFirstCountryOfAction(country);
                System.out.println("selecting country to move troops from");
                gameModel.updateGame();
                break;
            case SELECT_TROOP_MOVING_TO_PHASE:
                gameModel.getCurrentPlayer().setSecondCountryOfAction(country);
                System.out.println("selecting country to move troops to");
                Country fromCountry = gameModel.getCurrentPlayer().getFirstCountryOfAction();
                gameModel.getCurrentPlayer().inputTroopCount(
                        "How many troops do you want to move?", 1, fromCountry.getArmies() - 1);
                gameModel.doAction(gameModel.getCurrentPlayer().getAction());
                break;
        }

    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals("Back")){
            gameModel.doAction(new ActionBuilder().buildReset());
        } else if (actionEvent.getActionCommand().equals("Next")){
            gameModel.doAction(new ActionBuilder().buildEnd());
        }

        gameModel.updateGame();
    }




}
