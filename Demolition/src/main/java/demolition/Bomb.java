package demolition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represents a bomb utilized by the {@link Player}
 */
public class Bomb extends GameObjects {

    /**
     * Vertical offset value when drawing characters.
     */
    private final int OFFSET = 64;

    /**
     * Represents the size of one grid in pixels.
     */
    private final int GRID_SIZE = 32;

    /**
     * Represents the time each sprite shoud be rendered before moving to onto the
     * next sprite of the cycle.
     */
    protected final double TIME_PER_SPRITE_BOMB = 0.25;

    /**
     * Represents the number of sprites to be rendered in one cycle.
     */
    protected final int NUM_OF_SPRITE_BOMB = 8;

    /**
     * Represents the number of frames each sprite will be rendered.
     */
    protected final int FRAMES_PER_SPRITE_BOMB = framesPerSprite(TIME_PER_SPRITE_BOMB);

    /**
     * Represents the total number of frames the bomb will be rendered in one cycle.
     */
    protected final int TOTAL_FRAMES_BOMB = NUM_OF_SPRITE_BOMB * FRAMES_PER_SPRITE_BOMB;

    /**
     * Represents the total number of frames the explosion will be rendered in one
     * cycle.
     */
    protected final int TOTAL_FRAMES_EXPLOSION = (int) (0.5 * FPS);

    private static PImage bomb1;
    private static PImage bomb2;
    private static PImage bomb3;
    private static PImage bomb4;
    private static PImage bomb5;
    private static PImage bomb6;
    private static PImage bomb7;
    private static PImage bomb8;
    private static PImage[] bombSprites;

    private static PImage explosionCentre;
    private static PImage explosionHorizontal;
    private static PImage explosionVertical;
    private static PImage explosionBottom;
    private static PImage explosionTop;
    private static PImage explosionLeft;
    private static PImage explosionRight;

    /**
     * Location which the bomb is placed.
     */
    private Tuple location;

    /**
     * Number of frames the bomb is currently drawn, intially 0.
     */
    private int bombFrameCount;

    /**
     * Number of frames the explostion is currently drawn, intially 0.
     */
    private int explosionFrameCount;

    /**
     * If the bomb has exploded, initally false.
     */
    private boolean explode;

    /**
     * HashMap which stores explosion sprites in (location/sprite) "key/value"
     * pairs.
     */
    private HashMap<Tuple, PImage> explosionCommands;

    /**
     * List of all locations affected by the explosion.
     */
    private List<Tuple> explosionArea;

    /**
     * List of all demolished "Broken" {@link Tile}.
     */
    private List<Tuple> demolished;

    /**
     * Creates a new bomb object, requires location which the bomb is placed.
     * 
     * @param location Location which the bomb is placed.
     */
    public Bomb(Tuple location) {
        this.location = location;
        this.bombFrameCount = 0;
        this.explosionFrameCount = 0;
        this.explode = false;
        this.explosionCommands = new HashMap<Tuple, PImage>();
        this.explosionArea = new ArrayList<Tuple>();
        this.demolished = new ArrayList<Tuple>();
    }

    /**
     * Loads in all the sprites required to generate the bomb and explotion
     * animation on the app. Requires the app of the game.
     * 
     * @param app App of the game.
     */
    public static void setup(PApplet app) {
        // load bomb image
        bomb1 = app.loadImage("src/main/resources/bomb/bomb1.png");
        bomb2 = app.loadImage("src/main/resources/bomb/bomb2.png");
        bomb3 = app.loadImage("src/main/resources/bomb/bomb3.png");
        bomb4 = app.loadImage("src/main/resources/bomb/bomb4.png");
        bomb5 = app.loadImage("src/main/resources/bomb/bomb5.png");
        bomb6 = app.loadImage("src/main/resources/bomb/bomb6.png");
        bomb7 = app.loadImage("src/main/resources/bomb/bomb7.png");
        bomb8 = app.loadImage("src/main/resources/bomb/bomb8.png");

        bombSprites = new PImage[] { bomb1, bomb2, bomb3, bomb4, bomb5, bomb6, bomb7, bomb8 };

        // load explosion image
        explosionCentre = app.loadImage("src/main/resources/explosion/centre.png");
        explosionHorizontal = app.loadImage("src/main/resources/explosion/horizontal.png");
        explosionVertical = app.loadImage("src/main/resources/explosion/vertical.png");
        explosionBottom = app.loadImage("src/main/resources/explosion/end_bottom.png");
        explosionTop = app.loadImage("src/main/resources/explosion/end_top.png");
        explosionLeft = app.loadImage("src/main/resources/explosion/end_left.png");
        explosionRight = app.loadImage("src/main/resources/explosion/end_right.png");

    }

    /**
     * Returns the horizontal component of the bomb's location in pixels.
     * 
     * @param xCoord The x coordinate of the bomb.
     * @return value in pixels
     */
    public int pixelX(int xCoord) {
        return xCoord * GRID_SIZE;
    }

