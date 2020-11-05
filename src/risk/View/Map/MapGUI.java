package risk.View.Map;

import risk.Controller.Controller;
import risk.Enums.DrawingEnum;
import risk.Enums.MapColor;
import risk.View.Main.MainGUI;
import risk.View.MapCreator.CountryCreatorGUI;

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
        addJMenuBar();
        pane.add(map);
    }

    private void addJMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Options");

        JMenuItem quit = new JMenuItem("Quit");

        quit.addActionListener(e -> {
           controller.removeAsGameActionListener(map);
           new MainGUI();
           this.dispose();
        });

        menu.add(quit);


        bar.add(menu);

        setJMenuBar(bar);
    }


}
