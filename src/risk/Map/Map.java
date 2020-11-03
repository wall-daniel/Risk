package risk.Map;

import risk.Enums.DrawingEnum;
import risk.Enums.MapColor;
import risk.MapCreator.CountryCreator;
import risk.Model.Continent;
import risk.Model.Continents;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Map extends JFrame {
    JLabel status;
    JLayeredPane layeredPane;

    public Map(){
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
        addComponentToPane(getContentPane());

        getContentPane().setBackground(MapColor.BACKGROUND_COLOR.getColor());
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Risk");
        pack();
        setVisible(true);
    }

    public void addComponentToPane(Container pane)  {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        status = new JLabel(DrawingEnum.COUNTRIES.getText(), SwingConstants.CENTER);

        pane.add(status, BorderLayout.NORTH);
        pane.add(layeredPane, BorderLayout.CENTER);
    }


    public void loadMap(String mapName){



    }


}