    /**
     * Returns the vertical component of the bomb's location in pixels.
     * 
     * @param yCoord The y coordinate of the bomb.
     * @return value in pixels
     */
    public int pixelY(int yCoord) {
        return OFFSET + yCoord * GRID_SIZE;
    }

    /**
     * Signifies the bomb has exploded.
     */
    private void explode() {
        this.explode = true;
    }

    /**
     * Kill characters within the explosion area. Player loses a life while RedEnemy
     * and YellowEnemy dies.
     * 
     * @param player  Player
     * @param reds    List of red enemies on the map.
     * @param yellows List of yellow enemies on the map.
     */
    private void kill(Player player, List<RedEnemy> reds, List<YellowEnemy> yellows) {
        if (Tuple.contains(explosionArea, player.currLocation)) {
            player.die();
        }

        for (RedEnemy red : reds) {
            if (Tuple.contains(explosionArea, red.currLocation)) {
                red.die();
            }
        }

        for (YellowEnemy yellow : yellows) {
            if (Tuple.contains(explosionArea, yellow.currLocation)) {
                yellow.die();
            }
        }
    }

    /**
     * Records the explosion area of the bomb and adds the "location/sprite" pairs
     * to the hashmap. The explotion's shockwave extends in the four cardinal
     * directions to a maximum of 2 grids from the location which the bomb was
     * placed. The explostion can be stopped earlier than 2 spaces if it comes in
     * contact with "Wall" or "Broken" tile. The "Broken" tile will be demolished.
     * 
     * @param map Map of current level.
     */
    private void explosion(BomberMap map) {
        this.explosionCommands.put(location, explosionCentre);
        this.explosionArea.add(location);

        int xCoordCentre = location.x;
        int yCoordCentre = location.y;

        // up
        int yCoordUp1 = yCoordCentre - 1;
        int yCoordUp2 = yCoordCentre - 2;

        if (map.getTileType(xCoordCentre, yCoordUp1).equals("empty")) {
            if (map.getTileType(xCoordCentre, yCoordUp2).equals("empty")) {
                Tuple up1 = new Tuple(xCoordCentre, yCoordUp1);
                Tuple up2 = new Tuple(xCoordCentre, yCoordUp2);
                this.explosionArea.add(up1);
                this.explosionArea.add(up2);
                this.explosionCommands.put(up1, explosionVertical);
                this.explosionCommands.put(up2, explosionTop);
            } else if (map.getTileType(xCoordCentre, yCoordUp2).equals("broken")) {
                Tuple up1 = new Tuple(xCoordCentre, yCoordUp1);
                Tuple up2 = new Tuple(xCoordCentre, yCoordUp2);
                this.explosionCommands.put(up1, explosionVertical);
                this.explosionCommands.put(up2, explosionTop);
                this.explosionArea.add(up1);
                this.demolished.add((new Tuple(xCoordCentre, yCoordUp2)));
            } else {
                Tuple up1 = new Tuple(xCoordCentre, yCoordUp1);
                this.explosionCommands.put(up1, explosionTop);
                this.explosionArea.add(up1);
            }
        } else if (map.getTileType(xCoordCentre, yCoordUp1).equals("broken")) {
            Tuple up1 = new Tuple(xCoordCentre, yCoordUp1);
            this.explosionCommands.put(up1, explosionTop);
            this.demolished.add((new Tuple(xCoordCentre, yCoordUp1)));
        }

        int yCoordDown1 = yCoordCentre + 1;
        int yCoordDown2 = yCoordCentre + 2;

        if (map.getTileType(xCoordCentre, yCoordDown1).equals("empty")) {
            if (map.getTileType(xCoordCentre, yCoordDown2).equals("empty")) {
                Tuple down1 = new Tuple(xCoordCentre, yCoordDown1);
                Tuple down2 = new Tuple(xCoordCentre, yCoordDown2);
                this.explosionArea.add(down1);
                this.explosionArea.add(down2);
                this.explosionCommands.put(down1, explosionVertical);
                this.explosionCommands.put(down2, explosionBottom);
            } else if (map.getTileType(xCoordCentre, yCoordDown2).equals("broken")) {
                Tuple down1 = new Tuple(xCoordCentre, yCoordDown1);
                Tuple down2 = new Tuple(xCoordCentre, yCoordDown2);
                this.explosionCommands.put(down1, explosionVertical);
                this.explosionCommands.put(down2, explosionBottom);
                this.explosionArea.add(down1);
                this.demolished.add((new Tuple(xCoordCentre, yCoordDown2)));
            } else {
                Tuple down1 = new Tuple(xCoordCentre, yCoordDown1);
                this.explosionCommands.put(down1, explosionBottom);
                this.explosionArea.add(down1);
            }
        } else if (map.getTileType(xCoordCentre, yCoordDown1).equals("broken")) {
            Tuple down1 = new Tuple(xCoordCentre, yCoordDown1);
            this.explosionCommands.put(down1, explosionBottom);
            this.demolished.add((new Tuple(xCoordCentre, yCoordDown1)));
        }

        int xCoordLeft1 = xCoordCentre - 1;
        int xCoordLeft2 = xCoordCentre - 2;

        if (map.getTileType(xCoordLeft1, yCoordCentre).equals("empty")) {
            if (map.getTileType(xCoordLeft2, yCoordCentre).equals("empty")) {
                Tuple left1 = new Tuple(xCoordLeft1, yCoordCentre);
                Tuple left2 = new Tuple(xCoordLeft2, yCoordCentre);
                this.explosionArea.add(left1);
                this.explosionArea.add(left2);
                this.explosionCommands.put(left1, explosionHorizontal);
                this.explosionCommands.put(left2, explosionLeft);

            } else if (map.getTileType(xCoordLeft2, yCoordCentre).equals("broken")) {
                Tuple left1 = new Tuple(xCoordLeft1, yCoordCentre);
                Tuple left2 = new Tuple(xCoordLeft2, yCoordCentre);
                this.explosionCommands.put(left1, explosionHorizontal);
                this.explosionCommands.put(left2, explosionLeft);
                this.explosionArea.add(left1);
                this.demolished.add((new Tuple(xCoordLeft2, yCoordCentre)));

            } else {
                Tuple left1 = new Tuple(xCoordLeft1, yCoordCentre);
                this.explosionCommands.put(left1, explosionLeft);
                this.explosionArea.add(left1);
            }
        } else if (map.getTileType(xCoordLeft1, yCoordCentre).equals("broken")) {
            Tuple left1 = new Tuple(xCoordLeft1, yCoordCentre);
            this.explosionCommands.put(left1, explosionLeft);
            this.demolished.add((new Tuple(xCoordLeft1, yCoordCentre)));
        }

        int xCoordRight1 = xCoordCentre + 1;
        int xCoordRight2 = xCoordCentre + 2;

        if (map.getTileType(xCoordRight1, yCoordCentre).equals("empty")) {
            if (map.getTileType(xCoordRight2, yCoordCentre).equals("empty")) {
                Tuple right1 = new Tuple(xCoordRight1, yCoordCentre);
                Tuple right2 = new Tuple(xCoordRight2, yCoordCentre);
                this.explosionArea.add(right1);
                this.explosionArea.add(right2);
                this.explosionCommands.put(right1, explosionHorizontal);
                this.explosionCommands.put(right2, explosionRight);

            } else if (map.getTileType(xCoordRight2, yCoordCentre).equals("broken")) {
                Tuple right1 = new Tuple(xCoordRight1, yCoordCentre);
                Tuple right2 = new Tuple(xCoordRight2, yCoordCentre);
                this.explosionCommands.put(right1, explosionHorizontal);
                this.explosionCommands.put(right2, explosionRight);
                this.explosionArea.add(right1);
                this.demolished.add((new Tuple(xCoordRight2, yCoordCentre)));

            } else {
                Tuple right1 = new Tuple(xCoordRight1, yCoordCentre);
                this.explosionCommands.put(right1, explosionRight);
                this.explosionArea.add(right1);
            }
        } else if (map.getTileType(xCoordRight1, yCoordCentre).equals("broken")) {
            Tuple right1 = new Tuple(xCoordRight1, yCoordCentre);
            this.explosionCommands.put(right1, explosionRight);
            this.demolished.add((new Tuple(xCoordRight1, yCoordCentre)));

        }
    }

