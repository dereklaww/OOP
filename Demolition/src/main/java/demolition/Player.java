package demolition;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represents the character controlled by the player, BombMan
 */
public class Player extends BomberChar {

    private static PImage playerDown_1;
    private static PImage playerDown_2;
    private static PImage playerDown_3;
    private static PImage playerDown_4;
    private static PImage playerUp_1;
    private static PImage playerUp_2;
    private static PImage playerUp_3;
    private static PImage playerUp_4;
    private static PImage playerLeft_1;
    private static PImage playerLeft_2;
    private static PImage playerLeft_3;
    private static PImage playerLeft_4;
    private static PImage playerRight_1;
    private static PImage playerRight_2;
    private static PImage playerRight_3;
    private static PImage playerRight_4;

    private static PImage[] playerDown;
    private static PImage[] playerUp;
    private static PImage[] playerLeft;
    private static PImage[] playerRight;

    /**
     * Represents the initlal location of the Player.
     */
    private Tuple initialLocation;

    /**
     * Represents the number of lives of the Player. Initial count specified in the
     * configuration JSON file.
     */
    private int lifeCount;

    /**
     * Represents the number of frames the current sprite is drawn, intially 0.
     */
    private int frameCount;

    /**
     * List of Bomb objects created by the Player.
     */
    private List<Bomb> currBombs;

    /**
     * Creates a new Player object. Require the initial location and number of lives
     * of the Player.
     */
    public Player(Tuple location, int lives) {
        super(location);
        this.initialLocation = new Tuple(location.x, location.y);
        this.lifeCount = lives;
        this.frameCount = 0;
        this.currBombs = new ArrayList<Bomb>();
    }

    /**
     * Loads in all the sprites required to generate the Player object's animation
     * on the app. Requires Papplet object.
     * 
     * @param app App of the game.
     */

    public static void setup(PApplet app) {
        playerDown_1 = app.loadImage("src/main/resources/player/player1.png");
        playerDown_2 = app.loadImage("src/main/resources/player/player2.png");
        playerDown_3 = app.loadImage("src/main/resources/player/player3.png");
        playerDown_4 = app.loadImage("src/main/resources/player/player4.png");

        playerDown = new PImage[] { playerDown_1, playerDown_2, playerDown_3, playerDown_4 };

        playerUp_1 = app.loadImage("src/main/resources/player/player_up1.png");
        playerUp_2 = app.loadImage("src/main/resources/player/player_up2.png");
        playerUp_3 = app.loadImage("src/main/resources/player/player_up3.png");
        playerUp_4 = app.loadImage("src/main/resources/player/player_up4.png");

        playerUp = new PImage[] { playerUp_1, playerUp_2, playerUp_3, playerUp_4 };

        playerLeft_1 = app.loadImage("src/main/resources/player/player_left1.png");
        playerLeft_2 = app.loadImage("src/main/resources/player/player_left2.png");
        playerLeft_3 = app.loadImage("src/main/resources/player/player_left3.png");
        playerLeft_4 = app.loadImage("src/main/resources/player/player_left4.png");

        playerLeft = new PImage[] { playerLeft_1, playerLeft_2, playerLeft_3, playerLeft_4 };

        playerRight_1 = app.loadImage("src/main/resources/player/player_right1.png");
        playerRight_2 = app.loadImage("src/main/resources/player/player_right2.png");
        playerRight_3 = app.loadImage("src/main/resources/player/player_right3.png");
        playerRight_4 = app.loadImage("src/main/resources/player/player_right4.png");

        playerRight = new PImage[] { playerRight_1, playerRight_2, playerRight_3, playerRight_4 };

    }

    /**
     * Creates a Bomb object on the current location of the player and adds it to
     * the list of Bombs created by the Player.
     */
    public void bomb() {
        int xCoord = this.currLocation.x;
        int yCoord = this.currLocation.y;
        Bomb bomb = new Bomb(new Tuple(xCoord, yCoord));
        this.currBombs.add(bomb);
    }

    /**
     * Returns the list of Bomb objects created by the Player.
     * 
     * @return The list of Bomb objects.
     */
    public List<Bomb> getBombs() {
        return this.currBombs;
    }

    /**
     * Returns the current life count of the Player.
     * 
     * @return Current life count.
     */
    public int getLifeCount() {
        return this.lifeCount;
    }

    /**
     * Player dies. Life count decrements and Player returns to inital location.
     */
    public void die() {
        this.currLocation.update(initialLocation.x, initialLocation.y);
        this.lifeCount--;
    }

    /**
     * Draws the Player sprite on the app window. Frame count resets when it reaches
     * the total frames of a cycle. Requires PApplet obejct.
     * 
     * @param app App of the game.
     */
    public void draw(PApplet app) {
        int currentSprite = Math.floorDiv(frameCount, FRAMES_PER_SPRITE);
        if (this.direction.equals("Down")) {
            app.image(playerDown[currentSprite], pixelX(currLocation.x), pixelY(currLocation.y));
        } else if (this.direction.equals("Up")) {
            app.image(playerUp[currentSprite], pixelX(currLocation.x), pixelY(currLocation.y));
        } else if (this.direction.equals("Left")) {
            app.image(playerLeft[currentSprite], pixelX(currLocation.x), pixelY(currLocation.y));
        } else if (this.direction.equals("Right")) {
            app.image(playerRight[currentSprite], pixelX(currLocation.x), pixelY(currLocation.y));
        }
        frameCount++;
        if (this.frameCount == TOTAL_FRAMES) {
            this.frameCount = 0;
        }
    }
}
