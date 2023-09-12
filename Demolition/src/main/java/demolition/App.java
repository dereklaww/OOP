package demolition;

import java.util.List;
import processing.core.PApplet;

/**
 * Represents the PApplet App used to run the game.
 */
public class App extends PApplet {

    /**
     * Width of Papplet app window in pixels.
     */
    public static final int WIDTH = 480;
    /**
     * Height of app window in pixels.
     */
    public static final int HEIGHT = 480;

    /**
     * Represents the number of frames the program is drawing per second.
     */
    public static final int FPS = 60;

    /**
     * GameMechanics object associated with the App object.
     */
    public GameMechanics gameMechanics;

    /**
     * BomberMap object of the current level.
     */
    public BomberMap currentMap;

    /**
     * UI object of the current level.
     */
    public UI currentUI;

    /**
     * Player object of the current level.
     */
    public Player player;

    /**
     * List of RedEnemy objects of the current level.
     */
    public List<RedEnemy> reds;

    /**
     * List of YellowEne,y objects of the current level.
     */
    public List<YellowEnemy> yellows;

    /**
     * Represents the path of the configuration file.
     */
    private String configPath;

    /**
     * Signals key is released
     */
    private boolean keyIsReleased = true;

    /**
     * Creates new App object.
     */
    public App() {
        this.gameMechanics = new GameMechanics();
        this.configPath = "config.json";

    }

    /**
     * Settings of the map window size, given the height and width in pixels.
     */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setConfig(String path) {
        this.configPath = path;
    }

    /**
     * Inital setup for all classes.
     */
    public void setup() {
        frameRate(FPS);
        BomberMap.setup(this);
        Player.setup(this);
        RedEnemy.setup(this);
        YellowEnemy.setup(this);
        Bomb.setup(this);
        UI.setup(this);
        gameMechanics.config(this, configPath);
        gameMechanics.setChar();
        currentMap = gameMechanics.getCurrentMap();
        currentUI = gameMechanics.getCurrentUI();
        player = gameMechanics.getPlayer();
        reds = gameMechanics.getReds();
        yellows = gameMechanics.getYellows();

    }

    /**
     * Logic control of each class, refresh game configurations when signalled.
     */
    public void tick() {
        gameMechanics.tick();

        if (gameMechanics.refresh) {
            currentMap = gameMechanics.getCurrentMap();
            currentUI = gameMechanics.getCurrentUI();
            player = gameMechanics.getPlayer();
            reds = gameMechanics.getReds();
            yellows = gameMechanics.getYellows();
            gameMechanics.refresh = false;
        }
        for (RedEnemy red : reds) {
            red.tick(currentMap);
        }
        for (YellowEnemy yellow : yellows) {
            yellow.tick(currentMap);
        }

    }

    /**
     * Draw control of each class, draws game end on-screen text when signalled game
     * end.
     */
    public void draw() {
        background(255, 165, 0);
        if (!gameMechanics.isFinish()) {
            this.currentUI.draw(this, this.player);
            this.currentMap.draw(this);
            this.player.draw(this);
            for (RedEnemy red : reds) {
                red.draw(this);
            }
            for (YellowEnemy yellow : yellows) {
                yellow.draw(this);
            }
            if (this.player.getBombs().size() > 0) {
                for (Bomb bomb : this.player.getBombs()) {
                    bomb.draw(this);
                    bomb.tick(currentMap, player, reds, yellows);
                }
            }
            this.tick();

        } else {
            gameMechanics.draw(this);
        }
    }

    /**
     * Arrow keys and Spacebar configurations for Player movement and Bomb
     * placement.
     */
    public void keyPressed() {
        if (!gameMechanics.isFinish()) {
            if (keyIsReleased) {
                if (keyCode == 32) {
                    this.player.bomb();
                    keyIsReleased = false;
                } else if (keyCode == UP) {
                    this.player.move("Up", currentMap);
                    keyIsReleased = false;
                } else if (keyCode == DOWN) {
                    this.player.move("Down", currentMap);
                    keyIsReleased = false;
                } else if (keyCode == LEFT) {
                    this.player.move("Left", currentMap);
                    keyIsReleased = false;
                } else if (keyCode == RIGHT) {
                    this.player.move("Right", currentMap);
                    keyIsReleased = false;
                }
            }
        }
    }

    /**
     * Signal key is released to allow next key press command.
     */
    public void keyReleased() {
        keyIsReleased = true;
    }

    /**
     * Runs the program.
     * 
     * @param args Command line arguments (can be ignored)
     */
    public static void main(String[] args) {

        PApplet.main("demolition.App");

    }
}
