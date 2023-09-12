package demolition;

import java.util.List;

/**
 * A mechanism which stores a pair of integers. Used to record the coordinates
 * of the location of the game objects.
 */
public class Tuple {

    /**
     * Represents the first integer of the pair.
     */
    public int x;

    /**
     * Represents the second integer of the pair.
     */
    public int y;

    /**
     * Creates a new Tuple object. Requires two integers.
     * 
     * @param x First integer of the pair
     * @param y Second integer of the pair
     */
    public Tuple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Replaces the pair of integers. Requires a new pair of integers.
     * 
     * @param x First integer of new pair
     * @param y Second integer of new pair
     */
    public void update(int x, int y) {
        this.x = x;
        this.y = y;

    }

    /**
     * Returns true if the given Tuple exists in the given list of Tuple objects.
     * 
     * @param tuples List of Tuple objects
     * @param tuple  Given Tuple
     * @return If the given Tuple exists in the given list of Tuple objects.
     */
    public static boolean contains(List<Tuple> tuples, Tuple tuple) {
        for (Tuple existingTuple : tuples) {
            if (existingTuple.x == tuple.x && existingTuple.y == tuple.y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns if the existing Tuple and given Tuple are equal.
     * 
     * @param tuple Given Tuple
     * @return if the existing Tuple and given Tuple are equal.
     */
    public boolean tupleEquals(Tuple tuple) {
        if (this.x == tuple.x && this.y == tuple.y) {
            return true;
        }
        return false;
    }
}
