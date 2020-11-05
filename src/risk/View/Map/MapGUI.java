package risk.View.Map;

import risk.Controller.Controller;
import risk.Enums.MapColor;

import javax.swing.*;
import java.awt.*;

public class MapGUI extends JFrame {

    Map map;
    //MapInformation mapInformation;

    Controller controller;

    public MapGUI(Controller controller){
        this.controller = controller;
        this.map = new Map(controller);
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private void createAndShowGUI() {
        addComponentToPane(getContentPane());
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Game of Risk");
        pack();
        setVisible(true);
    }

    public void addComponentToPane(Container pane)  {
        pane.add(map);
    }


}
