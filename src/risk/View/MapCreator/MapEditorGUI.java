package risk.View.MapCreator;

import risk.Controller.Controller;
import risk.Enums.DrawingEnum;
import risk.Enums.MapColor;
import risk.Listener.Events.ContinentEvent;
import risk.Listener.Listeners.GameModelListener;
import risk.Listener.Events.OneCountryEvent;
import risk.Model.*;
import risk.View.Main.MainGUI;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class MapEditorGUI extends JFrame implements GameModelListener {
    JLabel status;
    JLayeredPane layeredPane;
    int countryCounter;

    Controller controller;
    String mapName;

    public static void main(String args[]){
        new MapEditorGUI(null);
    }

    public MapEditorGUI(Controller controller){
        countryCounter = 0;
        this.controller = controller;
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }


    public JLayeredPane getLayeredPane(){
        return layeredPane;
    }

    private void createAndShowGUI() {
        mapName = JOptionPane.showInputDialog("Enter New Map Name");

        addComponentToPane(getContentPane());

        addJMenuBar();

        getContentPane().setBackground(MapColor.BACKGROUND_COLOR.getColor());
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setTitle("Custom Map Creator");
        pack();
        setVisible(true);
    }

    private void addJMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Add");

        JMenuItem addCountry = new JMenuItem("Add Country");
        JMenuItem addContinent = new JMenuItem("Add Continent");
        JMenuItem saveMap = new JMenuItem("Save Map");
        JMenuItem quit = new JMenuItem("Quit");

        addCountry.addActionListener(e -> {
            new CountryCreatorGUI(this);
            status.setText(DrawingEnum.COUNTRIES.getText());
        });

        addContinent.addActionListener(e -> {
            addNewContinent();

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


    private void loadMap(){


    }

    private void saveMap() {
        //TODO have the controller save the map
        controller.saveMap();
    }



    public void addComponentToPane(Container pane)  {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        status = new JLabel(DrawingEnum.COUNTRIES.getText(), SwingConstants.CENTER);

        pane.add(status, BorderLayout.NORTH);
        pane.add(layeredPane, BorderLayout.CENTER);
    }


    public void editCountry(String name, ArrayList<String> neighbourNames, String continentName) {
        controller.editCountry(name, neighbourNames, continentName);
        System.out.println("controller must edit country: " + name);
    }

    public void addNewCountry(String name, Polygon polygon){
        controller.createNewCountry(name, polygon);
        System.out.println("controller must add new country");
    }

    @Override
    public void onNewCountry(OneCountryEvent oce) {
        EditableCustomCountry cc = new EditableCustomCountry(this, oce.getFirstCountry().getName(), oce.getFirstCountry().getPolygon());
        Insets insets = layeredPane.getInsets();
        cc.setBounds(insets.left, insets.top, cc.getCountryPolygon().getBounds().width + 30, cc.getCountryPolygon().getBounds().height + 30);

        cc.setBorder(BorderFactory.createLineBorder(Color.black)); //TODO will remove

        layeredPane.add(cc, Integer.valueOf(countryCounter));
        countryCounter++;
    }

    @Override
    public void onNewContinent(ContinentEvent cce) {




    }
}
