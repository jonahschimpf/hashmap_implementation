import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.*;

public class HashMapTest {
    static HashMap map;

    static class Mock {
        private final int hashCode;

        public Mock() {
            hashCode = this.hashCode();
        }

        public Mock(int hashCode) {
            this.hashCode = hashCode;
        }
    }

    @Test
    public void testSingleEntry() {
        map = new HashMap(4, 1);
        Mock m = new Mock();
        Mock a = new Mock();
        map.put(2, m);
        map.put(3, a);
        assertTrue(map.containsKey(2));
        assertTrue(map.containsKey(3));
        assertFalse(map.containsKey(null));
    }

    @Test
    public void testNullEntry() {
        map = new HashMap(4, 1);
        Mock m = new Mock();
        Mock a = null;
        map.put(3, m);
        map.put(2, a);
        assertTrue(map.containsKey(2));
        assertTrue(map.containsKey(3));
        assertTrue(map.containsValue(null));
    }

    @Test
    public void testNullKey() {
        map = new HashMap(4, 1);
        Mock m = new Mock();
        Mock a = null;
        map.put(m, 3);
        map.put(a, 2);
        assertTrue(map.containsKey(a));
        assertTrue(map.containsKey(m));
        assertTrue(map.containsValue(2));
        assertTrue(map.containsValue(3));
    }

    @Test
    public void testNullKeyAndNullValue() {
        map = new HashMap(4, 1);
        Mock m = new Mock();
        Mock a = null;
        map.put(m, 3);
        map.put(a, null);
        assertTrue(map.containsKey(a));
        assertTrue(map.containsKey(m));
        assertTrue(map.containsValue(3));
        assertTrue(map.containsValue(null));
    }

    @Test
    public void testKeyHundredValues() {
        map = new HashMap(5);
        Mock[] arr = new Mock[100];
        for (int i = 0; i < 100; i++) {
            Mock m = new Mock();
            map.put(m, i);
            arr[i] = m;
        }
        for (int i = 0; i < 100; i++) {
            assertTrue(map.containsKey(arr[i]));
        }
    }

    @Test
    public void testGet() {
        Mock m = new Mock();
        map = new HashMap(5);
        map.put(m, 1);
        assertEquals(1, map.get(m));
    }

    @Test
    public void testGetWithNull() {
        Mock m = null;
        map = new HashMap(20);
        map.put(m, 1);
        assertEquals(1, map.get(m));
    }

    @Test
    public void testGetWithNullValue() {
        Mock m = null;
        map = new HashMap(20);
        map.put(1, m);
        assertNull(map.get(1));
    }

    @Test
    public void testNullKeyNullValue() {
        Mock m = null;
        map = new HashMap(20);
        assertNull(map.get(null));
    }

    @Test
    public void testNullDoesntThroughException() {
        map = new HashMap(12);
        assertFalse(map.containsKey(null));
        assertFalse(map.containsValue(null));
    }

    @Test
    public void testComplicated() {
        map = new HashMap(16);
        Mock[] arr = new Mock[256];
        Integer[] values = new Integer[256];
        for (int i = 0; i < 256; i++) {
            if (i % 3 == 0) {
                Mock m = new Mock();
                map.put(m, null);
                arr[i] = m;
                values[i] = null;
            } else if (i % 2 == 0) {
                map.put(null, i);
                arr[i] = null;
                values[i] = i;
            } else {
                Mock m = new Mock();
                map.put(m, i);
                arr[i] = m;
                values[i] = i;
            }
        }
        for (int i = 0; i < 256; i++) {
            map.containsKey(arr[i]);
            map.containsValue(values[i]);
        }
    }

    @Test
    public void testRemoval() {
        map = new HashMap(16);
        Mock[] arr = new Mock[256];
        Integer[] values = new Integer[256];
        for (int i = 0; i < 256; i++) {
            Mock m = new Mock();
            map.put(m, i);
            arr[i] = m;
            values[i] = i;
        }
        for (int i = 0; i < 256; i++) {
            assertEquals(i, map.remove(arr[i]));
        }
        for (int i = 0; i < 256; i++) {
            assertNull(map.remove(arr[i]));
        }
    }

    @Test
    public void testCollision() {
        map = new HashMap(16);
        Mock m = new Mock(1);
        Mock b = new Mock(1);
        map.put(m, 1);
        map.put(b, 2);
        assertTrue(map.containsKey(m));
        assertTrue(map.containsKey(b));
        assertTrue(map.containsValue(1));
        assertTrue(map.containsValue(2));
    }

    @Test
    public void testCollisionRemoval() {
        map = new HashMap(16);
        Mock m = new Mock(1);
        Mock b = new Mock(1);
        map.put(m, 1);
        map.put(b, 2);
        assertEquals(1, map.remove(m));
        assertFalse(map.containsKey(m));
    }

    @Test
    public void testNullCollision() {
        map = new HashMap(16);
        Mock m = new Mock(0);
        Mock b = null;
        map.put(m, 1);
        map.put(b, 2);
        assertTrue(map.containsKey(m));
        assertTrue(map.containsKey(null));
    }

    @Test
    public void testNullCollisionRem1() {
        map = new HashMap(16);
        Mock m = new Mock(0);
        Mock b = null;
        map.put(m, 1);
        map.put(b, 2);
        map.remove(b);
        assertTrue(map.containsKey(m));
        assertFalse(map.containsKey(null));
    }

    @Test
    public void testNullCollisionRem2() {
        map = new HashMap(16);
        Mock m = new Mock(0);
        Mock b = null;
        map.put(m, 1);
        map.put(b, 2);
        map.remove(m);
        assertFalse(map.containsKey(m));
        assertTrue(map.containsKey(null));
    }

