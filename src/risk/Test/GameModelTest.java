package risk.Test;

import com.google.gson.JsonParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import risk.Model.*;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
    public void getPlaceableCountries() {
    }

    @Test
    public void getAttackableCountries() {
    }

    @Test
    public void getMoveTroopsToCountries() {
    }

    @Test
    public void getContinents() {
    }

    @Test
    public void getCountries() {
    }

    @Test
    public void getCountriesInLayerOrder() {
    }

    @Test
    public void getCountriesNames() {
    }

    @Test
    public void editCountry() {
    }

    @Test
    public void getCurrentPlayer() {
    }

    @Test
    public void placeArmies() {
    }

    @Test
    public void startGame() {
    }

    @Test
    public void nextTurn() {
    }

    @Test
    public void donePlacingArmies() {
    }

    @Test
    public void nextPhase() {
    }

    @Test
    public void startEndTurn() {
    }
}