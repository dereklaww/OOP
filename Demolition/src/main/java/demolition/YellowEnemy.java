package demolition;

import java.util.ArrayList;
import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represents the enemy of player, yellow enemy
 */
public class YellowEnemy extends BomberChar {

    private static PImage yellowDown_1;
    private static PImage yellowDown_2;
    private static PImage yellowDown_3;
    private static PImage yellowDown_4;
    private static PImage yellowUp_1;
    private static PImage yellowUp_2;
    private static PImage yellowUp_3;
    private static PImage yellowUp_4;
    private static PImage yellowLeft_1;
    private static PImage yellowLeft_2;
    private static PImage yellowLeft_3;
    private static PImage yellowLeft_4;
    private static PImage yellowRight_1;
    private static PImage yellowRight_2;
    private static PImage yellowRight_3;
    private static PImage yellowRight_4;

    private static PImage[] yellowDown;
    private static PImage[] yellowUp;
    private static PImage[] yellowLeft;
    private static PImage[] yellowRight;

    /**
     * Represents the number of times the logic is called by the app, initially 0.
     */
    private int tickCount;

    /**
     * Represents the number of frames the current sprite is drawn, intially 0.
     */
    private int frameCount;

    /**
     * If the YellowEnemy object is alive.
     */
    private boolean alive;

    /**
     * Represents the initial location of the Yellow Enemy.
     */
    private Tuple initialLocation;

    /**
     * Creates a new YellowEnemy object. Requires intial location.
     * 
     * @param location Initial location of the YellowEnemy object.
     */
    public YellowEnemy(Tuple location) {
        super(location);
        this.initialLocation = new Tuple(location.x, location.y);
        this.tickCount = 0;
        this.frameCount = 0;
        this.alive = true;
    }

    /**
     * Loads in all the sprites required to generate the YellowEnemy object's
     * animation on the app. Requires Papplet object.
     * 
     * @param app App of the game.
     */
    public static void setup(PApplet app) {
        yellowDown_1 = app.loadImage("src/main/resources/yellow_enemy/yellow_down1.png");
        yellowDown_2 = app.loadImage("src/main/resources/yellow_enemy/yellow_down2.png");
        yellowDown_3 = app.loadImage("src/main/resources/yellow_enemy/yellow_down3.png");
        yellowDown_4 = app.loadImage("src/main/resources/yellow_enemy/yellow_down4.png");

        yellowDown = new PImage[] { yellowDown_1, yellowDown_2, yellowDown_3, yellowDown_4 };

        yellowUp_1 = app.loadImage("src/main/resources/yellow_enemy/yellow_up1.png");
        yellowUp_2 = app.loadImage("src/main/resources/yellow_enemy/yellow_up2.png");
        yellowUp_3 = app.loadImage("src/main/resources/yellow_enemy/yellow_up3.png");
        yellowUp_4 = app.loadImage("src/main/resources/yellow_enemy/yellow_up4.png");

        yellowUp = new PImage[] { yellowUp_1, yellowUp_2, yellowUp_3, yellowUp_4 };

        yellowLeft_1 = app.loadImage("src/main/resources/yellow_enemy/yellow_left1.png");
        yellowLeft_2 = app.loadImage("src/main/resources/yellow_enemy/yellow_left2.png");
        yellowLeft_3 = app.loadImage("src/main/resources/yellow_enemy/yellow_left3.png");
        yellowLeft_4 = app.loadImage("src/main/resources/yellow_enemy/yellow_left4.png");

        yellowLeft = new PImage[] { yellowLeft_1, yellowLeft_2, yellowLeft_3, yellowLeft_4 };

        yellowRight_1 = app.loadImage("src/main/resources/yellow_enemy/yellow_right1.png");
        yellowRight_2 = app.loadImage("src/main/resources/yellow_enemy/yellow_right2.png");
        yellowRight_3 = app.loadImage("src/main/resources/yellow_enemy/yellow_right3.png");
        yellowRight_4 = app.loadImage("src/main/resources/yellow_enemy/yellow_right4.png");

        yellowRight = new PImage[] { yellowRight_1, yellowRight_2, yellowRight_3, yellowRight_4 };

    }

    /**
     * YellowEnemy dies. The object is removed from the app window.
     */
    public void die() {
        this.alive = false;
    }

    /**
     * Returns if the YellowEnemy is alive.
     * 
     * @return if the YellowEnemy is alive.
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Movement logic of the YellowEnemy. The Yellow Enemy moves in a straight line
     * every second , but on collision with a wall it will attempt to move
     * clockwise.
     * 
     * @param map Map of the current level.
     */
    public void tick(BomberMap map) {
        if (this.tickCount == 60) {
            if (!move(this.direction, map)) {
                ArrayList<String> directions = new ArrayList<String>(Arrays.asList("Up", "Right", "Down", "Left"));
                int curr = directions.indexOf(this.direction);
                ArrayList<String> clockwise = new ArrayList<String>();

                for (int i = curr; i < directions.size(); i++) {
                    clockwise.add(directions.get(i));
                }
                for (int j = 0; j < curr; j++) {
                    clockwise.add(directions.get(j));
                }

                for (int k = 0; k < clockwise.size(); k++) {
                    if (move(clockwise.get(k), map))
                        break;
                }
            }
            tickCount = 0;
        }
        tickCount++;
    }

    /**
     * Draws the YellowEnemy sprite on the app window. Frame count resets when it
     * reaches the total frames of a cycle. Requires PApplet obejct.
     * 
     * @param app App of the game.
     */
    public void draw(PApplet app) {
        if (alive) {
            int currentSprite = Math.floorDiv(frameCount, FRAMES_PER_SPRITE);
            if (this.direction.equals("Down")) {
                app.image(yellowDown[currentSprite], pixelX(currLocation.x), pixelY(currLocation.y));
            } else if (this.direction.equals("Up")) {
                app.image(yellowUp[currentSprite], pixelX(currLocation.x), pixelY(currLocation.y));
            } else if (this.direction.equals("Left")) {
                app.image(yellowLeft[currentSprite], pixelX(currLocation.x), pixelY(currLocation.y));
            } else if (this.direction.equals("Right")) {
                app.image(yellowRight[currentSprite], pixelX(currLocation.x), pixelY(currLocation.y));
            }
            frameCount++;
            if (frameCount == TOTAL_FRAMES) {
                frameCount = 0;
            }
        }
    }

    /**
     * Returns the initial location of the Yellow Enemy.
     * 
     * @return Initial location of the Yellow Enemy.
     */
    public Tuple getInitialLocation() {
        return this.initialLocation;
    }

}
