package demolition;

import processing.core.PImage;

/**
 * Represents the tiles on the map layout.
 */
public class Tile {

    /**
     * Represents the tile type.
     */
    private String type;

    /**
     * Represents the tile sprite.
     */
    private PImage sprite;

    /**
     * Creates a new Tile object. Requires the type and sprite.
     * 
     * @param type   Tile type
     * @param sprite Tile sprite
     */
    public Tile(String type, PImage sprite) {
        this.type = type;
        this.sprite = sprite;
    }

    /**
     * Returns the tile type.
     * 
     * @return Tile type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns the tile sprite
     * 
     * @return Tile sprite
     */
    public PImage getSprite() {
        return this.sprite;
    }

}
