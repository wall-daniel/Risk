package risk.Test;

import com.google.gson.JsonParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import risk.Model.*;
import risk.Players.Player;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameModelTest {

    private GameModel gm;
    private Country testCountry1;
    private Continent testContinent1;

    @Before
    public void setUp() throws Exception {
        this.gm = new GameModel(4);
        testCountry1 = new Country("TestCountry", new Polygon(new int[] {0, 5, 28}, new int[] {0, 0, 56}, 3), new Point(3, 23), 1);
        testContinent1 = new Continent("TestContinent", 5);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getCurrentNeighboursOfCountry() {
    }

    @Test
    public void updateGame() {
    }

    @Test
    public void addCountry() {
        gm.addCountry(testCountry1);
        assertTrue(gm.getCountries().contains(testCountry1));
    }

    @Test
    public void getCountry() {
        gm.addCountry(testCountry1);
        Country temp = gm.getCountry("TestCountry");
        assertEquals(temp, testCountry1);
    }

    @Test
    public void addContinent() {
        gm.addContinent(testContinent1);
        Continent temp = gm.getContinent("TestContinent");
        assertEquals(temp, testContinent1);
    }

    @Test
    public void saveMap() throws IOException {
        //TODO currently only checks that the file has been created
        gm.saveMap("testtest.txt");
        File file = new File("testtest.txt");
        assertNotNull(file);
        file.delete();

    }

    @Test
    public void getMoveTroopsToCountries() {
    }

    @Test
    public void getCountriesInLayerOrder() {
    }

    @Test
    public void getCountriesNames() {
        gm.addCountry(testCountry1);
        ArrayList<String> a = gm.getCountriesNames();
        assertTrue(a.contains(testCountry1.getName()));
    }

    @Test
    public void editCountry() {
        ArrayList<String> neighbours = new ArrayList<String>();
        neighbours.add("Brasil");
        neighbours.add("North Africa");
        gm.editCountry(testCountry1, neighbours, testContinent1);
        assertEquals(testCountry1.getNeighbours(), neighbours);
        assertEquals(testCountry1.getContinent(), testContinent1);
    }

    @Test
    public void placeArmies() {
        Player p = gm.getCurrentPlayer();
        p.startTurn(10);
        gm.placeArmies(testCountry1, 5);
        assertEquals(6,testCountry1.getArmies());

    }

    @Test
    public void startGame() {
        gm.startGame();
        assertEquals(0, gm.getCurrentPlayer().getIndex());
        assertEquals(GameModel.GameStatus.TROOP_PLACEMENT_PHASE, gm.gameStatus);
        assertTrue(gm.getCurrentPlayer().getPlaceableArmies() > 0);
    }

    @Test
    public void nextTurn() {
        //TODO add a case where a player has lost and their turn is skipped
        Player p = gm.getCurrentPlayer();
        int initialPlayer = p.getIndex();
        int nextPlayerIndex = (initialPlayer + 1) % gm.getPlayers().size();
        gm.nextTurn();
        Player nextPlayer = gm.getCurrentPlayer();
        assertTrue(nextPlayerIndex == nextPlayer.getIndex());
        assertEquals(GameModel.GameStatus.TROOP_PLACEMENT_PHASE, gm.gameStatus);
        assertTrue(nextPlayer.getPlaceableArmies() == Math.max(3, nextPlayer.getCountries().size() / 3) + gm.getContinentBonuses(nextPlayer));
    }

    @Test
    public void donePlacingArmies() {
        Player p = gm.getCurrentPlayer();
        p.startTurn(10);
        System.out.println(p.getPlaceableArmies());
        assertFalse(gm.donePlacingArmies());
        gm.placeArmies(testCountry1, 13);
        assertTrue(gm.donePlacingArmies());
    }

    @Test
    public void nextPhase() {
        GameModel.GameStatus gs = gm.gameStatus;
        assertEquals(gs, GameModel.GameStatus.TROOP_PLACEMENT_PHASE);
        gm.nextPhase();
        gs = gm.gameStatus;
        assertEquals(gs, GameModel.GameStatus.SELECT_ATTACKING_PHASE);
        gm.nextPhase();
        gs = gm.gameStatus;
        assertEquals(gs, GameModel.GameStatus.SELECT_DEFENDING_PHASE);
        //TODO
    }

    @Test
    public void startEndTurn() {
    }
}