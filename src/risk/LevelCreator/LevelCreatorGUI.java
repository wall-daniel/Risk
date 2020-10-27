package risk.LevelCreator;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import javax.imageio.ImageIO;
import javax.swing.*;

public class LevelCreatorGUI extends JFrame{
    public static void main(String[] args){
        new LevelCreatorGUI();
    }

   // DrawingPad drawingPad;
    JLabel status;
    JLayeredPane layeredPane;
    int countryCounter;


    public LevelCreatorGUI(){
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
        JMenuItem setNeighbours = new JMenuItem("Set Neighbours");
        JMenuItem setContinents = new JMenuItem("Set Continents");

        addCountry.addActionListener(e -> {
            new CountryCreator(this);
            status.setText(DrawingEnum.COUNTRIES.getText());

        });

        menu.add(addCountry);
        menu.add(setNeighbours);
        menu.add(setContinents);

        bar.add(menu);

        setJMenuBar(bar);
    }


    public void addComponentToPane(Container pane)  {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setName("Layered Pane");
        pane.setName("Pane");


        status = new JLabel(DrawingEnum.COUNTRIES.getText(), SwingConstants.CENTER);

        pane.add(status, BorderLayout.NORTH);
        pane.add(layeredPane, BorderLayout.CENTER);
    }

    public void addNewCountry(BufferedImage image){
        EditableCustomContinent cc = new EditableCustomContinent(image, "Country " + countryCounter);

        Insets insets = layeredPane.getInsets();
        cc.setBounds(insets.left, insets.top, image.getWidth(), image.getHeight());
        cc.setBorder(BorderFactory.createLineBorder(Color.black));

        layeredPane.add(cc, Integer.valueOf(countryCounter));
        countryCounter++;
    }



}
