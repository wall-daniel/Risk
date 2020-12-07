package risk.View.MapCreator;

import risk.Controller.EditorController;
import risk.Enums.MapColor;
import risk.Model.Continent;
import risk.Model.Country;
import risk.Model.GameModel;
import risk.View.Main.MainGUI;
import risk.View.Map.CountryPanel;
import risk.View.Views.GameActionListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MapEditorGUI extends JFrame implements GameActionListener {
    private JLayeredPane layeredPane;
    private CountryInformation countryInformation;

    private int countryCounter;

    private EditorController controller;
    private String mapName;

    private boolean toggleNeighbours;

    private final HashMap<Country, EditableCountryPanel> countryList = new HashMap<>();

    private JPanel continentPanel;

    private JScrollPane continentScroll;

    public MapEditorGUI(){
        GameModel gameModel = new GameModel();
        this.mapName = JOptionPane.showInputDialog("Enter New Map Name");
        setup(gameModel);
    }

    public MapEditorGUI(String filename) {
        try {
            GameModel gameModel = new GameModel(filename, false);
            mapName = filename;
            setup(gameModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setup(GameModel gameModel){
        this.controller = new EditorController(gameModel, this);
        gameModel.addActionListener(this);

        countryCounter = 0;
        toggleNeighbours =false;

        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI(gameModel));
    }

    public JLayeredPane getLayeredPane(){
        return layeredPane;
    }

    private void createAndShowGUI(GameModel gameModel) {
        addComponentToPane(getContentPane(), gameModel);

        addJMenuBar();

        setUndecorated(true);
        setVisible(true);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        getContentPane().setBackground(MapColor.BACKGROUND_COLOR.getColor());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Custom Map Creator");
        pack();
        setVisible(true);

        controller.updateEditor();
    }

    public void addComponentToPane(Container pane, GameModel gameModel)  {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        this.countryInformation = new CountryInformation(this, controller,gameModel.getContinentListModel());

        this.continentPanel = new JPanel(new BorderLayout());
        continentPanel.setVisible(false);
        JTextField continentName = new JTextField("");
        JTextField continentBonus = new JTextField("");

        JButton submit = new JButton("Submit");
        JButton delete = new JButton("Delete");
        delete.setEnabled(false);
        JButton reset = new JButton("Reset");

        JList<Continent> continentJList = new JList<>(gameModel.getContinentListModel());
        continentJList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        continentJList.addListSelectionListener(e -> {
            if (!continentJList.isSelectionEmpty()) {
                continentName.setText(continentJList.getSelectedValue().getName());
                continentBonus.setText(String.valueOf(continentJList.getSelectedValue().getBonus()));
                submit.setText("Edit");
                delete.setEnabled(true);
            }
        });

        submit.addActionListener(e -> {
            try {
                int bonus = Integer.parseInt(continentBonus.getText());

                if (submit.getText().equals("Submit")){
                    controller.createNewContinent(continentName.getText(), bonus);
                } else {
                    controller.editContinentProperties(continentJList.getSelectedValue(), continentName.getText(), bonus);
                }

                continentName.setText("");
                continentBonus.setText("");
                submit.setText("Submit");
                delete.setEnabled(false);
            } catch (Exception ex){

            }
        });

        delete.addActionListener(e -> controller.deleteContinent(continentJList.getSelectedValue()));

        reset.addActionListener(e -> {
            continentJList.clearSelection();
            continentName.setText("");
            continentBonus.setText("");
            submit.setText("Submit");
            delete.setEnabled(false);
        });

        JPanel textPanel = new JPanel(new GridLayout(1, 2));
        textPanel.add(continentName);
        textPanel.add(continentBonus);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        buttonPanel.add(textPanel);
        buttonPanel.add(submit);
        buttonPanel.add(delete);
        buttonPanel.add(reset);

        continentScroll = new JScrollPane(continentJList);

        continentPanel.add(continentScroll, BorderLayout.CENTER);
        continentPanel.add(buttonPanel, BorderLayout.SOUTH);

        pane.add(layeredPane, BorderLayout.CENTER);
        pane.add(countryInformation, BorderLayout.EAST);
        pane.add(continentPanel, BorderLayout.WEST);
    }

    private void addJMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu add = new JMenu("Add");

        JMenuItem addCountry = new JMenuItem("Add Country");
        JMenuItem editContinents = new JMenuItem("Edit Continents");
        JMenuItem autoGenerateNeighbours = new JMenuItem("Auto-Generate Neighbours");
        JMenuItem saveMap = new JMenuItem("Save Map");
        JMenuItem quit = new JMenuItem("Quit");

        addCountry.addActionListener(e -> new CountryCreatorGUI(this));

        editContinents.addActionListener(e -> editContinents());

        autoGenerateNeighbours.addActionListener(e -> autoGenerateNeighbours());

        saveMap.addActionListener(e -> saveMap());

        quit.addActionListener(e -> {
            new MainGUI();
            this.dispose();
        });


        add.add(addCountry);
        add.add(editContinents);

        file.add(autoGenerateNeighbours);
        file.add(saveMap);
        file.add(quit);

        bar.add(file);
        bar.add(add);

        setJMenuBar(bar);
    }

    private void editContinents() {
        continentPanel.setVisible(!continentPanel.isVisible());
        repaint();
    }

    private void autoGenerateNeighbours(){
        controller.updateNeighbours();
    }

    private void saveMap() {
        controller.updateAllComponentLocations();
        controller.saveMap(mapName);
    }


    public void addNewCountry(String name, Polygon polygon){
        controller.createNewCountry(name, polygon);
    }


    @Override
    public void updateMap(GameModel gameModel) {
        //handling new/update of countries
        for (Country country : gameModel.getCountries()){
            if (countryList.containsKey(country)){ //update country
                countryList.get(country).updateCountry(); //updates panel
                if (toggleNeighbours) {
                    resetCountryColors();
                    colorCountryNeighbours(countryInformation.getCountry());
                }
            } else { // new country
                EditableCountryPanel cc = new EditableCountryPanel(country, this.getSize(), controller);
                countryList.put(country, cc);
                Insets insets = layeredPane.getInsets();
                Insets frameInset = getInsets();

                cc.setBounds(country.getPolygonPoint().x - insets.left, country.getPolygonPoint().y - insets.top - frameInset.top - getJMenuBar().getHeight(), cc.getCountry().getPolygon().getBounds().width + 30, cc.getCountry().getPolygon().getBounds().height + 30);
                cc.setBorder(BorderFactory.createLineBorder(Color.black));

                int layer = country.getLayer();
                if (layer == -1)
                    layer = countryCounter;

                layeredPane.add(cc, Integer.valueOf(layer));
                countryCounter++;
            }
        }

        countryInformation.update();

        //handling country deletions
        Set<Country> currentCountries = new HashSet<>(countryList.keySet());
        currentCountries.removeAll(gameModel.getCountries());
        currentCountries.forEach(e -> layeredPane.remove(countryList.remove(e)));
    }


    public boolean isToggleNeighbours() {
        return toggleNeighbours;
    }

    public void resetToggleNeighbours(){
        toggleNeighbours = false;
    }

    public void setToggleNeighbours() {
        toggleNeighbours = true;
    }

    public void resetCountryColors(){
        countryList.values().forEach(CountryPanel::resetColor);
        repaint();
    }

    public void colorCountryNeighbours(Country country){
        for (Country country1: countryList.keySet())
            if (country.getNeighbours().contains(country1.getName()))
                countryList.get(country1).setColorClickable();
        repaint();
    }


    public void setCountryInformation(Country country){
        countryInformation.setCountry(country);
    }

    public void resetCountryInformation() {
        resetCountryColors();
        countryInformation.resetCountry();
    }




}
