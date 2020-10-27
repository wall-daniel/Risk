package risk.MapCreator;

import risk.Model.Continents;
import risk.Model.Countries;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EditableCustomContinent extends CustomContinent implements MouseListener, MouseMotionListener {

    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;

    public EditableCustomContinent(BufferedImage continentImage, String name) {
        super(continentImage, name);
        addMouseMotionListener(this);
        addMouseListener(this);
        setOpaque(false);
    }


    @Override
    public void mousePressed(MouseEvent e) {
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();

        myX = getX();
        myY = getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int deltaX = e.getXOnScreen() - screenX;
        int deltaY = e.getYOnScreen() - screenY;

        setLocation(myX + deltaX, myY + deltaY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //continent,
        JPanel countryInfoPanel = new JPanel(new GridLayout(2, 3));

        String countryName = e.getComponent().getName();

        JLabel countryNameLabel = new JLabel("Country Name");
        JLabel neighboursLabel = new JLabel("Select Neighbours");
        JLabel continentLabel = new JLabel("Select Continent");


        JTextField countryNameTextField = new JTextField(countryName);

        JList<String> neighboursJList = new JList<>(Countries.getCountryNamesDefaultListModel(countryName));
        neighboursJList.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (Countries.countryExists(countryName))
            neighboursJList.setSelectedIndices(Countries.getCountry(countryName).getNeighbours());


        JList<String> continentJList = new JList<>(Continents.getContinentNamesDefaultListModel());
        continentJList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        if (Countries.countryExists(countryName))
            continentJList.setSelectedValue(Countries.getCountry(countryName).getContinentName(), true);

        countryInfoPanel.add(countryNameLabel);
        countryInfoPanel.add(neighboursLabel);
        countryInfoPanel.add(continentLabel);

        countryInfoPanel.add(countryNameTextField);
        countryInfoPanel.add(neighboursJList);
        countryInfoPanel.add(continentJList);

        JOptionPane.showMessageDialog(this, countryInfoPanel);

        String name = countryNameTextField.getText();
        ArrayList<String> neighbourNames = neighboursJList.isSelectionEmpty() ? new ArrayList<>() : (ArrayList<String>) neighboursJList.getSelectedValuesList();
        String continentName = continentJList.getSelectedValue();

        Countries.editCountry(name, neighbourNames, continentName);
    }


    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
