import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenHashMapTest {

    private OpenHashMap map;
    @BeforeEach
    public void init(){
        //map.entries: [null, null, 2=10, 12=222, null, null, null, null, null, null]
        map = new OpenHashMap();
        map.put(2, 4);
        map.put(12, 222);
        map.put(2, 8);
        map.put(2, 10);
    }

    @Test
    public void testPut(){
        try {
            checkEntries(10, 2, 10, 0);
            checkEntries(222, 3, 10, 9);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGet(){
        assertEquals(new Long(10), map.get(2));
        assertEquals(new Long(222), map.get(12));
    }

    @Test
    public void testGetNull(){
        assertEquals(null, map.get(14));
    }

    @Test void testSize(){
        assertEquals(4, map.size());
    }

    @Test
    public void testGetIndexPositive(){
        try {
            Class[] cArg = new Class[1];
            cArg[0] = int.class;
            Method getIndex = map.getClass().getDeclaredMethod("getIndex", cArg);
            getIndex.setAccessible(true);
            Object result = getIndex.invoke(map,14);
            assertEquals(4, result);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetIndexNegative(){
        try {
            Class[] cArg = new Class[1];
            cArg[0] = int.class;
            Method getIndex = map.getClass().getDeclaredMethod("getIndex", cArg);
            getIndex.setAccessible(true);
            Object result = getIndex.invoke(map,-15);
            assertEquals(5, result);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindEmptyCell(){
        try {
            Class[] cArg = new Class[2];
            cArg[0] = int.class;
            cArg[1] = int.class;
            Method findEmptyCell = map.getClass().getDeclaredMethod("findEmptyCell", cArg);
            findEmptyCell.setAccessible(true);
            Object result = findEmptyCell.invoke(map,3, 13);
            assertEquals(4, result);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRebuildTable(){
        try {
            //map.entries before rebuild:
            // [null, null, 2=10, 12=222, null, null, null, null, null, null]
            checkCapacity(10);
            checkThreshold(7);
            checkEntries(222, 3, 10, 0);

            Method rebuildTable = map.getClass().getDeclaredMethod("rebuildTable");
            rebuildTable.setAccessible(true);
            rebuildTable.invoke(map);

            //map.entries after rebuild:
            // [null, null, 2=10, null, null, null, null, null, null, null, null, null, 12=222, null, null]
            checkCapacity(15);
            checkThreshold(11);
            checkEntries(222, 12, 15, 3);

        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private void checkCapacity(int expected) throws NoSuchFieldException, IllegalAccessException {
        Field capacityField = map.getClass().getDeclaredField("capacity");
        capacityField.setAccessible(true);
        int capacity = (int) capacityField.get(map);
        assertEquals(expected, capacity);
    }

    private void checkThreshold(int expected) throws NoSuchFieldException, IllegalAccessException {
        Field thresholdField = map.getClass().getDeclaredField("threshold");
        thresholdField.setAccessible(true);
        int threshold = (int) thresholdField.get(map);
        assertEquals(expected, threshold);
    }

    private void checkEntries(int expectedValue, int index, int expectedLength, int expectedNullIndex) throws NoSuchFieldException, IllegalAccessException {
        Field entriesField = map.getClass().getDeclaredField("entries");
        entriesField.setAccessible(true);
        Entry[] entries = (Entry[]) entriesField.get(map);
        assertEquals(expectedValue, entries[index].getValue());
        assertEquals(expectedLength, entries.length);
        assertEquals(null, entries[expectedNullIndex]);
    }
}
