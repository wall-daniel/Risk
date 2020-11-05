package risk.View.MapCreator;

import risk.Enums.CountryDrawingEnum;

import javax.swing.*;
import java.awt.*;

public class CountryCreatorGUI extends JFrame {

    CountryDrawingEnum drawingStatus;
    JLabel drawingStatusLabel;
    CountryDrawPad drawingPad;

    MapEditorGUI mapEditorGUI;

    //TODO add menu items to set border_detail and border_deviation of countrydrawpad

    public CountryCreatorGUI(MapEditorGUI mapEditorGUI){
        this.mapEditorGUI = mapEditorGUI;

        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
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
        JMenuItem finish = new JMenuItem("Finish");

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


        finish.addActionListener(e -> {
            String countryName = JOptionPane.showInputDialog("Enter Country Name");
            mapEditorGUI.addNewCountry(countryName, drawingPad.getPolygon());
            this.dispose();
        });


        menu.add(clear);
        menu.add(closeShape);
        menu.add(newShape);
        menu.add(finish);

        bar.add(menu);

        setJMenuBar(bar);
    }

    public void setDrawingStatus(CountryDrawingEnum drawingStatus){
        this.drawingStatus = drawingStatus;
        this.drawingStatusLabel.setText(drawingStatus.getDescription());
    }



}
