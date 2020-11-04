package risk.View.Map;

import risk.Controller.Controller;
import risk.Enums.DrawingEnum;
import risk.Enums.MapColor;

import javax.swing.*;
import java.awt.*;

public class MapGUI extends JFrame {
    JLabel status;
    JLayeredPane layeredPane;

    public MapGUI(){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public MapGUI(String mapName){
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

        status = new JLabel(Controller.getGameStatus().toString(), SwingConstants.CENTER);

        pane.add(status, BorderLayout.NORTH);
        pane.add(layeredPane, BorderLayout.CENTER);
    }


    public void loadMap(String mapName){



    }


    public void saveGame(){



    }


}