    /**
     * The bomb will explode when it is rendered for the specified amount of frames.
     * This will trigger an explosion which will kill characters and demolish
     * "Broken" tiles within the explosion area.
     * 
     * @param map     Map of the current level.
     * @param player  Player
     * @param reds    List of red enemies on the map.
     * @param yellows List of yellow enemies on the map.
     */
    public void tick(BomberMap map, Player player, List<RedEnemy> reds, List<YellowEnemy> yellows) {
        if (bombFrameCount == TOTAL_FRAMES_BOMB) {
            this.explode();
            this.explosion(map);
        }

        if (explosionFrameCount < TOTAL_FRAMES_EXPLOSION) {
            this.kill(player, reds, yellows);
        }

        if (explosionFrameCount == TOTAL_FRAMES_EXPLOSION - 1) {
            for (Tuple location : this.demolished) {
                map.demolish(location);
            }
        }
    }

    /**
     * Draws the bomb and explosion sprites on the app window. Require Papplet
     * object.
     * 
     * @param app PApplet app of the game.
     */
    public void draw(PApplet app) {
        if (!this.explode) {
            int currentSprite = Math.floorDiv(bombFrameCount, FRAMES_PER_SPRITE_BOMB);
            app.image(bombSprites[currentSprite], pixelX(location.x), pixelY(location.y));
            bombFrameCount++;
        } else {
            if (explosionFrameCount < TOTAL_FRAMES_EXPLOSION) {
                for (Tuple coords : this.explosionCommands.keySet()) {
                    int xPixel = pixelX(coords.x);
                    int yPixel = pixelY(coords.y);
                    app.image(this.explosionCommands.get(coords), xPixel, yPixel);
                }
                explosionFrameCount++;
            }
        }

    }
}
