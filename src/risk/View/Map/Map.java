package risk.View.Map;

import risk.Controller.Controller;
import risk.Enums.MapColor;
import risk.Listener.Events.OneCountryEvent;
import risk.Listener.Events.TwoCountryEvent;
import risk.View.Views.GameActionListener;
import risk.Model.GameModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Map extends JPanel implements GameActionListener {
    Controller controller;

    HashMap<String, CountryPanel> countryList = new HashMap<>();

    OneCountryEvent countryEvent = null;



    public Map(Controller controller) {
        this.controller = controller;
        setLayout(null);
        setBackground(MapColor.BACKGROUND_COLOR.getColor());
    }

    @Override
    public void updateMap(GameModel gameModel) {
        gameModel.getCountriesInLayerOrder().forEach(country -> {
            if (countryList.containsKey(country.getName())){
                countryList.get(country.getName()).updateCountry();
            } else {
                CountryPanel countryPanel = new CountryPanel(country, this.getSize(), controller, true);
                countryList.put(country.getName(), countryPanel);
                add(countryPanel);
            }
        });

        ArrayList<String> clickableCountries = controller.getClickableCountries();

        for (String countryNames: clickableCountries)
            countryList.get(countryNames).setColorClickable();

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
