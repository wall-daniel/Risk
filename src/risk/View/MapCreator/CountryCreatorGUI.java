package risk.View.MapCreator;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class CountryCreatorGUI extends JFrame {

    JPanel drawingButtons;
    CountryDrawPad drawingPad;

    JButton closeShape;
    JButton finish;

    MapEditorGUI mapEditorGUI;

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
        drawingButtons = new JPanel(new FlowLayout());
        JButton clear = new JButton("Clear");
        clear.addActionListener(e -> {
            closeShape.setEnabled(false);
            finish.setEnabled(false);
            drawingPad.clear();
        });

        closeShape = new JButton("Close Shape");
        closeShape.setEnabled(false);
        closeShape.addActionListener(e -> {
            finish.setEnabled(true);
            drawingPad.closeShape();
        });

        finish = new JButton("Finish");
        finish.setEnabled(false);
        finish.addActionListener(e -> {
            String countryName = "";
            do {
                countryName = JOptionPane.showInputDialog("Enter Country Name");
            } while (countryName.equals(""));
            mapEditorGUI.addNewCountry(countryName, drawingPad.getPolygon());
            this.dispose();
        });

        JPanel borderDetailPanel = new JPanel(new GridLayout(2, 1));
        JLabel borderDetailLabel = new JLabel("Border Detail", JLabel.CENTER);
        JSlider borderDetailSlider = new JSlider(JSlider.HORIZONTAL, 2, 100, 100);
        Hashtable labelTable = new Hashtable();
        labelTable.put(2, new JLabel("Max") );
        labelTable.put(100, new JLabel("Min") );
        borderDetailSlider.setLabelTable( labelTable );
        borderDetailSlider.setPaintLabels(true);
        borderDetailSlider.addChangeListener(e-> drawingPad.setBorderDetail(borderDetailSlider.getValue()));
        borderDetailPanel.add(borderDetailLabel);
        borderDetailPanel.add(borderDetailSlider);

        drawingButtons.add(clear);
        drawingButtons.add(closeShape);
        drawingButtons.add(finish);

        drawingButtons.add(borderDetailPanel);

        drawingPad = new CountryDrawPad(this);

        pane.add(drawingButtons, BorderLayout.NORTH);
        pane.add(drawingPad, BorderLayout.CENTER);
    }

    public void setCloseShapeEnabled(boolean enabled){
        closeShape.setEnabled(enabled);
    }

    public void setFinishEnabled(boolean enabled){
        finish.setEnabled(enabled);
    }

    private void addJMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu controlMenu = new JMenu("Options");
        JMenuItem cancel = new JMenuItem("Cancel");

        cancel.addActionListener(e -> {
            this.dispose();
        });

        controlMenu.add(cancel);

        bar.add( controlMenu);

        setJMenuBar(bar);
    }

}
