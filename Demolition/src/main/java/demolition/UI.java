package demolition;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

/**
 * Represents the user interface (UI) of the game, shown on the top border of
 * the screen.
 */
public class UI {

    private static final Tuple PLAYER_LIFE_ICON_POS = new Tuple(128, 21);
    private static final Tuple PLAYER_LIFE_TEXT_POS = new Tuple(173, 48);
    private static final Tuple TIMER_ICON_POS = new Tuple(256, 21);
    private static final Tuple TIMER_TEXT_POS = new Tuple(301, 48);

    private static PImage playerLife;
    private static PImage clock;
    private static PFont font;

    /**
     * Represents remainting time on the current level.
     */
    private int timer;

    /**
     * Represents the number of times the logic is called, initially 0.
     */
    private int timerFrameCount;

    /**
     * Creates a new UI object. Requires the initial time of the level.
     * 
     * @param time
     */
    public UI(int time) {
        this.timer = time;
        this.timerFrameCount = 0;
    }

    /**
     * Loads in all the sprites and font required to generate the UI object on the
     * app. Requires Papplet object.
     * 
     * @param app App of the game.
     */
    public static void setup(PApplet app) {
        playerLife = app.loadImage("src/main/resources/icons/player.png");
        clock = app.loadImage("src/main/resources/icons/clock.png");
        font = app.createFont("src/main/resources/PressStart2P-Regular.ttf", 20);
        app.textFont(font);
    }

    /**
     * Returns remaining time on the current level.
     * 
     * @return Time left
     */
    public int getTimer() {
        return this.timer;
    }

    /**
     * Logic to control time decrement.
     */
    public void timerTick() {
        this.timerFrameCount++;
        if (timerFrameCount == 60) {
            timer--;
            timerFrameCount = 0;
        }
    }

    /**
     * Draws the UI on the app window. Requires Papplet object.
     * 
     * @param app    App of the game
     * @param player Player
     */
    public void draw(PApplet app, Player player) {
        app.image(playerLife, PLAYER_LIFE_ICON_POS.x, PLAYER_LIFE_ICON_POS.y);
        app.text(player.getLifeCount(), PLAYER_LIFE_TEXT_POS.x, PLAYER_LIFE_TEXT_POS.y);
        app.image(clock, TIMER_ICON_POS.x, TIMER_ICON_POS.y);
        app.text(timer, TIMER_TEXT_POS.x, TIMER_TEXT_POS.y);
        app.fill(0, 0, 0);
        timerTick();
    }
}
