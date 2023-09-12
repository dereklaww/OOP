package demolition;

import processing.core.PApplet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyMoveTest {

    /**
     * Test enemy movement. Enemy should move from initial position after 1 second.
     */
    @Test 
    public void basicTest() {
        // Create an instance of your application
        App app = new App();

        // Set the program to not loop automatically
        app.noLoop();

        // Set the path of the config file to use
        app.setConfig("src/test/resources/config.json");

        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] {"App"}, app);

        // Call App.setup() to load in sprites
        app.setup();

        // Set a 1 second delay to ensure all resources are loaded
        app.delay(1000);

        // Call draw to update the program.
        for (int i = 0; i < 60; i++) {
            app.draw();
        }

        for (RedEnemy red: app.reds) {
            assertFalse(red.getCurrLocation().tupleEquals(red.getInitialLocation()));
        }

        for (YellowEnemy yellow: app.yellows) {
            assertFalse(yellow.getCurrLocation().tupleEquals(yellow.getInitialLocation()));
        }
    }
}