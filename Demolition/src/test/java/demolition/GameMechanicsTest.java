package demolition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameMechanicsTest {

    /**
     * Test constructor
     */
    @Test
    public void constructor() {
        assertNotNull(new GameMechanics());
    }

    /**
     * Test refresh flag
     */
    @Test
    public void isRefreshedTest() {
        GameMechanics testGameMechanics = new GameMechanics();
        assertFalse(testGameMechanics.isRefreshed());
    }

    /**
     * Test game over flag
     */
    @Test
    public void gameOverTest() {
        GameMechanics testGameMechanics = new GameMechanics();
        testGameMechanics.gameOver();
        assertTrue(testGameMechanics.isFinish());
    }
}
