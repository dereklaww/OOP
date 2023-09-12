package demolition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import processing.core.PApplet;
import processing.core.PFont;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 * Handles the mechanics of the game.
 */
public class GameMechanics {

    private PFont font;

    /**
     * HashMap which stores level and map in (level, map configuration) "key/value"
     * pairs.
     */
    private HashMap<Integer, BomberMap> mapConfigs;

    /**
     * HashMap which stores level and UI in (level, UI) "key/value" pairs.
     */
    private HashMap<Integer, UI> uiConfigs;

    /**
     * Represents the current level, initally 1.
     */
    private int currentLevel;

    /**
     * Represents the total levels of the game, specified in the configuration .json
     * file.
     */
    private int totalLevel;

    /**
     * Represents the number of levels the Player has passedm initially 0.
     */
    private int passCount;

    /**
     * Signals the app to update the map configurations and characters when the
     * Player has passed a level.
     */
    public boolean refresh;

    /**
     * Represents the Player fails to complete the game, initially false.
     */
    private boolean gameOver;

    /**
     * Represents the Player won the game, initially false.
     */
    private boolean win;

    /**
     * Number of lives the Player initally has, specified in the configuration .json
     * file.
     */
    private int lives;

    /**
     * Player object created with the specified location.
     */
    private Player player;

    /**
     * List of RedEnemy objects created with the specified location.
     */
    private List<RedEnemy> reds;

    /**
     * List of YellowEnemy objects created with the specified location.
     */
    private List<YellowEnemy> yellows;

    /**
     * Creates a new GameMechanics object.

     */
    public GameMechanics() {
        this.mapConfigs = new HashMap<Integer, BomberMap>();
        this.uiConfigs = new HashMap<Integer, UI>();
        this.currentLevel = 1;
        this.totalLevel = 0;
        this.passCount = 0;
        this.refresh = false;
        this.gameOver = false;
        this.win = false;
        this.lives = 0;
        this.player = null;
        this.reds = new ArrayList<RedEnemy>();
        this.yellows = new ArrayList<YellowEnemy>();

    }

    /**
     * Load the game configurations (map layout and initial time of each level, max
     * lives of the Player) from the given configuration .json file. Requires
     * Papplet app.
     * 
     * @param app      App of the game
     * @param pathname Directory path of cnfiguration .json file.
     */
    public void config(PApplet app, String pathname) {
        font = app.createFont("src/main/resources/PressStart2P-Regular.ttf", 20);
        app.textFont(font);

        JSONObject config = app.loadJSONObject(pathname);

        JSONArray levels = config.getJSONArray("levels");

        this.totalLevel = levels.size();

        for (int i = 0; i < levels.size(); i++) {
            JSONObject level = levels.getJSONObject(i);

            BomberMap map = new BomberMap();

            try {
                map.read(level.getString("path"));
            } catch (InvalidConfigurationException ex) {
                System.out.println(ex.getMessage());
                System.exit(1);
            }

            mapConfigs.put(i + 1, map);

            UI ui = new UI(level.getInt("time"));
            uiConfigs.put(i + 1, ui);

        }

        int lives = config.getInt("lives");
        this.lives = lives;

    }

    /**
     * Creates the in-game character objects ({@link Player}, {@link RedEnemy},
     * {@link YellowEnemy}) based on the map layout loaded in and add them into a
     * list associated with the GameMechanics object.
     */
    public void setChar() {
        this.player = new Player(getCurrentMap().getPlayerInitialLocation(), lives);

        List<Tuple> redLocation = getCurrentMap().getRedLocation();

        for (Tuple location : redLocation) {
            RedEnemy red = new RedEnemy(location);
            this.reds.add(red);
        }

        List<Tuple> yellowLocation = getCurrentMap().getYellowLocation();

        for (Tuple location : yellowLocation) {
            YellowEnemy yellow = new YellowEnemy(location);
            this.yellows.add(yellow);
        }
    }

    /**
     * Signals the app to refresh and update to new game configurations, initially
     * false.
     * 
     * @return if the game configurations need to be refreshed and updated.
     */
    public boolean isRefreshed() {
        return this.refresh;
    }

    /**
     * Returns current level. (For JUnit testing purposes only.)
     * 
     * @return current level
     */
    public int getCurrentLevel() {
        return this.currentLevel;
    }

    /**
     * Returns BomberMap object of the current level.
     * 
     * @return BomberMap object
     */
    public BomberMap getCurrentMap() {
        return mapConfigs.get(currentLevel);
    }

    /**
     * Returns the UI object of the current level.
     * 
     * @return UI object
     */
    public UI getCurrentUI() {
        return uiConfigs.get(currentLevel);
    }

    /**
     * Returns the Player object created.
     * 
     * @return Player object
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the list of RedEnemy objects created.
     * 
     * @return list of RedEnemy objects
     */
    public List<RedEnemy> getReds() {
        return this.reds;
    }

    /**
     * Returns the list of YellowEnemy objects created.
     * 
     * @return list of YellowEnemy objects
     */
    public List<YellowEnemy> getYellows() {
        return this.yellows;
    }

    /**
     * Signals game over
     */
    public void gameOver() {
        this.gameOver = true;
    }

    /**
     * Called when the Player passes a level. If all levels were passed, signals the
     * Player won.
     */
    public void pass() {
        this.currentLevel++;
        this.passCount++;
        if (passCount == totalLevel) {
            this.win = true;
        }
    }

    /**
     * Returns if the game has ended (win/lose)
     * 
     * @return if the game has ended.
     */
    public boolean isFinish() {
        return (gameOver || win);
    }

    /**
     * Draws the game end on-screen text. Requires Papplet app.
     * 
     * @param app App of the game.
     */
    public void draw(PApplet app) {
        if (gameOver) {
            app.text("GAME OVER", 145, 240);
        } else if (win) {
            app.text("YOU WIN", 160, 240);
        }
    }

    /**
     * Logic of main game mechanics, which includes checking if the Player and
     * enemies (RedEnemy and Yellowenemy objects) have crossed path, checking timer
     * and Player life count (if timer reaches zero or the Player loses all its
     * life, game ends), checking if the Player has passed a level (reaches Goal
     * location) and signals refresh and update of game configurations.
     */
    public void tick() {
        // check if Player collided with any enemies on the map
        List<Tuple> redCurrLocations = new ArrayList<Tuple>();

        for (RedEnemy red : reds) {
            if (red.isAlive())
                redCurrLocations.add(red.currLocation);
        }

        List<Tuple> yellowCurrLocations = new ArrayList<Tuple>();

        for (YellowEnemy yellow : yellows) {
            if (yellow.isAlive())
                yellowCurrLocations.add(yellow.currLocation);
        }

        if (Tuple.contains(redCurrLocations, player.currLocation)
                || Tuple.contains(yellowCurrLocations, player.currLocation)) {
            player.die();
        }

        // check time
        if (getCurrentUI().getTimer() == 0) {
            gameOver();
        }

        // check player lives
        if (player.getLifeCount() == 0) {
            gameOver();
        }

        //check player reached Goal tile
        if (player.currLocation.tupleEquals(getCurrentMap().getGoalLocation())) {
            refresh = true;
            pass();
            if (!isFinish()) {
                this.lives = player.getLifeCount();
                this.reds = new ArrayList<RedEnemy>();
                this.yellows = new ArrayList<YellowEnemy>();
                this.player = null;
                setChar();
            }
        }

    }

}