package demolition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UITest {

    /**
     * Test constuctor
     */
    @Test
    public void contructor() {
        assertNotNull(new UI(180));
    }

    /**
     * Test UI decrements after 1 second.
     */
    @Test
    public void timerTickTest() {
        
        UI testUI = new UI(180);
        for (int i = 0; i <= 60; i++) {
            testUI.timerTick();
        }

        assertEquals(179, testUI.getTimer());
    }
}