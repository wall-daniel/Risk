package risk.View.Map;

import risk.Controller.Controller;
import risk.Model.GameModel;
import risk.View.Main.MainGUI;

import javax.swing.*;
import java.awt.*;

public class MapGUI extends JFrame {

    Map map;
    //MapInformation mapInformation;

    Controller controller;

    public MapGUI(int numPlayers){
        try {
            GameModel gameModel = new GameModel(numPlayers);
            this.controller = new Controller(gameModel, this);
            this.map = new Map(controller);
            gameModel.addActionListener(map);

            javax.swing.SwingUtilities.invokeLater(this::createAndShowGUI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAndShowGUI() {
        addComponentToPane(getContentPane());

        // Set maximum screen size. I was having trouble with 2 monitors having it display correctly.
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        if (dimension.width > 1920) {
            dimension.width = 1920;
        }
        if (dimension.height > 1200) {
            dimension.height = 1200;
        }
        setPreferredSize(dimension);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Game of Risk");

        pack();
        setVisible(true);

        controller.updateGame();
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
           new MainGUI();
           this.dispose();
        });

        menu.add(quit);


        bar.add(menu);

        setJMenuBar(bar);
    }


}
