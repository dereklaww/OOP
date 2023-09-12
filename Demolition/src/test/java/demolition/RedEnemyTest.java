package demolition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RedEnemyTest {

    /**
     * Test constructor
     */
    @Test
    public void constructor() {
        assertNotNull(new RedEnemy(new Tuple(5, 5)));
    }

    /**
     * Test die() method.
     */
    @Test
    public void dieTest() {
        RedEnemy testRed = new RedEnemy(new Tuple(5, 5));
        testRed.die();
        assertFalse(testRed.isAlive());
    }

}