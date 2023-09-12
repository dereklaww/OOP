package demolition;

import processing.core.PApplet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameCompleteTest {

    /**
     * Test level complete, moving on to next level.
     */
    @Test
    public void levelCompleteTest() {
        // Create an instance of your application
        App app = new App();

        // Set the program to not loop automatically
        app.noLoop();

        // Set the path of the config file to use
        app.setConfig("src/test/resources/levelPassConfig.json");

        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);

        // Call App.setup() to load in sprites
        app.setup();

        // Set a 1 second delay to ensure all resources are loaded
        app.delay(1000);

        // Call draw to update the program.
        app.draw();

        app.player.move("Down", app.currentMap);

        app.draw();

        assertEquals(app.gameMechanics.getCurrentLevel(), 2);
        assertTrue(app.player.getCurrLocation().tupleEquals(new Tuple(1, 6)));
    }

    /**
     * Test game complete
     */
    @Test
    public void winTest() {
        // Create an instance of your application
        App app = new App();

        // Set the program to not loop automatically
        app.noLoop();

        // Set the path of the config file to use
        app.setConfig("src/test/resources/winTest.json");

        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);

        // Call App.setup() to load in sprites
        app.setup();

        // Set a 1 second delay to ensure all resources are loaded
        app.delay(1500);

        // Call draw to update the program.
        app.draw();

        app.player.move("Down", app.currentMap);

        for (int i = 0; i < 3; i++)
            app.draw();

        assertTrue(app.gameMechanics.isFinish());
    }

    /**
     * Test game over due to death by enemy.
     */
    @Test
    public void deathByEnemyTest() {
        // Create an instance of your application
        App app = new App();

        // Set the program to not loop automatically
        app.noLoop();

        // Set the path of the config file to use
        app.setConfig("src/test/resources/deathByEnemy.json");

        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);

        // Call App.setup() to load in sprites
        app.setup();

        // Set a 1 second delay to ensure all resources are loaded
        app.delay(1500);

        // Call draw to update the program.
        app.draw();

        app.player.move("Up", app.currentMap);

        app.draw();

        assertTrue(app.gameMechanics.isFinish());
    }

    /**
     * Test game over due to death by bomb explosion
     */
    @Test
    public void deathByBombTest() {
        // Create an instance of your application
        App app = new App();

        // Set the program to not loop automatically
        app.noLoop();

        // Set the path of the config file to use
        app.setConfig("src/test/resources/deathByBomb.json");

        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);

        // Call App.setup() to load in sprites
        app.setup();

        // Set a 1 second delay to ensure all resources are loaded
        app.delay(2000);

        // Call draw to update the program.
        app.draw();

        app.player.bomb();

        for (int i = 0; i < 180; i++)
            app.draw();

        assertTrue(app.gameMechanics.isFinish());
    }

    /**
     * Test game over due to timer reaches zero.
     */
    @Test
    public void gameOverTimer() {
        // Create an instance of your application
        App app = new App();

        // Set the program to not loop automatically
        app.noLoop();

        // Set the path of the config file to use
        app.setConfig("src/test/resources/timerTest.json");

        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);

        // Call App.setup() to load in sprites
        app.setup();

        // Set a 1 second delay to ensure all resources are loaded
        app.delay(2000);

        // Call draw to update the program.

        for (int i = 0; i < 60; i++)
            app.draw();

        assertEquals(app.currentUI.getTimer(), 9);

        for (int i = 0; i < 540; i++)
            app.draw();

        assertTrue(app.gameMechanics.isFinish());
    }

}