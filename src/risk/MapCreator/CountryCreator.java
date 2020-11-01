package risk.MapCreator;

import risk.Enums.CountryDrawingEnum;

import javax.swing.*;
import java.awt.*;

public class CountryCreator extends JFrame {
    public static void main(String args[]){
        new CountryCreator(null);
    }

    CountryDrawingEnum drawingStatus;
    JLabel drawingStatusLabel;
    CountryDrawPad drawingPad;

    MapEditor mapEditor;

    public CountryCreator(MapEditor mapEditor){
        this.mapEditor = mapEditor;

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
        // Create and set up the content pane.
        setTitle("Country Creator");
        addComponentToPane(getContentPane());
        addJMenuBar();


        // Display the window.
        setPreferredSize(new Dimension(800, 1000));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        pack();
        setVisible(true);
    }


    public void addComponentToPane(Container pane) {
        drawingStatus = CountryDrawingEnum.FIRST_POINT;
        drawingStatusLabel = new JLabel(drawingStatus.getDescription());

        drawingPad = new CountryDrawPad();

        pane.add(drawingStatusLabel, BorderLayout.NORTH);
        pane.add(drawingPad, BorderLayout.CENTER);
    }

    private void addJMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Options");

        JMenuItem clear = new JMenuItem("Clear");
        JMenuItem closeShape = new JMenuItem("Close Shape");
        JMenuItem newShape = new JMenuItem("New Shape");
        JMenuItem fill = new JMenuItem("Fill");
        JMenuItem finish = new JMenuItem("End");

        clear.addActionListener(e -> {
            drawingPad.clear();
            setDrawingStatus(CountryDrawingEnum.FIRST_POINT);
        });


        closeShape.addActionListener(e -> {
            drawingPad.closeShape();
            setDrawingStatus(CountryDrawingEnum.FILL);
        });


        newShape.addActionListener(e -> {
            drawingPad.reset();
            setDrawingStatus(CountryDrawingEnum.FIRST_POINT);
        });

        fill.addActionListener(e -> {
            drawingPad.fill();
            setDrawingStatus(CountryDrawingEnum.FILL);
        });


        finish.addActionListener(e -> {
            String countryName = JOptionPane.showInputDialog("Enter Country Name");
            mapEditor.addNewCountry(drawingPad.imgToPolygon(), countryName);
            this.dispose();
        });


        menu.add(clear);
        menu.add(closeShape);
        menu.add(newShape);
        menu.add(fill);
        menu.add(finish);


        bar.add(menu);

        setJMenuBar(bar);
    }

    public void setDrawingStatus(CountryDrawingEnum drawingStatus){
        this.drawingStatus = drawingStatus;
        this.drawingStatusLabel.setText(drawingStatus.getDescription());
    }



}
