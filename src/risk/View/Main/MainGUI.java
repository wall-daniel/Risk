package risk.View.Main;

import risk.Enums.DrawingEnum;
import risk.Enums.MapColor;
import risk.View.MapCreator.MapEditorGUI;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {

    public static void main(String args[]){
        new MainGUI();
    }

    /*
     - new game
        - select each player (upto 6) to be ai/random player or human
        - select map
     - load game
        - select save

     - create new map
     - load map editor
        - select map
     */

    public MainGUI(){
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
        JButton newGame = new JButton("New Game");
        JButton loadGame = new JButton("Load Game");
        JButton createMap = new JButton("Create New Map");
        JButton editMap = new JButton("Edit Map");

        newGame.addActionListener(e -> {
            loadPlayMapOptions();
        });

        loadGame.addActionListener(e -> {
            loadSaveOptions();
        });

        createMap.addActionListener(e -> {
            new MapEditorGUI();
            this.setVisible(false);
        });

        editMap.addActionListener(e -> {
            loadMapOptions();
        });

        pane.add(newGame);
        pane.add(loadGame);
        pane.add(createMap);
        pane.add(editMap);
    }

    /**
     * displays maps from file in rows, select one
     * enter number of players
     */
    private void loadPlayMapOptions() {




    }

    /**
     * displays saves from file in rows, select one
     */
    private void loadSaveOptions() {




    }

    /**
     * displays maps from file in rows, select one
     */
    private void loadMapOptions() {





    }
}
