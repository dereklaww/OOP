package demolition;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Exception which is thrown when the map layout is invalid.
 */
class InvalidConfigurationException extends Exception {
    /**
     * Creates a new InvalidConfigurationException with the given error message
     * based on the condition it was raised by.
     * 
     * @param message Error message
     */
    public InvalidConfigurationException(String message) {
        super(message);
    }
}

/**
 * Represents the map layout of the game.
 */
public class BomberMap {

    /**
     * Vertical offset value when drawing characters.
     */
    private final int OFFSET = 64;

    /**
     * Represents the size of one grid in pixels.
     */
    private final int GRID_SIZE = 32;

    /**
     * Represents the width of the app window.
     */
    private final int WIDTH_GRIDS = 15;

    /**
     * Represents the height of the app window.
     */
    private final int HEIGHT_GRIDS = 13;

    private static Tile emptyTile;
    private static Tile brokenTile;
    private static Tile wallTile;
    private static Tile goalTile;

    /**
     * Two-dimensional Tile array which stores the Tile on each coordinate on the
     * map.
     */
    private Tile[][] mapCoord;

    /**
     * Initial location of the Player.
     */
    private Tuple playerInitialLocation;

    /**
     * Location of the Goal tile.
     */
    private Tuple goalLocation;

    /**
     * List of initial locations of the RedEnemy objects.
     */
    private List<Tuple> redLocation;

    /**
     * List of intial locations of the YellowEnemy objects.
     */
    private List<Tuple> yellowLocation;

    /**
     * Creates a new BomberMap object.
     */
    public BomberMap() {
        this.mapCoord = new Tile[HEIGHT_GRIDS][WIDTH_GRIDS];
        this.playerInitialLocation = null;
        this.redLocation = new ArrayList<Tuple>();
        this.yellowLocation = new ArrayList<Tuple>();
        this.goalLocation = null;
    }

    /**
     * Loads the required four Tile objects (Empty, Broken, Wall, Goal) and their
     * respective sprites. Require Papplet object.
     * 
     * @param app App of the game.
     */
    public static void setup(PApplet app) {
        // load images
        PImage empty = app.loadImage("src/main/resources/empty/empty.png");
        PImage broken = app.loadImage("src/main/resources/broken/broken.png");
        PImage wall = app.loadImage("src/main/resources/wall/solid.png");
        PImage goal = app.loadImage("src/main/resources/goal/goal.png");

        // create necessary instances
        emptyTile = new Tile("empty", empty);
        brokenTile = new Tile("broken", broken);
        wallTile = new Tile("wall", wall);
        goalTile = new Tile("goal", goal);
    }

