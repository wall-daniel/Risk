package risk.View.Map;

import risk.Controller.PlayerController;
import risk.Enums.MapColor;
import risk.Model.GameModel;
import risk.View.Views.GameActionListener;

import javax.swing.*;
import java.util.HashMap;

public class Map extends JPanel implements GameActionListener {

    private final PlayerController controller;
    private final HashMap<String, CountryPanel> countryList = new HashMap<>();

    public Map(PlayerController controller) {
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
                CountryPanel countryPanel = new CountryPanel(country, this.getSize(), controller);
                countryList.put(country.getName(), countryPanel);
                add(countryPanel);
            }

            if (country.isClickable())
                countryList.get(country.getName()).setColorClickable();

        });

        repaint();
    }

    @Override
    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
