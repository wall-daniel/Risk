package risk.MapCreator;

import risk.Enums.DrawingEnum;
import risk.Enums.FileNames;
import risk.Enums.MapColor;
import risk.Enums.PlayerColor;
import risk.Model.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MapEditor extends JFrame{
    public static void main(String[] args){
        new MapEditor();
    }

    JLabel status;
    static JLayeredPane layeredPane;
    int countryCounter;
    String path;

    public MapEditor(){
        countryCounter = 0;

        /* Use an appropriate Look and Feel */
        try {
            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        // Schedule a job for the event patch thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private void createAndShowGUI() {
        String mapName = JOptionPane.showInputDialog("Enter Map Name");
        path = "maps\\" + mapName + "\\";

        addComponentToPane(getContentPane());

        addJMenuBar();

        getContentPane().setBackground(MapColor.BACKGROUND_COLOR.getColor());
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Custom Map Creator");
        pack();
        setVisible(true);
    }

    private void addJMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Add");

        JMenuItem addCountry = new JMenuItem("Add Country");
        JMenuItem addContinent = new JMenuItem("Add Continent");
        JMenuItem loadMap = new JMenuItem("Load Map");
        JMenuItem saveMap = new JMenuItem("Save Map");

        addCountry.addActionListener(e -> {
            new CountryCreator(this);
            status.setText(DrawingEnum.COUNTRIES.getText());
        });

        addContinent.addActionListener(e -> {
            String continentName = JOptionPane.showInputDialog("Enter Continent Name");
            int continentBonus = Integer.valueOf(JOptionPane.showInputDialog("Enter Continent Bonus"));
            Continent continent = new Continent(continentName, continentBonus);
            Continents.addContinent(continentName, continent);
        });

        loadMap.addActionListener(e -> {
            String mapName = JOptionPane.showInputDialog("Enter Map Name");
            loadMap(mapName);
        });


        saveMap.addActionListener(e -> {
            try {
                saveMap();
            } catch (Exception e1){ }

        });


        menu.add(addCountry);
        menu.add(addContinent);
        menu.add(loadMap);
        menu.add(saveMap);

        bar.add(menu);

        setJMenuBar(bar);
    }

    private void loadMap(String mapName){
        File f = new File(path);
        if (f.exists() && f.isDirectory()) {

        }
    }

    private void saveMap() throws IOException {
        new File(path + FileNames.POLYGONS.getPath()).mkdirs();
        new File(path + FileNames.CONTINENTS.getPath()).mkdirs();
        new File(path + FileNames.COUNTRIES.getPath()).mkdirs();
        new File(path + FileNames.LOCATIONS.getPath()).mkdirs();

        System.out.println("attempting to write countries.");
        for (Country country: Countries.getCountries().values()){
            ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(path + FileNames.COUNTRIES.getPath() + country.getName()));
            oos1.writeObject(country);
            oos1.flush();
            oos1.close();
        }
        System.out.println("countries written");

        System.out.println("attempting to write continents");
        for (Continent continent : Continents.getContinents().values()){
            ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(path + FileNames.CONTINENTS.getPath() + continent.getName()));
            oos1.writeObject(continent);
            oos1.flush();
            oos1.close();
        }
        System.out.println("continents written");

        System.out.println("attempting to write locations of all components.");
        for (Component c : layeredPane.getComponents()){
            if (c instanceof EditableCustomCountry) {
                System.out.println("COMPONENT: " + c.getName());
                ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(path + FileNames.LOCATIONS.getPath() + c.getName()));
                oos2.writeObject(c.getLocationOnScreen());
                oos2.flush();
                oos2.close();
            }
        }
        System.out.println("components written");

        System.out.println("attempting to write polygons");
        for (Component c : layeredPane.getComponents()){
            if (c instanceof EditableCustomCountry){
                EditableCustomCountry ecc = (EditableCustomCountry) c;

                ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(path + FileNames.POLYGONS.getPath() + ecc.getName()));
                oos2.writeObject(ecc.getCountryPolygon());
                oos2.flush();
                oos2.close();
            }
        }
        System.out.println("polygons written");

    }


    public void addComponentToPane(Container pane)  {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        status = new JLabel(DrawingEnum.COUNTRIES.getText(), SwingConstants.CENTER);

        pane.add(status, BorderLayout.NORTH);
        pane.add(layeredPane, BorderLayout.CENTER);
    }

    //Saves images
    public void addNewCountry(Polygon polygon, String name){
        EditableCustomCountry cc = new EditableCustomCountry(polygon, name);

        Country country = new Country(name);
        Countries.addCountry(name, country);

        Insets insets = layeredPane.getInsets();
        cc.setBounds(insets.left, insets.top, polygon.getBounds().width + 30,  polygon.getBounds().height  + 30);
        cc.setBorder(BorderFactory.createLineBorder(Color.black));

        layeredPane.add(cc, Integer.valueOf(countryCounter));
        countryCounter++;
    }
}
