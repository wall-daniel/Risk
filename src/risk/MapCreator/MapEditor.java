package risk.MapCreator;

import risk.Enums.DrawingEnum;
import risk.Enums.MapColor;
import risk.Model.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MapEditor extends JFrame{
    public static void main(String[] args){
        new MapEditor();
    }

    JLabel status;
    JLayeredPane layeredPane;
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
        new File(path).mkdirs();
        new File(path + "countryImages\\").mkdirs();
        new File(path + "location\\").mkdirs();
        addComponentToPane(getContentPane());

        addJMenuBar();

        getContentPane().setBackground(MapColor.BACKGROUND_COLOR.getColor());
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Custom Level Creator");
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
            Continent continent = new Continent(continentName);
            Continents.addContinent(continentName, continent);
        });

        loadMap.addActionListener(e -> {
            loadMap();
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

    private void loadMap(){


    }

    /*
    Saves:
    named images - saved in addNewCountry()
    countries/continents serialized - done
    names and coordinates
     */

    private void saveMap() throws IOException {
        ContinentsSerializable continentsSerializable = new ContinentsSerializable(Continents.getContinents());
        CountriesSerializable countriesSerializable = new CountriesSerializable(Countries.getCountries());

        System.out.println("attempting to write locations of all components.");
        for (Component c : layeredPane.getComponents()){
            System.out.println("COMPONENT: " + c.getName());
            ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(path + "location\\" + c.getName()));
            oos2.writeObject(c.getLocationOnScreen());
            oos2.flush();
            oos2.close();
        }
        System.out.println("components written");

        System.out.println("attempting to write images");
        for (Component c : layeredPane.getComponents()){
            if (c instanceof EditableCustomCountry){
                EditableCustomCountry l = (EditableCustomCountry) c;
                BufferedImage bi = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
                bi.createGraphics();

                String name = l.getName();

                try {
                    ImageIO.write(bi, "png", new File(path + "countryImages\\" + name));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        System.out.println("images written");

        System.out.println("attempting to write continents");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path + "continents"));
        oos.writeObject(continentsSerializable);
        oos.flush();
        oos.close();
        System.out.println("continents written");

        System.out.println("attempting to write countries.");
        ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(path + "countries"));
        oos1.writeObject(countriesSerializable);
        oos1.flush();
        oos1.close();
        System.out.println("countries written");


    }


    public void addComponentToPane(Container pane)  {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        status = new JLabel(DrawingEnum.COUNTRIES.getText(), SwingConstants.CENTER);

        pane.add(status, BorderLayout.NORTH);
        pane.add(layeredPane, BorderLayout.CENTER);
    }

    //Saves images
    public void addNewCountry(BufferedImage image, String name){
        EditableCustomCountry cc = new EditableCustomCountry(image, name);

        Country country = new Country(name);
        Countries.addCountry(name, country);

        Insets insets = layeredPane.getInsets();
        cc.setBounds(insets.left, insets.top, image.getWidth(), image.getHeight());
        cc.setBorder(BorderFactory.createLineBorder(Color.black));

        layeredPane.add(cc, Integer.valueOf(countryCounter));
        countryCounter++;
    }
}
