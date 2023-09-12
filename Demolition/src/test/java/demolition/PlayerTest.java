package demolition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    /**
     * Test constructor
     */
    @Test
    public void constructor() {
        assertNotNull(new Player(new Tuple(1, 1), 3));
    }

    /**
     * Test bomb method: when the method is called, number of Bomb objects in
     * Player's list increaments
     */
    @Test
    public void bombTest() {
        Player testPlayer = new Player(new Tuple(1, 1), 3);
        testPlayer.bomb();
        assertEquals(1, testPlayer.getBombs().size());
    }

    /**
     * Test getLifeCount() method
     */
    @Test
    public void getLifeCountTest() {
        Player testPlayer = new Player(new Tuple(1, 1), 3);
        assertEquals(3, testPlayer.getLifeCount());
    }

    /**
     * Test die() method. Player life count decrements
     */
    @Test
    public void dieTest() {
        Player testPlayer = new Player(new Tuple(1, 1), 3);
        testPlayer.die();
        assertEquals(2, testPlayer.getLifeCount());
    }

}
