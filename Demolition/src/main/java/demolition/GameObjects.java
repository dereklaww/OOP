package demolition;

/**
 * Represents the parent class of all in-game object classes :
 * {@link BomberChar}, {@link Bomb}
 */
public abstract class GameObjects {

    /**
     * Represents the number of frames the program is drawing per second.
     */
    protected static final int FPS = App.FPS;

    /**
     * Returns the number of frames a sprite is rendered based on the required
     * screen time of each sprite.
     * 
     * @param time Required screen time of each sprite.
     * @return Number of frames a sprite is rendered.
     */
    protected int framesPerSprite(double time) {
        return (int) (time * FPS);
    }
}
