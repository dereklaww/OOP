package demolition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TileTest {

    /**
     * Test constructor
     */
    @Test
    public void constructor() {
        assertNotNull(new Tile("testTile", null));
    }

    /**
     * Test getTileType() method.
     */
    @Test
    public void getTileTypeTest() {
        Tile testTile = new Tile("testTile", null);
        assertEquals("testTile", testTile.getType());
    }
}