    /**
     * Reads the given configuration .txt file which contains the Tile types and
     * characters on each coordinate. The .txt file will be checked if the map
     * layout is valid before pairing the Tile obejcts to their respective
     * locations.
     * 
     * @param filename
     * @throws InvalidConfigurationException
     */
    public void read(String filename) throws InvalidConfigurationException {

        BufferedReader checker = null;
        BufferedReader reader = null;
        int upperBorderIndex = -1;
        int lowerBorderIndex = -1;
        List<String> mapChars = new ArrayList<String>(
                Arrays.asList(new String[] { "W", "B", "G", " ", "P", "R", "Y" }));

        try {
            String strCurrentLine;
            int rows = 0;
            boolean upperBorder = false;
            boolean lowerBorder = false;

            checker = new BufferedReader(new FileReader(filename));

            while ((strCurrentLine = checker.readLine()) != null) {

                //Check map width
                if (strCurrentLine.length() != 15) {
                    throw new InvalidConfigurationException("Invalid Configuration File: width should be 15!");
                }
                ;

                //Checlk upper and lower borders are all Wall tiles
                if (strCurrentLine.equals("WWWWWWWWWWWWWWW")) {
                    if (upperBorder) {
                        lowerBorder = true;
                        lowerBorderIndex = rows;
                    } else {
                        upperBorder = true;
                        upperBorderIndex = rows;
                    }
                }

                rows++;
            }

            if (!(upperBorder && lowerBorder)) {
                throw new InvalidConfigurationException("Invalid Configuration File: invalid upper or lower borders");
            }

            //Check map height
            if (rows != HEIGHT_GRIDS) {
                throw new InvalidConfigurationException("Invalid Configuration File: height should be 13!");
            }

        } catch (FileNotFoundException ex) {
            System.err.println("File not found");

        } catch (IOException ex) {
            ex.printStackTrace();

        } finally {
            try {
                if (checker != null)
                    checker.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try {
            reader = new BufferedReader(new FileReader(filename));

            for (int i = 0; i < HEIGHT_GRIDS; i++) {
                String[] blockTypes = reader.readLine().split("");

                //Check side borders are all Wall tiles.
                if (i > upperBorderIndex && i < lowerBorderIndex) {
                    if (!(blockTypes[0].equals("W") && blockTypes[WIDTH_GRIDS - 1].equals("W"))) {
                        throw new InvalidConfigurationException(
                                "Invalid Configuration File: invalid side borders on " + filename + ": row " + (i + 1));
                    }
                }

                //Check invalid Characters
                for (int j = 0; j < WIDTH_GRIDS; j++) {
                    if (!(mapChars.contains(blockTypes[j]))) {
                        throw new InvalidConfigurationException("Invalid Configuration File: invalid map character "
                                + blockTypes[j] + " on " + filename + ": row " + (i + 1));

                    }

                    //Parse Tile objects to map configuration 2D array
                    if (blockTypes[j].equals("W")) {
                        mapCoord[i][j] = wallTile;

                    } else if (blockTypes[j].equals("B")) {
                        mapCoord[i][j] = brokenTile;

                    } else if (blockTypes[j].equals("G")) {

                        //if goal location is previously specified, raise multiple goals error
                        if (this.goalLocation != null) {
                            throw new InvalidConfigurationException(
                                    "Invalid Configuration File: multiple goals specified!");
                        } else {
                            mapCoord[i][j] = goalTile;
                            this.goalLocation = new Tuple(j, i);
                        }

                    } else if (blockTypes[j].equals("P")) {

                        //if player's initial location is specified, raise multiple players error
                        if (this.playerInitialLocation != null) {
                            throw new InvalidConfigurationException(
                                    "Invalid Configuration File: multiple players specified!");
                        } else {
                            mapCoord[i][j] = emptyTile;
                            this.playerInitialLocation = new Tuple(j, i);
                        }

                    } else if (blockTypes[j].equals("R")) {
                        mapCoord[i][j] = emptyTile;
                        this.redLocation.add(new Tuple(j, i));

                    } else if (blockTypes[j].equals("Y")) {
                        mapCoord[i][j] = emptyTile;
                        this.yellowLocation.add(new Tuple(j, i));

                    } else {
                        mapCoord[i][j] = emptyTile;
                    }

                }
            }
            //if no player initial location is specified, raise no player error
            if (this.playerInitialLocation == null) {
                throw new InvalidConfigurationException("Invalid Configuration File: No player location specified!");
            }

            //if no goal location is specified, raise no goal error
            if (this.goalLocation == null) {
                throw new InvalidConfigurationException("Invalid Configuration File: No goal location specified!");
            }

        } catch (FileNotFoundException ex) {
            System.err.println("File not found");

        } catch (IOException ex) {
            ex.printStackTrace();

        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Demolish the "Broken" Tile on the given location.
     * 
     * @param location Location of the "Broken" Tile
     */
    public void demolish(Tuple location) {
        int x = location.x;
        int y = location.y;
        mapCoord[y][x] = emptyTile;
    }

    /**
     * Returns the Tile type on the specified location of the map. Requires the
     * coordinates of the desired location.
     * 
     * @param x x coordinate of the location
     * @param y y coordinate of the location
     * @return Tile type
     */
    public String getTileType(int x, int y) {
        Tile tile = mapCoord[y][x];
        return tile.getType();
    }

    /**
     * Returns the initial location of the Player.
     * 
     * @return Initial location of the Player.
     */
    public Tuple getPlayerInitialLocation() {
        return this.playerInitialLocation;
    }

    /**
     * Returns the list of initial location of the RedEnemy objects.
     * 
     * @return List of initial location of the RedEnemy objects.
     */
    public List<Tuple> getRedLocation() {
        return this.redLocation;
    }

    /**
     * Returns the list of initial location of the YellowEnemy objects.
     * 
     * @return List of initial location of the YellowEnemy objects.
     */
    public List<Tuple> getYellowLocation() {
        return this.yellowLocation;
    }

    /**
     * Returns the location of the Goal Tile.
     * 
     * @return Location of the Goal Tile.
     */
    public Tuple getGoalLocation() {
        return this.goalLocation;
    }

    /**
     * Draws the respective Tile sprite on the app window. Requires Papplet object.
     * 
     * @param app
     */
    public void draw(PApplet app) {
        for (int i = 0; i < HEIGHT_GRIDS; i++) {
            for (int j = 0; j < WIDTH_GRIDS; j++) {
                app.image(this.mapCoord[i][j].getSprite(), (j * GRID_SIZE), (OFFSET + (i * GRID_SIZE)));
            }
        }
    }

}
