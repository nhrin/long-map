import de.comparus.opensource.longmap.LongMapImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;


public class LongMapImplTest {
    private LongMapImpl<String> longMap;

    @Before
    public void setUp() {
        longMap = new LongMapImpl<>();
    }

    @Test
    public void testPutAndGet() {
        longMap.put(1L, "One");
        longMap.put(2L, "Two");
        longMap.put(3L, "Three");

        assertEquals("One", longMap.get(1L));
        assertEquals("Two", longMap.get(2L));
        assertEquals("Three", longMap.get(3L));
    }

    @Test
    public void testPutDuplicateKey() {
        longMap.put(1L, "One");
        longMap.put(1L, "New One");

        assertEquals("New One", longMap.get(1L));
    }

    @Test
    public void testGetNonExistingKey() {
        assertNull(longMap.get(42L));
    }

    @Test
    public void testRemove() {
        longMap.put(1L, "One");
        longMap.put(2L, "Two");

        assertEquals("Two", longMap.remove(2L));
        assertNull(longMap.get(2L));
    }

    @Test
    public void testRemoveNonExistingKey() {
        assertNull(longMap.remove(42L));
    }

    @Test
    public void testContainsKey() {
        longMap.put(1L, "One");
        longMap.put(2L, "Two");

        assertTrue(longMap.containsKey(1L));
        assertTrue(longMap.containsKey(2L));
        assertFalse(longMap.containsKey(3L));
    }

    @Test
    public void testContainsValue() {
        longMap.put(1L, "One");
        longMap.put(2L, "Two");

        assertTrue(longMap.containsValue("One"));
        assertTrue(longMap.containsValue("Two"));
        assertFalse(longMap.containsValue("Three"));
    }

    @Test
    public void testKeys() {
        longMap.put(1L, "One");
        longMap.put(2L, "Two");

        long[] keys = longMap.keys();
        assertArrayEquals(new long[]{1L, 2L}, keys);
    }

    @Test
    public void testValues() {
        longMap.put(1L, "One");
        longMap.put(2L, "Two");
        assertArrayEquals(new String[]{"One", "Two"}, longMap.values());
    }

    @Test
    public void testSize() {
        longMap.put(1L, "One");
        longMap.put(2L, "Two");

        assertEquals(2, longMap.size());

        longMap.remove(1L);
        assertEquals(1, longMap.size());

        longMap.clear();
        assertEquals(0, longMap.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(longMap.isEmpty());

        longMap.put(1L, "One");
        assertFalse(longMap.isEmpty());

        longMap.clear();
        assertTrue(longMap.isEmpty());
    }

    @Test
    public void testPutPerformance() {
        int iterations = 1000000; // The number of iterations to measure performance

        long startTime = System.nanoTime();

        // Perform the put operation multiple times
        for (int i = 0; i < iterations; i++) {
            long key = i;
            String value = "Value_" + i;
            longMap.put(key, value);
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        double avgPutTime = (double) duration / iterations;

        System.out.println("Average Put Time: " + avgPutTime + " nanoseconds");
    }

    @Test
    public void testGetPerformance() {
        int iterations = 1000000; // The number of iterations to measure performance

        // Insert elements to the map for testing
        for (int i = 0; i < iterations; i++) {
            long key = i;
            String value = "Value_" + i;
            longMap.put(key, value);
        }

        Random random = new Random();

        long startTime = System.nanoTime();

        // Perform the get operation multiple times
        for (int i = 0; i < iterations; i++) {
            long key = random.nextInt(iterations); // Choose a random key within the inserted range
            longMap.get(key);
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        double avgGetTime = (double) duration / iterations;

        System.out.println("Average Get Time: " + avgGetTime + " nanoseconds");
    }

    @Test
    public void testRemovePerformance() {
        int iterations = 1000000; // The number of iterations to measure performance

        // Insert elements to the map for testing
        for (int i = 0; i < iterations; i++) {
            long key = i;
            String value = "Value_" + i;
            longMap.put(key, value);
        }

        Random random = new Random();

        long startTime = System.nanoTime();

        // Perform the remove operation multiple times
        for (int i = 0; i < iterations; i++) {
            long key = random.nextInt(iterations); // Choose a random key within the inserted range
            longMap.remove(key);
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        double avgRemoveTime = (double) duration / iterations;

        System.out.println("Average Remove Time: " + avgRemoveTime + " nanoseconds");
    }
}
