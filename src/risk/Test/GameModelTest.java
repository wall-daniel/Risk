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
    private Country testCountry2;
    private Country testCountry3;
    private Continent testContinent1;

    @Before
    public void setUp() throws Exception {
        this.gm = new GameModel();
        testCountry1 = new Country("TestCountry", new Polygon(new int[] {0, 5, 28}, new int[] {0, 0, 56}, 3), new Point(3, 23), 1);
        testCountry2 = new Country("TestCountry2", new Polygon(new int[] {50, 5, 28}, new int[] {0, 25, 56}, 3), new Point(3, 23), 2);
        testCountry3 = new Country("TestCountry3", new Polygon(new int[] {0, 5, 68}, new int[] {100, 0, 56}, 3), new Point(3, 23), 3);
        testContinent1 = new Continent("TestContinent", 5);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getCurrentNeighboursOfCountry() {
        gm.addCountry(testCountry1);
        gm.addCountry(testCountry2);
        gm.addCountry(testCountry3);
        testCountry1.addNeighbour("TestCountry2");
        testCountry1.addNeighbour("TestCountry3");
        int[] neighbours = gm.getCurrentNeighboursOfCountry(testCountry1);
        Country c1 = gm.getCountries().get(neighbours[0]);
        Country c2 = gm.getCountries().get(neighbours[1]);
        assertTrue(testCountry1.getNeighbours().contains(c1.getName()) && testCountry1.getNeighbours().contains(c2.getName()));
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
    public void getCountriesNames() {
        gm.addCountry(testCountry1);
        ArrayList<String> a = gm.getCountriesNames();
        assertTrue(a.contains(testCountry1.getName()));
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
        Player p = gm.getCurrentPlayer();
        int initialPlayer = p.getIndex();
        int nextPlayerIndex = (initialPlayer + 1) % gm.getPlayers().size();
        // TODO: this is very different now then it was before, so this test has to be completely rethought
//        gm.nextTurn();
//        Player nextPlayer = gm.getCurrentPlayer();
//        assertTrue(nextPlayerIndex == nextPlayer.getIndex());
//        assertEquals(GameModel.GameStatus.TROOP_PLACEMENT_PHASE, gm.gameStatus);
//        assertTrue(nextPlayer.getPlaceableArmies() == Math.max(3, nextPlayer.getCountries().size() / 3) + gm.getContinentBonuses(nextPlayer));
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
        assertEquals(gs, GameModel.GameStatus.SELECT_TROOP_MOVING_FROM_PHASE);
    }


}