    @Test
    public void test1000CollisionWithNull() {
        map = new HashMap(16);
        Mock[] arr = new Mock[1000];
        for (int i = 0; i < 1000; i++) {
            Mock m = new Mock(0);
            if (i == 30) {
                map.put(null, 1);
                arr[i] = null;
            } else {
                map.put(m, i);
                arr[i] = m;
            }
        }
        for (int i = 0; i < 1000; i++) {
            assertTrue(map.containsKey(arr[i]));
        }
        for (int i = 0; i < 1000; i++) {
            map.remove(arr[i]);
            assertFalse(map.containsKey(arr[i]));
        }
    }

    @Test
    public void addDuplicate() {
        map = new HashMap(16);
        Mock m = new Mock(0);
        map.put(m, 1);
        map.put(m, 4);
        assertEquals(4, map.get(m));
        map.remove(m);
        assertFalse(map.containsKey(m));
    }

    @Test
    public void testIterator() {
        map = new HashMap(16);
        HashMap.Entry e = new HashMap.Entry("Hi", 2, null);
        map.put(e.getKey(), e.getValue());
        Iterator it = map.entryIterator();
        assertTrue(it.hasNext());
        assertEquals(e, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testIteratorComplicated() {
        map = new HashMap(16);
        HashSet kset = new HashSet();
        for (int i = 0; i < 1028; i++) {
            int x = 1000 * i;
            HashMap.Entry e = new HashMap.Entry(i, x, null);
            kset.add(e);
            map.put(i, x);
        }
        Iterator it = map.entryIterator();
        while (it.hasNext()) {
            assertTrue(kset.contains(it.next()));
        }
    }

    @Test
    public void testIteratorHashedSame() {
        map = new HashMap(16);
        HashSet kset = new HashSet();
        for (int i = 0; i < 1028; i++) {
            Mock m = new Mock(0);
            HashMap.Entry e = new HashMap.Entry(m, i, null);
            kset.add(e);
            map.put(m, i);
        }
        Iterator it = map.entryIterator();
        while (it.hasNext()) {
            assertTrue(kset.contains(it.next()));
        }
    }

    @Test
    public void testNullRemove() {
        map = new HashMap(16);
        map.put("20", 1);
        assertNull(null, map.remove(null));
    }

    @Test
    public void testNullEndOfBucket() {
        map = new HashMap(16);
        HashSet kset = new HashSet();
        for (int i = 0; i < 1028; i++) {
            map.put(null, 200);
            Mock m = new Mock(0);
            HashMap.Entry e = new HashMap.Entry(m, i, null);
            kset.add(e);
            map.put(m, i);
        }
        assertTrue(map.containsKey(null));
        assertEquals((Integer) 200, map.remove(null));
        assertFalse(map.containsKey(null));
    }

    @Test
    public void testSize() {
        map = new HashMap(16);
        map.put(null, 200);
        for (int i = 0; i < 100; i++) {
            Mock m = new Mock(0);
            // Mock b = new Mock();
            map.put(m, i);
        }
        assertEquals(101, map.size());
    }

    @Test
    public void testClear() {
        map = new HashMap(16);
        for (int i = 0; i < 100; i++) {
            Mock m = new Mock(0);
            // Mock b = new Mock();
            map.put(m, i);
        }
        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    public void testNullAfter() {
        map = new HashMap(16);
        Mock m = new Mock(0);
        map.put(m, 14);
        map.put(null, 12);
        assertEquals(14, map.get(m));
    }

    @Test
    public void testNullAfterNotThere() {
        map = new HashMap(16);
        Mock m = new Mock(0);
        map.put(null, 12);
        assertNull(map.get(m));
    }

    @Test
    public void testAfterNull() {
        map = new HashMap(16);
        map.put(null, 12);
        Mock m = new Mock(0);
        map.put(m, 14);
        assertEquals(12, map.get(null));
    }

    @Test
    public void testAfterNullNotThere() {
        map = new HashMap(16);
        Object obj1 = new Object() {
            @Override
            public int hashCode() {
                return 0;
            }
        };

        Object obj2 = new Object() {
            @Override
            public int hashCode() {
                return 0;
            }
        };
        map.put(obj1, 1);
        map.put(obj2, 1);
        assertNull(map.get(null));
    }

    @Test
    public void testAfterNullNotThereOther() {
        map = new HashMap(16);
        Object obj1 = new Object() {
            @Override
            public int hashCode() {
                return 0;
            }
        };

        Object obj2 = new Object() {
            @Override
            public int hashCode() {
                return 0;
            }
        };
        map.put(null, 12);
        map.put(obj1, 1);
        map.put(obj2, 2);
        assertTrue(map.containsValue(1));
        assertTrue(map.containsValue(2));
        assertEquals(12, map.get(null));
    }

    @Test(expected = NoSuchElementException.class)
    public void testThrowsIt() {
        map = new HashMap(14);
        map.put("12", 12);
        Iterator it = map.entryIterator();
        it.next();
        it.next();
    }

    @Test
    public void testColGet() {
        map = new HashMap(16);
        Object obj1 = new Object() {
            @Override
            public int hashCode() {
                return 12;
            }
        };

        Object obj2 = new Object() {
            @Override
            public int hashCode() {
                return 12;
            }
        };
        map.put(null, 12);
        map.put(obj1, 20);
        map.put(obj2, 30);
        assertEquals(20, map.get(obj1));
        assertEquals(30, map.get(obj2));
        assertTrue(map.containsValue(20));
        assertTrue(map.containsValue(30));
        assertFalse(map.containsValue(100));
    }
}
