package demolition;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TupleTest {

    /**
     * Test constructor
     */
    @Test
    public void constructor() {
        assertNotNull(new Tuple (1, 1));
    }

    /**
     * Test update() method
     */
    @Test
    public void updateTest() {
        Tuple testTuple = new Tuple(2, 2);

        testTuple.update(3, 3);
        assertEquals(3, testTuple.x);
        assertEquals(3, testTuple.y);
    }

    /**
     * Test contains() method
     */
    @Test
    public void containsTest() {
        List<Tuple> testList = new ArrayList<Tuple>(
            Arrays.asList(new Tuple[] {new Tuple(1, 1), new Tuple(2, 2),
                new Tuple(3, 3)})
        );

        Tuple testTuple = new Tuple(1, 1);

        assertTrue(Tuple.contains(testList, testTuple));
    }

    /**
     * Test tupleEquals() method.
     */
    @Test
    public void tupleEqualsTest() {
        Tuple testTuple = new Tuple(1, 1);

        assertTrue(testTuple.tupleEquals(new Tuple(1, 1)));
    }

}