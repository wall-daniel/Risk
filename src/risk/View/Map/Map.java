package risk.View.Map;

import risk.Controller.Controller;
import risk.Enums.MapColor;
import risk.Listener.Events.OneCountryEvent;
import risk.Listener.Events.TwoCountryEvent;
import risk.Listener.Listeners.GameActionListener;
import risk.Model.Country;
import risk.Model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Map extends JPanel implements GameActionListener {
    Controller controller;

    HashMap<String, CountryPanel> countryList = new HashMap<>();

    OneCountryEvent countryEvent = null;

    public Map(Controller controller) {
        this.controller = controller;

        setLayout(null);
        setBackground(MapColor.BACKGROUND_COLOR.getColor());
        this.controller.addAsGameActionListener(this);

        addEndButton();
    }

    private JButton endButton;

    private void addEndButton() {
        endButton = new JButton("End button");
        endButton.setSize(120, 400);
        add(endButton);
        endButton.addActionListener(controller);
    }

    @Override
    public void updateMap(GameModel gameModel) {
        gameModel.getCountries().forEach(country -> {
            CountryPanel countryPanel = new CountryPanel(country, this.getSize(), controller);
            countryList.put(country.getName(), countryPanel);
            add(countryPanel);
        });

        switch (gameModel.gameStatus) {
            case TROOP_PLACEMENT_PHASE:
                endButton.setText("Troops: " + gameModel.getCurrentPlayer().getPlaceableArmies());
                break;
            case SELECT_ATTACKING_PHASE:
                endButton.setText("End attack");
                break;
            case SELECT_DEFENDING_PHASE:
                endButton.setText("Stop attack");
                break;
            case SELECT_TROOP_MOVING_FROM_PHASE:
                endButton.setText("Skip moving, end");
                break;
            case SELECT_TROOP_MOVING_TO_PHASE:
                endButton.setText("Don't move from");
                break;
            case WAITING:
                break;
        }
        repaint();
    }

    @Override
    public void onPlaceTroops(OneCountryEvent oce) {
        countryEvent = oce;
//        polygonList.put(oce.getFirstCountry().getName(), oce.getFirstCountry().getPolygon());
        repaint();
    }

    @Override
    public void onAttack(TwoCountryEvent tce) {
        countryEvent = tce;
//        polygonList.put(tce.getFirstCountry().getName(), tce.getFirstCountry().getPolygon());
//        polygonList.put(tce.getSecondCountry().getName(), tce.getSecondCountry().getPolygon());
        repaint();
    }

    @Override
    public void onTroopMovement(TwoCountryEvent tce) {
        countryEvent = tce;
//        polygonList.put(tce.getFirstCountry().getName(), tce.getFirstCountry().getPolygon());
//        polygonList.put(tce.getSecondCountry().getName(), tce.getSecondCountry().getPolygon());
        repaint();
    }
}
