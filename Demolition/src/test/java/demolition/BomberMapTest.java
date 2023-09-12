package demolition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BomberMapTest {

    @Test
    /**
     * Test constructor
     */
    public void constructor() {
        assertNotNull(new BomberMap());
    }

    @Test
    /**
     * Invalid map test: invalid height
     */
    public void invalidHeightTest() {
        BomberMap testMap = new BomberMap();

        try {
            testMap.read("/src/test/resources/invalidTests/invalidHeight.txt");
        } catch (InvalidConfigurationException ex) {
            String message = "Invalid Configuration File: height should be 13!";
            assertEquals(message, ex.getMessage());
        }
    }

    @Test
    /**
     * Invalid map test: Invalid width
     */
    public void invalidWidthTest() {
        BomberMap testMap = new BomberMap();

        try {
            testMap.read("src/test/resources/invalidTests/invalidWidth.txt");
        } catch (InvalidConfigurationException ex) {
            String message = "Invalid Configuration File: width should be 15!";
            assertEquals(message, ex.getMessage());
        }
    }

    @Test
    /**
     * Invalid map test: Borders are not all Wall Tiles
     */
    public void invalidBorderTest() {
        BomberMap testMap = new BomberMap();

        try {
            testMap.read("src/test/resources/invalidTests/invalidUpperBorder.txt");
        } catch (InvalidConfigurationException ex) {
            String message = "Invalid Configuration File: invalid upper or lower borders";
            assertEquals(message, ex.getMessage());
        }

        try {
            testMap.read("src/test/resources/invalidTests/invalidLowerBorder.txt");
        } catch (InvalidConfigurationException ex) {
            String message = "Invalid Configuration File: invalid upper or lower borders";
            assertEquals(message, ex.getMessage());
        }

        try {
            testMap.read("src/test/resources/invalidTests/invalidSideBorder.txt");
        } catch (InvalidConfigurationException ex) {
            String message = "Invalid Configuration File: "
                    + "invalid side borders on src/test/resources/invalidTests/invalidSideBorder.txt: row 4";
            assertEquals(message, ex.getMessage());
        }
    }

    /**
     * Invalid map test: Invalid Character input
     */
    @Test
    public void invalidMapCharTest() {
        BomberMap testMap = new BomberMap();

        try {
            testMap.read("src/test/resources/invalidTests/invalidMapChar.txt");
        } catch (InvalidConfigurationException ex) {
            String message = "Invalid Configuration File: "
                    + "invalid map character K on src/test/resources/invalidTests/invalidMapChar.txt: row 6";
            assertEquals(message, ex.getMessage());
        }
    }

    /**
     * Invalid map test: multiple goal tiles
     */
    @Test
    public void multipleGoalTest() {
        BomberMap testMap = new BomberMap();

        try {
            testMap.read("src/test/resources/invalidTests/multipleGoal.txt");
        } catch (InvalidConfigurationException ex) {
            String message = "Invalid Configuration File: multiple goals specified!";
            assertEquals(message, ex.getMessage());
        }
    }

    /**
     * Invalid map test: multiple Players
     */
    @Test
    public void multiplePlayerTest() {
        BomberMap testMap = new BomberMap();

        try {
            testMap.read("src/test/resources/invalidTests/multiplePlayer.txt");
        } catch (InvalidConfigurationException ex) {
            String message = "Invalid Configuration File: multiple players specified!";
            assertEquals(message, ex.getMessage());
        }
    }

    /**
     * Invalid map test: no Goal tile specifiec
     */
    @Test
    public void noGoalTest() {
        BomberMap testMap = new BomberMap();

        try {
            testMap.read("src/test/resources/invalidTests/noGoal.txt");
        } catch (InvalidConfigurationException ex) {
            String message = "Invalid Configuration File: No goal location specified!";
            assertEquals(message, ex.getMessage());
        }
    }

    /**
     * Invalid map test: no player specified
     */
    @Test
    public void noPlayerTest() {
        BomberMap testMap = new BomberMap();

        try {
            testMap.read("src/test/resources/invalidTests/noPlayer.txt");
        } catch (InvalidConfigurationException ex) {
            String message = "Invalid Configuration File: No player location specified!";
            assertEquals(message, ex.getMessage());
        }
    }

    /**
     * Invalid map test: Valid text file
     */
    @Test
    public void validFileTest() {
        BomberMap testMap = new BomberMap();
        try {
            testMap.read("src/test/resources/invalidTests/config.txt");
        } catch (InvalidConfigurationException ex) {
            System.out.println(ex.getMessage());
        }

        assertNotNull(testMap.getPlayerInitialLocation());
        assertNotNull(testMap.getGoalLocation());
        assertFalse(testMap.getRedLocation().isEmpty());
        assertFalse(testMap.getYellowLocation().isEmpty());
    }

}
