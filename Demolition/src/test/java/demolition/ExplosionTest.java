package demolition;

import org.junit.jupiter.api.Test;

import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.*;

public class ExplosionTest {
    
    /**
     * Test bomb method with surrounding Broken tiles.
     */
    @Test 
    public void brokenTest() {
        // Create an instance of your application
        App app = new App();

        // Set the program to not loop automatically
        app.noLoop();

        // Set the path of the config file to use
        app.setConfig("src/test/resources/explosionTestAllBroken.json");

        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] {"App"}, app);

        // Call App.setup() to load in sprites
        app.setup();

        // Set a 1 second delay to ensure all resources are loaded
        app.delay(1000);

        app.draw();

        app.player.bomb();

        for (int i = 0; i < 300; i++)
            app.draw();
    }

    /**
     * Test bomb method with surrounding Empty tiles.
     */
    @Test 
    public void emptyTest() {
        // Create an instance of your application
        App app = new App();

        // Set the program to not loop automatically
        app.noLoop();

        // Set the path of the config file to use
        app.setConfig("src/test/resources/explosionTestAllEmpty.json");

        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] {"App"}, app);

        // Call App.setup() to load in sprites
        app.setup();

        // Set a 1 second delay to ensure all resources are loaded
        app.delay(1000);

        app.draw();

        app.player.bomb();

        for (int i = 0; i < 200; i++)
            app.draw();
    }

    /**
     * Test bomb method with surrounding Wall tiles.
     */
    @Test 
    public void wallTest() {
        // Create an instance of your application
        App app = new App();

        // Set the program to not loop automatically
        app.noLoop();

        // Set the path of the config file to use
        app.setConfig("src/test/resources/explosionTestAllWall.json");

        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] {"App"}, app);

        // Call App.setup() to load in sprites
        app.setup();

        // Set a 1 second delay to ensure all resources are loaded
        app.delay(1000);

        app.draw();

        app.player.bomb();

        for (int i = 0; i < 200; i++)
            app.draw();
    }
}
