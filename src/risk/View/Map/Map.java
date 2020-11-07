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

    boolean initialized;


    public Map(Controller controller) {
        this.controller = controller;
        this.initialized = false;
        setLayout(null);
        setBackground(MapColor.BACKGROUND_COLOR.getColor());
        this.controller.addAsGameActionListener(this);

    }


    @Override
    public void updateMap(GameModel gameModel) {
        gameModel.getCountries().forEach(country -> {
            CountryPanel countryPanel = new CountryPanel(country, this.getSize(), controller);
            countryList.put(country.getName(), countryPanel);
            if (!initialized)
                add(countryPanel);
        });

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
