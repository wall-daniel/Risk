package risk.View.MapCreator;

import risk.Controller.Controller;
import risk.Controller.EditorController;
import risk.Enums.MapColor;
import risk.Listener.Events.ContinentEvent;
import risk.Listener.Events.CountryEvent;
import risk.Model.GameModel;
import risk.View.Main.MainGUI;
import risk.View.Views.GameModelListener;

import javax.swing.*;
import java.awt.*;

public class MapEditorGUI extends JFrame implements GameModelListener {
    private JLayeredPane layeredPane;
    private int countryCounter;

    private EditorController controller;
    private String mapName;

    public MapEditorGUI(){
        countryCounter = 0;

        try {
            GameModel gameModel = new GameModel();
            gameModel.addGameModelListener(this);
            this.controller = new EditorController(gameModel, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.mapName = JOptionPane.showInputDialog("Enter New Map Name");

        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    public MapEditorGUI(String filename) {
        try {
            GameModel gameModel = new GameModel(filename);
            gameModel.addGameModelListener(this);
            this.controller = new EditorController(gameModel, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapName = filename;
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }


    public JLayeredPane getLayeredPane(){
        return layeredPane;
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
        System.out.println("controller must add new continent");
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
    }


    public void addNewCountry(String name, Polygon polygon){
        controller.createNewCountry(name, polygon);
    }

    @Override
    public void onNewCountry(CountryEvent oce) {
        EditableCountryPanel cc = new EditableCountryPanel(oce.getFirstCountry(), this.getSize(), controller);

        Insets insets = layeredPane.getInsets();
        Insets frameInset = getInsets();

        cc.setBounds(oce.getFirstCountry().getPolygonPoint().x - insets.left, oce.getFirstCountry().getPolygonPoint().y - insets.top - frameInset.top - getJMenuBar().getHeight(), cc.getCountry().getPolygon().getBounds().width + 30, cc.getCountry().getPolygon().getBounds().height + 30);
        cc.setBorder(BorderFactory.createLineBorder(Color.black)); //TODO will remove

        int layer = oce.getFirstCountry().getLayer();
        if (layer == -1)
            layer = countryCounter;

        layeredPane.add(cc, Integer.valueOf(layer));
        countryCounter++;
    }

    @Override
    public void onNewContinent(ContinentEvent cce) {




    }
}
