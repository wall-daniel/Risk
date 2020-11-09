package risk.View.Map;

import risk.Controller.Controller;
import risk.Enums.MapColor;
import risk.Model.GameModel;
import risk.View.Views.GameActionListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Map extends JPanel implements GameActionListener {

    private final Controller controller;
    private final HashMap<String, CountryPanel> countryList = new HashMap<>();

    public Map(Controller controller) {
        this.controller = controller;

        setLayout(null);
        setBackground(MapColor.BACKGROUND_COLOR.getColor());
    }

    @Override
    public void updateMap(GameModel gameModel) {
        gameModel.getCountriesInLayerOrder().forEach(country -> {
            if (countryList.containsKey(country.getName())) {
                countryList.get(country.getName()).updateCountry();
            } else {
                CountryPanel countryPanel = new CountryPanel(country, this.getSize(), controller, true);
                countryList.put(country.getName(), countryPanel);
                add(countryPanel);
            }
        });

        ArrayList<String> clickableCountries = controller.getClickableCountries();

        for (String countryNames : clickableCountries)
            countryList.get(countryNames).setColorClickable();

        repaint();
    }
}
