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
        JMenu controlMenu = new JMenu("Shape Options");

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



        JMenu drawingOptionsMenu = new JMenu("Drawing Options");

        JMenuItem setBorderDetail = new JMenuItem("Set Border Detail");
        JMenuItem setBorderDeviation = new JMenuItem("Set Border Deviation");

        setBorderDetail.addActionListener(e -> {
            int borderDetail = Integer.parseInt(JOptionPane.showInputDialog("Set Border Detail (4 (highest) - 100 (lowest))"));
            drawingPad.setBORDER_DETAIL(borderDetail);
        });

        setBorderDeviation.addActionListener(e -> {
            double borderDeviation = Double.parseDouble(JOptionPane.showInputDialog("Set Border Detail (0.0 (highest) - 1.0 (lowest))"));
            drawingPad.setBORDER_DEVIATION(borderDeviation);
        });

        drawingOptionsMenu.add(setBorderDetail);
        drawingOptionsMenu.add(setBorderDeviation);


        controlMenu.add(clear);
        controlMenu.add(closeShape);
        controlMenu.add(newShape);
        controlMenu.add(finish);

        bar.add( controlMenu);
        bar.add( drawingOptionsMenu);

        setJMenuBar(bar);
    }

    public void setDrawingStatus(CountryDrawingEnum drawingStatus){
        this.drawingStatus = drawingStatus;
        this.drawingStatusLabel.setText(drawingStatus.getDescription());
    }



}
