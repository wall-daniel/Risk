package risk.View.MapCreator;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class EditableCustomCountry extends JPanel implements MouseListener, MouseMotionListener {

    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;

    private boolean pressed = false;;

    Polygon countryPolygon;
    //TODO clicking on tranparent section to drag panel behind does nothing.

    MapEditorGUI mapEditorGUI;

    public EditableCustomCountry(MapEditorGUI mapEditorGUI, Polygon countryPolygon, String name) {
        this.countryPolygon = countryPolygon;
        this.setName(name);
        this.mapEditorGUI = mapEditorGUI;
        addMouseMotionListener(this);
    }

    /*
    @Override
    public void setUpCountryLabel(){
        this.countryLabel = new MoveableCountryLabel(getName());
        this.add(countryLabel);
    }
    */

    public Polygon getCountryPolygon(){
        return countryPolygon;
    }


    @Override
    public void mousePressed(MouseEvent e) {
        if (countryPolygon.contains(e.getPoint())) {
            screenX = e.getXOnScreen();
            screenY = e.getYOnScreen();

            myX = getX();
            myY = getY();
            pressed = true;
        } else {
            pressed = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (pressed) {
            int deltaX = e.getXOnScreen() - screenX;
            int deltaY = e.getYOnScreen() - screenY;

            setLocation(myX + deltaX, myY + deltaY);
        }
    }

    public EditableCustomCountry getCountryClicked(int x, int y){
        int highestLayer = -1;
        EditableCustomCountry editableCustomCountry = null;

        for (Component component : mapEditorGUI.getLayeredPane().getComponents()){
            if (component instanceof EditableCustomCountry){
                EditableCustomCountry ecc = (EditableCustomCountry) component;
                int layer = mapEditorGUI.getLayeredPane().getLayer(ecc);
                int xLocation = ecc.getLocationOnScreen().x;
                int yLocation = ecc.getLocationOnScreen().y;

                if (layer > highestLayer && ecc.getCountryPolygon().contains(x - xLocation, y - yLocation)){
                    highestLayer = layer;
                    editableCustomCountry = ecc;
                }
            }
        }

        return editableCustomCountry;
    }

    public boolean isPointInPolygon(int x, int y){
        return countryPolygon.contains(x, y);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (!(e.getSource() instanceof EditableCustomCountry))
            return;

        int x = e.getXOnScreen(), y = e.getYOnScreen();
        System.out.println("clicked: " + x + " , " + y);

        EditableCustomCountry countryClicked;

        if (((EditableCustomCountry) e.getSource()).isPointInPolygon(x, y))
            countryClicked = (EditableCustomCountry) e.getSource();
        else
            countryClicked = getCountryClicked(x, y);

        if (countryClicked == null)
            return;

        String countryName = countryClicked.getName();

        //Create the input panel for the user to input neighbours, continents etc.
        JPanel countryInfoPanel = new JPanel(new GridLayout(2, 3));

        JLabel countryNameLabel = new JLabel("Country Name");
        JLabel neighboursLabel = new JLabel("Select Neighbours");
        JLabel continentLabel = new JLabel("Select Continent");

        JTextField countryNameTextField = new JTextField(countryName);




        JList<String> neighboursJList = new JList<>(GameModel.getCountryNamesDefaultListModel(countryName));
        neighboursJList.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (GameModel.countryExists(countryName))
            neighboursJList.setSelectedIndices(GameModel.getCountry(countryName).getNeighbours());

        JList<String> continentJList = new JList<>(GameModel.getContinentNamesDefaultListModel());
        continentJList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        if (GameModel.countryExists(countryName))
            continentJList.setSelectedValue(GameModel.getCountry(countryName).getContinentName(), true);

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


        GameModel.editCountry(name, neighbourNames, continentName);
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
