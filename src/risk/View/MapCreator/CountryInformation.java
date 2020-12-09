package risk.View.MapCreator;

import risk.Controller.EditorController;
import risk.Enums.StringGlobals;
import risk.Model.Continent;
import risk.Model.Country;

import javax.swing.*;
import java.awt.*;

public class CountryInformation extends JPanel {
    private Country country;

    private JLabel countryName;
    private JButton continentName;
    private JButton toggleNeighbours;

    public CountryInformation(MapEditorGUI mapEditorGUI, EditorController controller, DefaultListModel continentListModel){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel countryLabel = new JLabel("Country: ");
        JLabel continentLabel = new JLabel("Continent: ");

        countryName = new JLabel("");
        continentName = new JButton("Set Continent");
        toggleNeighbours = new JButton(StringGlobals.setNeighboursString);

        JList<Continent> continentJList = new JList<>(continentListModel);
        continentJList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        JScrollPane continentScroll = new JScrollPane(continentJList);

        continentName.addActionListener(e->{
            if (country.getContinent()!=null)
                continentJList.setSelectedValue(country.getContinent(), true);
            if (continentJList.getModel().getSize()==0){
                JOptionPane.showMessageDialog(null, "Add some continents first.");
                return;
            }
            do {
                JOptionPane.showMessageDialog(null, continentScroll);
            } while (continentJList.isSelectionEmpty());
            controller.editCountryContinent(country, continentJList.getSelectedValue());
        });

        toggleNeighbours.addActionListener(e -> {
            mapEditorGUI.resetCountryColors();

          if (toggleNeighbours.getText().equals(StringGlobals.setNeighboursString)){
              mapEditorGUI.colorCountryNeighbours(country);
              mapEditorGUI.setToggleNeighbours();
              toggleNeighbours.setText(StringGlobals.backString);
          } else {
              mapEditorGUI.resetToggleNeighbours();
              toggleNeighbours.setText(StringGlobals.setNeighboursString);
          }
        });

        add(countryLabel);
        add(countryName);
        add(continentLabel);
        add(continentName);
        add(toggleNeighbours);

        setVisible(false);
    }

    public Country getCountry(){
        return country;
    }

    public void resetCountry(){
        setVisible(false);
        toggleNeighbours.setText(StringGlobals.setNeighboursString);
    }

    public void update(){
        if (country==null)
            return;
        countryName.setText(country.getName());
        continentName.setText(country.getContinent()!=null ? country.getContinent().getName() : "Set Continent");
        repaint();
    }


    public void setCountry(Country country){
        this.country = country;
        countryName.setText(country.getName());
        continentName.setText(country.getContinent()!=null ? country.getContinent().getName() : "Set Continent");
        setVisible(true);
    }
}
