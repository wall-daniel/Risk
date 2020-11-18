package risk.View.Map;

import risk.Controller.PlayerController;
import risk.Enums.PlayerType;
import risk.Model.GameModel;
import risk.View.Main.MainGUI;

import javax.swing.*;
import java.awt.*;

public class MapGUI extends JFrame {

    Map map;
    MapInformation mapInformation;

    PlayerController controller;

    public MapGUI(int numPlayers, String[] playerNames, PlayerType[] playerTypes){
        try {
            GameModel gameModel = new GameModel(numPlayers, playerNames, playerTypes);
            this.controller = new PlayerController(gameModel, this);
            this.map = new Map(controller);
            this.mapInformation = new MapInformation(controller);
            gameModel.addActionListener(map);
            gameModel.addActionListener(mapInformation);

//            javax.swing.SwingUtilities.invokeLater(this::createAndShowGUI);
            createAndShowGUI();
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

        controller.startGame();
    }

    public void addComponentToPane(Container pane) {
        addJMenuBar();
        pane.add(map, BorderLayout.CENTER);
        pane.add(mapInformation, BorderLayout.SOUTH);
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
