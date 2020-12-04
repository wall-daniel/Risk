package risk.View.MapCreator;

import risk.Controller.Controller;
import risk.Controller.EditorController;
import risk.Enums.MapColor;
import risk.Listener.Events.ContinentEvent;
import risk.Listener.Events.CountryEvent;
import risk.Model.Country;
import risk.Model.GameModel;
import risk.View.Main.MainGUI;
import risk.View.Map.CountryPanel;
import risk.View.Views.GameModelListener;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MapEditorGUI extends JFrame implements GameModelListener {
    private JLayeredPane layeredPane;
    private CountryInformation countryInformation;

    private int countryCounter;

    private EditorController controller;
    private String mapName;

    private boolean toggleNeighbours;

    private final HashMap<String, EditableCountryPanel> countryList = new HashMap<>();

    public MapEditorGUI(){
        try {
            GameModel gameModel = new GameModel();
            setup(gameModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.mapName = JOptionPane.showInputDialog("Enter New Map Name");

        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    public MapEditorGUI(String filename) {
        try {
            GameModel gameModel = new GameModel(filename, false);
            setup(gameModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapName = filename;
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }


    public JLayeredPane getLayeredPane(){
        return layeredPane;
    }

    private void setup(GameModel gameModel){
        this.controller = new EditorController(gameModel, this);
        countryInformation = new CountryInformation(this, controller);
        gameModel.addGameModelListener(this);
        gameModel.addGameModelListener(countryInformation);

        countryCounter = 0;
        toggleNeighbours =false;
    }

    private void createAndShowGUI() {
        addComponentToPane(getContentPane());

        addJMenuBar();

        getContentPane().setBackground(MapColor.BACKGROUND_COLOR.getColor());
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setTitle("Custom Map Creator");
        pack();
        setVisible(true);

        controller.updateEditor();
    }

    private void addJMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Add");

        JMenuItem addCountry = new JMenuItem("Add Country");
        JMenuItem addContinent = new JMenuItem("Add Continent");
        JMenuItem autoGenerateNeighbours = new JMenuItem("Auto-Generate Neighbours");
        JMenuItem saveMap = new JMenuItem("Save Map");
        JMenuItem quit = new JMenuItem("Quit");

        addCountry.addActionListener(e -> {
            new CountryCreatorGUI(this);
        });

        addContinent.addActionListener(e -> {
            addNewContinent();
        });

        autoGenerateNeighbours.addActionListener(e -> {
            autoGenerateNeighbours();
        });

        saveMap.addActionListener(e -> {
            saveMap();
        });


        quit.addActionListener(e -> {
            new MainGUI();
            this.dispose();
        });

        menu.add(addCountry);
        menu.add(addContinent);
        menu.add(saveMap);
        menu.add(quit);

        bar.add(menu);

        setJMenuBar(bar);
    }

    private void addNewContinent() {
        String continentName = JOptionPane.showInputDialog("Enter Continent Name");
        int continentBonus = Integer.parseInt(JOptionPane.showInputDialog("Enter Continent Bonus"));
        controller.createNewContinent(continentName, continentBonus);
    }

    private void autoGenerateNeighbours(){
        controller.updateNeighbours();
    }

    private void saveMap() {
        controller.updateAllComponentLocations();

        controller.saveMap(mapName);
    }

    public void addComponentToPane(Container pane)  {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        pane.add(layeredPane, BorderLayout.CENTER);
        pane.add(countryInformation, BorderLayout.EAST);
    }


    public void addNewCountry(String name, Polygon polygon){
        controller.createNewCountry(name, polygon);
    }

    @Override
    public void onNewCountry(CountryEvent oce) {
        Country country = oce.getCountry();
        EditableCountryPanel cc = new EditableCountryPanel(country, this.getSize(), controller);

        countryList.put(country.getName(), cc);

        Insets insets = layeredPane.getInsets();
        Insets frameInset = getInsets();

        cc.setBounds(country.getPolygonPoint().x - insets.left, country.getPolygonPoint().y - insets.top - frameInset.top - getJMenuBar().getHeight(), cc.getCountry().getPolygon().getBounds().width + 30, cc.getCountry().getPolygon().getBounds().height + 30);
        cc.setBorder(BorderFactory.createLineBorder(Color.black)); //TODO will remove

        int layer = country.getLayer();
        if (layer == -1)
            layer = countryCounter;

        layeredPane.add(cc, Integer.valueOf(layer));
        countryCounter++;
    }


    @Override
    public void onEditCountry(CountryEvent oce) {
        Country country = oce.getCountry();

        //change of name
        if (!oce.getInitialCountryName().equals("")) {
            String oldCountryName = oce.getInitialCountryName();

            EditableCountryPanel ecc = countryList.remove(oldCountryName);
            countryList.put(country.getName(), ecc);

            ecc.updateCountry();
        }

        if (toggleNeighbours) {
            resetCountryColors();
            colorCountryNeighbours(country);
            for (String neighbour : country.getNeighbours())
                System.out.println(neighbour);
        }
    }

    @Override
    public void onDeleteCountry(Country oce) {

    }


    public boolean isToggleNeighbours() {
        return toggleNeighbours;
    }

    public void setCountryInformation(Country country){
        countryInformation.setCountry(country);
    }

    public void resetCountryColors(){
        countryList.values().forEach(e -> e.resetColor());
        repaint();
    }

    public void colorCountryNeighbours(Country country){
        for (String neighbour : country.getNeighbours())
            countryList.get(neighbour).setColorClickable();
        repaint();
    }

    public void resetCountryInformation() {
        resetCountryColors();
        countryInformation.resetCountry();
    }

    public void resetToggleNeighbours(){
        toggleNeighbours = false;
    }

    public void setToggleNeighbours() {
        toggleNeighbours = true;
    }
}
