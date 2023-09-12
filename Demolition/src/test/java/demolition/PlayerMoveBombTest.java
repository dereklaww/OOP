package demolition;

import processing.core.PApplet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerMoveBombTest {

    /**
     * Test player movement
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
        app.draw();

        app.player.move("Down", app.currentMap);
        app.player.move("Down", app.currentMap);
        app.player.move("Right", app.currentMap);
        app.player.move("Left", app.currentMap);
        app.player.move("Up", app.currentMap);

        // Call draw again to move onto the next frame
        app.draw();

        // Verify the new state of the program with assertions
        assertTrue(app.player.getCurrLocation().tupleEquals(new Tuple(1,2)));

        app.player.move("Up", app.currentMap);
        app.player.move("Up", app.currentMap);
        app.player.move("Left", app.currentMap);

        app.draw();

        assertTrue(app.player.getCurrLocation().tupleEquals(new Tuple(1,1)));

        for(int i = 0; i < 5; i++){
            app.player.move("Right", app.currentMap);
        }
        

        app.draw();

        assertTrue(app.player.getCurrLocation().tupleEquals(new Tuple(5,1)));

        app.player.bomb();

        for (int i = 0; i < 120; i++) {
            app.draw();
        }

        assertTrue(app.player.getCurrLocation().tupleEquals(new Tuple(1,1)));
    }
    
}
