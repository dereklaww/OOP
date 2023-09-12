package demolition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class YellowEnemyTest {

    /**
     * Test constructor
     */
    @Test
    public void constructor() {
        assertNotNull(new YellowEnemy(new Tuple(5, 5))); 
    }

    /**
     * Test die() method.
     */
    @Test
    public void dieTest() {
        YellowEnemy testYellow = new YellowEnemy(new Tuple(5, 5));
        testYellow.die();
        assertFalse(testYellow.isAlive());
    }

}