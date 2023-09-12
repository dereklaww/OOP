package demolition;

/**
 * Represents the parent class of in-game character classes: {@link Player},
 * {@link RedEnemy}, {@link YellowEnemy}
 */
public class BomberChar extends GameObjects {

    /**
     * Vertical offset value when drawing characters.
     */
    protected final int OFFSET = 48;

    /**
     * Represents the size of one grid in pixels.
     */
    protected final int GRID_SIZE = 32;

    /**
     * Represents the time each sprite shoud be rendered before moving to onto the
     * next sprite of the cycle.
     */
    protected final double TIME_PER_SPRITE = 0.2;

    /**
     * Represents the number of sprites in one cycle.
     */
    protected final int NUMBER_OF_SPRITE = 4;

    /**
     * Represents the number of frames each sprite of the character will be
     * rendered.
     */
    protected final int FRAMES_PER_SPRITE = framesPerSprite(TIME_PER_SPRITE);

    /**
     * Represents the total number of frames the character will be rendered in one
     * cycle.
     */
    protected final int TOTAL_FRAMES = FRAMES_PER_SPRITE * NUMBER_OF_SPRITE;

    /**
     * Intial location of the character.
     */
    protected Tuple currLocation;

    /**
     * Direction which the character is facing.
     */
    protected String direction;

    /**
     * Constructor of parent class, requires intial location of the character on the
     * grid. Character will face downwards intially.
     * 
     * @param location Current location of the character.
     */
    protected BomberChar(Tuple location) {
        this.currLocation = location;
        this.direction = "Down";
    }

    /**
     * Returns the horizonal component of the character's current location in
     * pixels.
     * 
     * @param xCoord The x coordinate of the character
     * @return value in pixels
     */
    protected int pixelX(int xCoord) {
        return xCoord * GRID_SIZE;
    }

    /**
     * Returns the vertical component of the character's current location in pixels.
     * 
     * @param yCoord The y coordinate of the character
     * @return value
     */
    protected int pixelY(int yCoord) {
        return OFFSET + yCoord * GRID_SIZE;
    }

    /**
     * Returns the coordinate pair of the character's current location. Only used
     * for testing purposes.
     * 
     * @return
     */
    protected Tuple getCurrLocation() {
        return this.currLocation;
    }

    /**
     * Moves the character by 1 unit on the grid based on the given location. The
     * character may only traverse through empty tiles. Upon reaching the goal tile,
     * the Player has completed the level. Enemies can traverse though the goal tile
     * normally;
     * 
     * @param moveDirection Direction the character is moving towards.
     * @param map           Map of the current level.
     * @return if the character successfully moves
     */
    protected boolean move(String moveDirection, BomberMap map) {

        int xFinal = -1;
        int yFinal = -1;

        if (moveDirection.equals("Up")) {
            xFinal = this.currLocation.x;
            yFinal = this.currLocation.y - 1;

        } else if (moveDirection.equals("Down")) {
            xFinal = this.currLocation.x;
            yFinal = this.currLocation.y + 1;
        } else if (moveDirection.equals("Left")) {
            xFinal = this.currLocation.x - 1;
            yFinal = this.currLocation.y;
        } else if (moveDirection.equals("Right")) {
            xFinal = this.currLocation.x + 1;
            yFinal = this.currLocation.y;
        }


        if (map.getTileType(xFinal, yFinal).equals("empty") || map.getTileType(xFinal, yFinal).equals("goal")) {
            this.currLocation.update(xFinal, yFinal);
            this.direction = moveDirection;
            return true;
        }
        
        return false;
    }
}
