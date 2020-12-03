package risk.View.MapCreator;

import risk.Controller.EditorController;
import risk.Listener.Events.CountryEvent;
import risk.Model.Country;
import risk.Model.GameModel;
import risk.View.Views.GameModelListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CountryInformation extends JPanel implements GameModelListener {
    private Country country;

    private JLabel countryName;
    private JButton continentName;
    private JButton toggleNeighbours;
    private JButton deleteCountry;

    public CountryInformation(MapEditorGUI mapEditorGUI, EditorController controller){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        countryName = new JLabel("");
        /*
        countryName.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter Country Name");
            controller.editCountryName(country, name);
        });*/

        continentName = new JButton("");
        continentName.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter Continent Name");
            controller.editCountryContinent(country, name);
        });

        toggleNeighbours = new JButton("Set Neighbours");
        toggleNeighbours.addActionListener(e -> {
          if (toggleNeighbours.getText().equals("Set Neighbours")){
              mapEditorGUI.colorCountryNeighbours(country);
              mapEditorGUI.setToggleNeighbours();
              toggleNeighbours.setText("Back");
          } else {
              mapEditorGUI.resetCountryColors();
              mapEditorGUI.resetToggleNeighbours();
              toggleNeighbours.setText("Set Neighbours");
          }
        });

        deleteCountry = new JButton("Delete Country");
        deleteCountry.addActionListener(e -> {

        });

        add(countryName);
        add(continentName);
        add(toggleNeighbours);

        setVisible(false);
    }

    public void resetCountry(){
        setVisible(false);
        toggleNeighbours.setText("Set Neighbours");
    }


    public void setCountry(Country country){
        this.country = country;
        countryName.setText(country.getName());
        continentName.setText(country.getContinent()!=null ? country.getContinent().getName() : "Set Continent");
        setVisible(true);
    }

    @Override
    public void onEditCountry(CountryEvent oce) {
        setCountry(oce.getCountry());
    }

    @Override
    public void onDeleteCountry(Country oce) {

    }


    @Override
    public void onNewCountry(CountryEvent oce) {

    }
}
