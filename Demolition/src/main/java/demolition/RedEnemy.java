package demolition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represents the enemy of player, red enemy
 */
public class RedEnemy extends BomberChar {

    private static PImage redDown_1;
    private static PImage redDown_2;
    private static PImage redDown_3;
    private static PImage redDown_4;
    private static PImage redUp_1;
    private static PImage redUp_2;
    private static PImage redUp_3;
    private static PImage redUp_4;
    private static PImage redLeft_1;
    private static PImage redLeft_2;
    private static PImage redLeft_3;
    private static PImage redLeft_4;
    private static PImage redRight_1;
    private static PImage redRight_2;
    private static PImage redRight_3;
    private static PImage redRight_4;

    private static PImage[] redDown;
    private static PImage[] redUp;
    private static PImage[] redLeft;
    private static PImage[] redRight;

    /**
     * Represents the number of times the logic is called by the app, initially 0.
     */
    private int tickCount;

    /**
     * Represents the number of frames the current sprite is drawn, intially 0.
     */
    private int frameCount;

    /**
     * If the RedEnemy object is alive.
     */
    private boolean alive;

    /**
     * Represents the initial location of the Red Enemy.
     */
    private Tuple initialLocation;

    /**
     * Creates a new RedEnemy object. Requires intial location.
     * 
     * @param location Initial location of the RedEnemy object.
     */
    public RedEnemy(Tuple location) {
        super(location);
        this.initialLocation = new Tuple(location.x, location.y);
        this.tickCount = 0;
        this.frameCount = 0;
        this.alive = true;
    }

    /**
     * Loads in all the sprites required to generate the RedEnemy object's animation
     * on the app. Requires Papplet object.
     * 
     * @param app App of the game.
     */
    public static void setup(PApplet app) {
        redDown_1 = app.loadImage("src/main/resources/red_enemy/red_down1.png");
        redDown_2 = app.loadImage("src/main/resources/red_enemy/red_down2.png");
        redDown_3 = app.loadImage("src/main/resources/red_enemy/red_down3.png");
        redDown_4 = app.loadImage("src/main/resources/red_enemy/red_down4.png");

        redDown = new PImage[] { redDown_1, redDown_2, redDown_3, redDown_4 };

        redUp_1 = app.loadImage("src/main/resources/red_enemy/red_up1.png");
        redUp_2 = app.loadImage("src/main/resources/red_enemy/red_up2.png");
        redUp_3 = app.loadImage("src/main/resources/red_enemy/red_up3.png");
        redUp_4 = app.loadImage("src/main/resources/red_enemy/red_up4.png");

        redUp = new PImage[] { redUp_1, redUp_2, redUp_3, redUp_4 };

        redLeft_1 = app.loadImage("src/main/resources/red_enemy/red_left1.png");
        redLeft_2 = app.loadImage("src/main/resources/red_enemy/red_left2.png");
        redLeft_3 = app.loadImage("src/main/resources/red_enemy/red_left3.png");
        redLeft_4 = app.loadImage("src/main/resources/red_enemy/red_left4.png");

        redLeft = new PImage[] { redLeft_1, redLeft_2, redLeft_3, redLeft_4 };

        redRight_1 = app.loadImage("src/main/resources/red_enemy/red_right1.png");
        redRight_2 = app.loadImage("src/main/resources/red_enemy/red_right2.png");
        redRight_3 = app.loadImage("src/main/resources/red_enemy/red_right3.png");
        redRight_4 = app.loadImage("src/main/resources/red_enemy/red_right4.png");

        redRight = new PImage[] { redRight_1, redRight_2, redRight_3, redRight_4 };

    }

    /**
     * RedEnemy dies. The object is removed from the app window.
     */
    public void die() {
        this.alive = false;
    }

    /**
     * Returns if the RedEnemy is alive.
     * 
     * @return if the RedEnemy is alive.
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Movement logic of the RedEnemy. The object moves in a straight line every
     * second and turns to a random decision every time its path is blocked by a
     * solid/broken wall.
     * 
     * @param map Map of the current level.
     */
    public void tick(BomberMap map) {
        if (alive) {
            if (this.tickCount == 60) {
                if (!move(this.direction, map)) {
                    ArrayList<String> directions = new ArrayList<String>(Arrays.asList("Up", "Down", "Left", "Right"));
                    directions.remove(this.direction);
                    Random random = new Random();
                    int int_random = random.nextInt(directions.size());
                    String direction = directions.get(int_random);
                    while (!move(direction, map)) {
                        int_random = random.nextInt(directions.size());
                        direction = directions.get(int_random);
                        directions.remove(direction);
                    }
                }
                tickCount = 0;
            }
            tickCount++;
        }
    }

    /**
     * Draws the RedEnemy sprite on the app window. Frame count resets when it
     * reaches the total frames of a cycle. Requires PApplet obejct.
     * 
     * @param app App of the game.
     */
    public void draw(PApplet app) {
        if (alive) {
            int currentSprite = Math.floorDiv(frameCount, FRAMES_PER_SPRITE);
            if (this.direction.equals("Down")) {
                app.image(redDown[currentSprite], pixelX(currLocation.x), pixelY(currLocation.y));
            } else if (this.direction.equals("Up")) {
                app.image(redUp[currentSprite], pixelX(currLocation.x), pixelY(currLocation.y));
            } else if (this.direction.equals("Left")) {
                app.image(redLeft[currentSprite], pixelX(currLocation.x), pixelY(currLocation.y));
            } else if (this.direction.equals("Right")) {
                app.image(redRight[currentSprite], pixelX(currLocation.x), pixelY(currLocation.y));
            }
            frameCount++;
            if (frameCount == TOTAL_FRAMES) {
                frameCount = 0;
            }
        }
    }

    /**
     * Returns the initial location of the Red Rnemy. (Only for testing purposes)
     * 
     * @return Initial location of the Red Enemy.
     */
    public Tuple getInitialLocation() {
        return this.initialLocation;
    }
}
