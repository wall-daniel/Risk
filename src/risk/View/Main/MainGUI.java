package risk.View.Main;

import risk.Controller.Controller;
import risk.Enums.MapColor;
import risk.Enums.PlayerType;
import risk.Enums.StringGlobals;
import risk.Model.GameModel;
import risk.View.Map.MapGUI;
import risk.View.MapCreator.MapEditorGUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainGUI extends JFrame {

    public static void main(String args[]){
        new MainGUI();
    }

    public MainGUI(){
        super("Risk");
        javax.swing.SwingUtilities.invokeLater(this::createAndShowGUI);
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

        JPanel p = new JPanel(new GridLayout(4, 1));
        p.setBorder(new EmptyBorder(100, 100, 100, 100));

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


        p.add(newGame);
        p.add(loadGame);
        p.add(createMap);
        p.add(editMap);

        pane.add(p, BorderLayout.CENTER);
    }

    /**
     * displays maps from file in rows, select one
     * enter number of players
     */
    private void loadPlayMapOptions() {
        String mapName = getFileName(StringGlobals.mapsFolder);

        JPanel playerOptionsMainPanel = new JPanel(new GridLayout(7, 1));

        //num players set up
        String[] numPlayersOptions = {"2" , "3" , "4" , "5", "6"};
        JComboBox numPlayersInput = new JComboBox(numPlayersOptions);
        playerOptionsMainPanel.add(numPlayersInput);

        //player type string set up
        String[] playerTypeStrings = new String[PlayerType.values().length+1];
        for (PlayerType playerType : PlayerType.values())
            playerTypeStrings[playerType.ordinal()] = playerType.name();
        playerTypeStrings[PlayerType.values().length] = "No Player";

        //input fields setup
        int MAX_PLAYERS = 6;
        JTextField [] playerNameFields = new JTextField[MAX_PLAYERS];
        JComboBox [] playerTypeInputs = new JComboBox[MAX_PLAYERS];

        //adding all components to main panel
        for (int i = 0; i < 6; i++){
            JPanel playerOptionsPanel = new JPanel(new GridLayout(1, 2));
            playerNameFields[i] = new JTextField("Player " + i);

            playerTypeInputs[i] = new JComboBox(playerTypeStrings);

            playerOptionsPanel.add(playerNameFields[i]);
            playerOptionsPanel.add(playerTypeInputs[i]);

            playerOptionsMainPanel.add(playerOptionsPanel);
        }

        JOptionPane.showMessageDialog(this, playerOptionsMainPanel);

        //setting up fields to pass to create game
        int numPlayers = Integer.parseInt(numPlayersInput.getSelectedItem().toString());
        String [] playerNames = new String[numPlayers];
        PlayerType[] playerTypes = new PlayerType[numPlayers];

        //adding inputs to the fields
        for (int i = 0; i < numPlayers; i++){
            playerNames[i] = playerNameFields[i].getText();
            playerTypes[i] = PlayerType.valueOf(playerTypeInputs[i].getSelectedItem().toString());
        }

        new MapGUI(mapName, numPlayers, playerNames, playerTypes);
        this.dispose();
    }


    /**
     * displays saves from file in rows, select one
     */
    private void loadSaveOptions() {
        String saveName = getFileName(StringGlobals.savesFolder);

        new MapGUI(saveName);
        this.dispose();
    }

    /**
     * displays maps from file in rows, select one
     */
    private void loadMapOptions() {
        String saveName = getFileName(StringGlobals.mapsFolder);

        new MapEditorGUI(saveName);
        this.dispose();
    }

    private String getFileName(String folder){
        File [] files = new File(folder).listFiles();
        if (files.length==0){
            JOptionPane.showMessageDialog(this,"No files");
            System.exit(0);
        }

        String [] saveNames = new String[files.length];

        for (int i = 0; i < files.length; i++)
            saveNames[i] = files[i].getName();
        String saveName = (String)JOptionPane.showInputDialog(
                this,
                "Choose map:",
                "Load Saved Game",
                JOptionPane.PLAIN_MESSAGE,
                null,
                saveNames,
                saveNames[0]);

        return saveName;
    }
}
