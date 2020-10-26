package LevelCreator;
import java.awt.Graphics;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
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

        addComponentToPane(getContentPane());
        addJMenuBar();

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


    public void addComponentToPane(Container pane) {
        pane.setLayout(null);

        status = new JLabel(DrawingEnum.WAITING.getText(), SwingConstants.CENTER);

    //        drawingPad = new DrawingPad();

        Image continentImage = new ImageIcon("treeIcon.png").getImage();
        int h = continentImage.getHeight(null);
        int w = continentImage.getWidth(null);

        CustomContinent cc = new CustomContinent(continentImage);
        cc.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("pressed");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("Entered panel");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("Exited panel");
            }

        });


        pane.add(status);
        pane.add(cc);

        Insets insets = pane.getInsets();
        Dimension size = status.getPreferredSize();
        status.setBounds(25 + insets.left, 5 + insets.top,
                size.width, size.height);


        size = cc.getPreferredSize();
        System.out.println(size);

        cc.setBounds(100 + insets.left, 100 + insets.top,
                w, h);

        //pane.add(drawingPad, BorderLayout.CENTER);
    }


}
