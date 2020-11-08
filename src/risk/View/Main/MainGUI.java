package risk.View.Main;

import risk.Controller.Controller;
import risk.Enums.MapColor;
import risk.Model.GameModel;
import risk.View.Map.MapGUI;
import risk.View.MapCreator.MapEditorGUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainGUI extends JFrame {

    public static void main(String args[]){


        new MainGUI();
    }





    public MainGUI(){
        super("Risk");



        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private void createAndShowGUI() {
        addComponentToPane(getContentPane());
        getContentPane().setBackground(MapColor.BACKGROUND_COLOR.getColor());
//        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setTitle("Risk");
        pack();
        setVisible(true);
    }

    public void addComponentToPane(Container pane)  {

        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBorder(new EmptyBorder(100, 100, 100, 100));

        JButton newGame = new JButton("New Game");
       // JButton loadGame = new JButton("Load Game");
        JButton createMap = new JButton("Create New Map");
       // JButton editMap = new JButton("Edit Map");

        newGame.addActionListener(e -> {
            loadPlayMapOptions();
        });

        /*
        loadGame.addActionListener(e -> {
            loadSaveOptions();
        });
*/
        createMap.addActionListener(e -> {
            new MapEditorGUI();
            this.setVisible(false);
        });

        /*
        editMap.addActionListener(e -> {
            loadMapOptions();
        });


         */

        p.add(newGame);
        //p.add(loadGame);
        p.add(createMap);
        //p.add(editMap);

        pane.add(p, BorderLayout.CENTER);
    }

    /**
     * displays maps from file in rows, select one
     * enter number of players
     */
    private void loadPlayMapOptions() {
        String mapNamesOptions [] = {"Earth", "Italy", "Chicken"};
        //TODO have the controller getMapNames
        //String mapNamesOptions [] = controller.getMapNames();

        /*
        String mapName = (String)JOptionPane.showInputDialog(
                this,
                "Choose map: (Just placeholder)",
                "New Game",
                JOptionPane.PLAIN_MESSAGE,
                null,
                mapNamesOptions,
                "Earth");
        */

        String numPlayersOptions[] = {"2" , "3" , "4" , "5", "6"};
        int numPlayers = Integer.parseInt(
                (String)JOptionPane.showInputDialog(
                        this,
                        "Number of Players:",
                        "New Game",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        numPlayersOptions,
                        "3")
        );



        MapGUI mapGUI = new MapGUI(numPlayers);
        //TODO the controller setup the man given the map name and the number of players
        //controller.setUpModel(mapName, numPlayers);
        //mapGUI.loadGame(); //loads the gameModel into the map
        this.dispose();
    }

    /**
     * displays saves from file in rows, select one
     */
    /*
    private void loadSaveOptions() {
        String gameNamesOptions [] = {"EarthGame1", "ItalyGame2", "ChickenGame3"};
        //TODO have the controller get names of all saved games
        //String gameNamesOptions [] = controller.getSavedGamesNames();

        String gameName = (String)JOptionPane.showInputDialog(
                this,
                "Choose map:",
                "New Game",
                JOptionPane.PLAIN_MESSAGE,
                null,
                gameNamesOptions,
                "EarthGame1");

        MapGUI mapGUI = new MapGUI(6);
        //TODO the controller setup the man given the map name and the number of players
        //controller.setUpModel(gameName);
        //map.loadGame(); //loads the gameModel into the map
        this.dispose();
    }

     */

    /**
     * displays maps from file in rows, select one
     */
    /*
    private void loadMapOptions() {
        String mapNamesOptions [] = {"Earth", "Italy", "Chicken"};
        //TODO have the controller getMapNames
        //String mapNamesOptions [] = controller.getMapNames();

        String mapName = (String)JOptionPane.showInputDialog(
                this,
                "Choose map:",
                "New Game",
                JOptionPane.PLAIN_MESSAGE,
                null,
                mapNamesOptions,
                "Earth");

        MapEditorGUI mapEditorGUI = new MapEditorGUI();
        //TODO the controller setup the man given the map name and the number of players
        //controller.setUpModel(mapName);
        //mapEditor.loadGame(); //loads the gameModel into the map
        this.dispose();
    }

     */
}
