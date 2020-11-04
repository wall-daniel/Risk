package risk.View.MapCreator;

import risk.Controller.Controller;
import risk.Enums.DrawingEnum;
import risk.Enums.MapColor;
import risk.Model.*;

import java.awt.*;
import javax.swing.*;

public class MapEditorGUI extends JFrame{
    JLabel status;
    JLayeredPane layeredPane;
    int countryCounter;

    Controller controller;
    String mapName;

    public MapEditorGUI(Controller controller){
        countryCounter = 0;
        this.controller = controller;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public JLayeredPane getLayeredPane(){
        return layeredPane;
    }

    private void createAndShowGUI() {
        mapName = JOptionPane.showInputDialog("Enter Map Name");

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

        addCountry.addActionListener(e -> {
            new CountryCreatorGUI(this);
            status.setText(DrawingEnum.COUNTRIES.getText());
        });

        addContinent.addActionListener(e -> {
            String continentName = JOptionPane.showInputDialog("Enter Continent Name");
            Continent continent = new Continent(continentName);
            Continents.addContinent(continentName, continent);
        });

        saveMap.addActionListener(e -> {
            //TODO have the controller save the map given mapName
            //controller.saveMap(mapName);
        });

        menu.add(addCountry);
        menu.add(addContinent);
        menu.add(saveMap);

        bar.add(menu);

        setJMenuBar(bar);
    }

    private void loadMap(){


    }

    public void addComponentToPane(Container pane)  {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        status = new JLabel(DrawingEnum.COUNTRIES.getText(), SwingConstants.CENTER);

        pane.add(status, BorderLayout.NORTH);
        pane.add(layeredPane, BorderLayout.CENTER);
    }

    //Saves images
    public void addNewCountry(Polygon image, String name){ //we need to save image as polygon instead
        //EditableCustomCountry cc = new EditableCustomCountry(image, name);
        Country country = new Country(name);
        Countries.addCountry(name, country);

        Insets insets = layeredPane.getInsets();
        //cc.setBounds(insets.left, insets.top, image.getWidth(), image.getHeight());
        //cc.setBorder(BorderFactory.createLineBorder(Color.black));

        //layeredPane.add(image, Integer.valueOf(countryCounter));
        countryCounter++;
    }
}
