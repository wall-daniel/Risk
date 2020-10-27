package risk.LevelCreator;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

public class LevelCreatorGUI extends JFrame{
    public static void main(String[] args){


        new LevelCreatorGUI();
    }

    DrawingPad drawingPad;
    JLabel status;

    public LevelCreatorGUI(){
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Create and set up the content pane.
        setTitle("Custom Level Creator");
        try{

           // addComponentToPane(getContentPane());
            drawingPad = new DrawingPad();
            getContentPane().add(drawingPad);
        } catch (Exception e){

        }

       // addJMenuBar();


        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        // Display the window.
        pack();
        setVisible(true);
    }

    private void addJMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Add");
        JMenuItem addContinent = new JMenuItem("Add Continent");

        addContinent.addActionListener(e -> {
            drawingPad.setDrawingStatus(DrawingEnum.CONTINENTS);
            status.setText(DrawingEnum.CONTINENTS.getText());
        });

        JMenuItem addCountry = new JMenuItem("Add Country");
        addCountry.addActionListener(e -> {
            drawingPad.setDrawingStatus(DrawingEnum.COUNTRIES);
            status.setText(DrawingEnum.COUNTRIES.getText());
        });

        menu.add(addContinent);
        menu.add(addCountry);

        bar.add(menu);

        setJMenuBar(bar);
    }


    public void addComponentToPane(Container pane) throws IOException {

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setName("Layered Pane");

        pane.setName("Pane");

       // pane.setBackground(new Color(21, 80, 255));
        //status = new JLabel(DrawingEnum.WAITING.getText(), SwingConstants.CENTER);

        File img = new File("treeIcon.png");
        BufferedImage continentImage = ImageIO.read(img);

        int h = continentImage.getHeight(null);
        int w = continentImage.getWidth(null);

        CustomContinent cc = new CustomContinent(continentImage, "tree 1");
        CustomContinent cc1 = new CustomContinent(continentImage, "tree 2");

        layeredPane.add(cc, Integer.valueOf(2));
        layeredPane.add(cc1, Integer.valueOf(3));

        Insets insets = layeredPane.getInsets();
        Dimension size = cc.getPreferredSize();
        cc.setBounds(100 + insets.left, 100 + insets.top, w, h);
        cc.setBorder(BorderFactory.createLineBorder(Color.black));


        cc1.setBounds(200+ insets.left, 320 + insets.top, w, h);
        cc1.setBorder(BorderFactory.createLineBorder(Color.black));

        pane.add(layeredPane);
    }


}
