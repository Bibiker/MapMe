package Utils;

import org.junit.*;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class MapMeTest {

    private Map<Integer, String> mapIntStr, mapIntStrCONTROL;

    @Before
    public void setUp() {
        mapIntStr = new MapMe<>();
        mapIntStrCONTROL = new TreeMap<>();

        Integer inputInt;
        String inputStr;

        inputInt = 1; inputStr = "";
        mapIntStr.put(inputInt, inputStr);
        mapIntStrCONTROL.put(inputInt, inputStr);

        inputInt = 3435; inputStr = null;
        mapIntStr.put(inputInt, inputStr);
        mapIntStrCONTROL.put(inputInt, inputStr);

        inputInt = -2; inputStr = "\n";
        mapIntStr.put(inputInt, inputStr);
        mapIntStrCONTROL.put(inputInt, inputStr);

        inputInt = 11111111; inputStr = "888";
        mapIntStr.put(inputInt, inputStr);
        mapIntStrCONTROL.put(inputInt, inputStr);

        inputInt = 0; inputStr = ".-=";
        mapIntStr.put(inputInt, inputStr);
        mapIntStrCONTROL.put(inputInt, inputStr);

        assertTrue(mapIntStrCONTROL.equals(mapIntStr));
    }

//PUT
    @Test
    public void test_put_InEmptyMap() {
        Integer inputInt = 7;
        String inputStr = "name";
        mapIntStr = new MapMe<>();
        mapIntStrCONTROL = new TreeMap<>();

        mapIntStr.put(inputInt, inputStr);
        mapIntStrCONTROL.put(inputInt, inputStr);

        assertTrue(mapIntStrCONTROL.equals(mapIntStr));
    }

    @Test
    public void test_put() {
        Integer inputInt = 7;
        String inputStr = "name";

        mapIntStr.put(inputInt, inputStr);
        mapIntStrCONTROL.put(inputInt, inputStr);

        assertTrue(mapIntStrCONTROL.equals(mapIntStr));
    }

    @Test (expected = NullPointerException.class)
    public void test_put_NullPointerException() {
        mapIntStr.put(null,"");
    }
//ENDPUT//////////////////////////////////////////////

//GET
    @Test
    public void test_get_FirstElement() {
        assertEquals(mapIntStrCONTROL.get(1), mapIntStr.get(1));
    }

    @Test
    public void test_get_LastElement() {
        assertEquals(mapIntStrCONTROL.get(0), mapIntStr.get(0));
    }

    @Test
    public void test_get_IntermediateElement() {
        assertEquals(mapIntStrCONTROL.get(-2), mapIntStr.get(-2));
    }

    @Test
    public void test_get_NullReturn() {
        assertEquals(mapIntStrCONTROL.get(100), mapIntStr.get(100));
    }
//ENDGET//////////////////////////////////////////////

//SIZE
    @Test
    public void test_size() {
        assertEquals(mapIntStrCONTROL.size(), mapIntStr.size());
        mapIntStr = new MapMe();
        assertEquals(0, mapIntStr.size());
    }
//ENDSIZE/////////////////////////////////////////////

//CONTAINS
    @Test
    public void test_containsValue() {
        assertEquals(mapIntStrCONTROL.containsValue(null), mapIntStr.containsValue(null));
    }

    @Test
    public void test_containsKey() {
        assertEquals(mapIntStrCONTROL.containsKey(100), mapIntStr.containsKey(100));
    }

    @Test (expected = NullPointerException.class)
    public void test_containsKey_NullPointerException() {
        mapIntStr.containsKey(null);
    }
//ENDCONTAINS/////////////////////////////////////////

//REMOVE
    @Test
    public void test_remove_FirstElement() {
        assertEquals(mapIntStrCONTROL.remove(1), mapIntStr.remove(1));
    }

    @Test
    public void test_remove_LastElement() {
        assertEquals(mapIntStrCONTROL.remove(0), mapIntStr.remove(0));
    }

    @Test
    public void test_remove_IntermediateElement() {
        assertEquals(mapIntStrCONTROL.remove(-2), mapIntStr.remove(-2));
    }

    @Test
    public void test_remove_NullReturn() {
        assertEquals(mapIntStrCONTROL.remove(100), mapIntStr.remove(100));
    }
//ENDREMOVE//////////////////////////////////////////

//PUTALL
    @Test
    public void test_putAll() {
        Map<Integer, String> mapToAdd = new TreeMap<>();
        mapToAdd.put(18, "18");
        mapToAdd.put(201, "201");
        mapToAdd.put(1, "1");

        mapIntStrCONTROL.putAll(mapToAdd);
        mapIntStr.putAll(mapToAdd);

        assertTrue(mapIntStrCONTROL.equals(mapIntStr));
    }

    @Test (expected = NullPointerException.class)
    public void test_putAllNullPointerException() {
        mapIntStr.putAll(null);
    }

    @Test
    public void test_putAll_Itself() {
        mapIntStr.putAll(mapIntStr);
    }
//ENDPUTALL/////////////////////////////////////////

//CLEAR
    @Test
    public void test_clear() {
        mapIntStr.clear();
        mapIntStrCONTROL.clear();

        assertEquals(mapIntStrCONTROL.size(), mapIntStr.size());
        assertEquals(mapIntStrCONTROL.isEmpty(), mapIntStr.isEmpty());
    }
//ENDCLEAR//////////////////////////////////////////

//ISEMPTY

    @Test
    public void test_isEmpty() {
        assertEquals(mapIntStr.isEmpty(), mapIntStrCONTROL.isEmpty());
    }

    @Test
    public void test_isEmpty_justOneElement() {
        mapIntStr.clear();
        mapIntStrCONTROL.clear();

        Integer inputInt = 5;
        String inputStr = "str";

        mapIntStr.put(inputInt, inputStr);
        mapIntStrCONTROL.put(inputInt, inputStr);

        assertEquals(mapIntStr.isEmpty(), mapIntStrCONTROL.isEmpty());
    }

//ENDISEMPTY////////////////////////////////////////

//KEYSET
    @Test
    public void test_keySet() {
        assertEquals(mapIntStrCONTROL.keySet(), mapIntStr.keySet());
    }

    @Test
    public void test_keySet_put() {
        Set<Integer> setofKeysCONTROL = mapIntStrCONTROL.keySet();
        Set<Integer> setofKeys = mapIntStr.keySet();

        Integer inputInt = 54;
        String inputStr = "54";
        mapIntStr.put(inputInt, inputStr);
        mapIntStrCONTROL.put(inputInt, inputStr);

        assertEquals(6, setofKeys.size());
        assertEquals(setofKeysCONTROL, setofKeys);
    }

    @Test
    public void test_keySet_removeFromSet() {
        Set<Integer> setofKeysCONTROL = mapIntStrCONTROL.keySet();
        Set<Integer> setofKeys = mapIntStr.keySet();

        setofKeys.remove(-2);
        setofKeysCONTROL.remove(-2);

        assertEquals(setofKeys, setofKeysCONTROL);
    }

    @Test
    public void test_keySet_removeFromMap() {
        Set<Integer> setofKeysCONTROL = mapIntStrCONTROL.keySet();
        Set<Integer> setofKeys = mapIntStr.keySet();

        mapIntStr.remove(-2);
        mapIntStrCONTROL.remove(-2);

        assertEquals(4, setofKeys.size());
        assertEquals(setofKeys, setofKeysCONTROL);
    }

    @Test
    public void test_keySet_contains() {
        Set<Integer> setofKeysCONTROL = mapIntStrCONTROL.keySet();
        Set<Integer> setofKeys = mapIntStr.keySet();

        Integer inputInt = -2;
        assertEquals(setofKeys.contains(inputInt), setofKeysCONTROL.contains(inputInt));

        inputInt = 100;
        assertEquals(setofKeys.contains(inputInt), setofKeysCONTROL.contains(inputInt));
    }
//ENDKEYSET/////////////////////////////////////////

//ENTRYSET
    @Test
    public void test_entrySet() {
        assertEquals(mapIntStrCONTROL.entrySet(), mapIntStr.entrySet());
    }

    @Test
    public void test_entrySet_put() {
        Set<Map.Entry<Integer, String>> setofEntriesCONTROL = mapIntStrCONTROL.entrySet();
        Set<Map.Entry<Integer, String>> setofEntries = mapIntStr.entrySet();

        Integer inputInt = 54;
        String inputStr = "54";
        mapIntStr.put(inputInt, inputStr);
        mapIntStrCONTROL.put(inputInt, inputStr);

        assertEquals(6, setofEntries.size());
        assertEquals(setofEntriesCONTROL, setofEntries);
    }

    @Test
    public void test_entrySet_removeFromSet() {
        Set<Map.Entry<Integer, String>> setofEntriesCONTROL = mapIntStrCONTROL.entrySet();
        Set<Map.Entry<Integer, String>> setofEntries = mapIntStr.entrySet();

        MapMe.Entry<Integer, String> mapEntry = new MapMe.Entry<>(-2, "\n", null, null);
        setofEntries.remove(mapEntry);
        setofEntriesCONTROL.remove(mapEntry);

        assertEquals(setofEntriesCONTROL, setofEntries);
    }

    @Test
    public void test_entrySet_removeFromMap() {
        Set<Map.Entry<Integer, String>> setofEntriesCONTROL = mapIntStrCONTROL.entrySet();
        Set<Map.Entry<Integer, String>> setofEntries = mapIntStr.entrySet();

        mapIntStr.remove(-2);
        mapIntStrCONTROL.remove(-2);

        assertEquals(4, setofEntries.size());
        assertEquals(setofEntriesCONTROL, setofEntries);
    }

    @Test
    public void test_entrySet_contains() {
        Set<Map.Entry<Integer, String>> setofEntriesCONTROL = mapIntStrCONTROL.entrySet();
        Set<Map.Entry<Integer, String>> setofEntries = mapIntStr.entrySet();

        Integer inputInt = -2;
        assertEquals(setofEntries.contains(inputInt), setofEntriesCONTROL.contains(inputInt));

        inputInt = 100;
        assertEquals(setofEntries.contains(inputInt), setofEntriesCONTROL.contains(inputInt));
    }
//ENDENTRYSET///////////////////////////////////////

//VALUES////////////////////////////////////////////

    @Test
    public void test_values() {
        Collection<String> values = mapIntStr.values();
        Collection<String> valuesCONTROL = mapIntStrCONTROL.values();

        assertTrue(values.containsAll(valuesCONTROL));
        assertTrue(valuesCONTROL.containsAll(values));
    }

    @Test
    public void test_values_put() {
        Collection<String> values = mapIntStr.values();
        Collection<String> valuesCONTROL = mapIntStrCONTROL.values();

        Integer inputInt = 1444;
        String inputStr = "input String";
        mapIntStr.put(inputInt, inputStr);
        mapIntStrCONTROL.put(inputInt, inputStr);

        assertEquals(6, values.size());
        assertTrue(values.containsAll(valuesCONTROL));
        assertTrue(valuesCONTROL.containsAll(values));
    }

    @Test
    public void test_values_removeFromCollection() {
        Collection<String> values = mapIntStr.values();
        Collection<String> valuesCONTROL = mapIntStrCONTROL.values();

        values.remove("");
        valuesCONTROL.remove("");

        Object[] arr = values.toArray();
        Object[] arr2 = valuesCONTROL.toArray();

        assertEquals(4, values.size());
        assertTrue(values.containsAll(valuesCONTROL));
        assertTrue(valuesCONTROL.containsAll(values));
    }

    @Test
    public void test_values_removeFromMap() {
        Collection<String> values = mapIntStr.values();
        Collection<String> valuesCONTROL = mapIntStrCONTROL.values();

        mapIntStr.remove(-2);
        mapIntStrCONTROL.remove(-2);

        assertEquals(4, values.size());
        assertTrue(values.containsAll(valuesCONTROL));
        assertTrue(valuesCONTROL.containsAll(values));
    }

//ENDVALUES/////////////////////////////////////////


    @Test
    public void test_putting() {
        long start;
        long finish;

//        start = System.currentTimeMillis();
//
//        for (int i = 0; i < 100_000; i++) {
//            mapIntStr.put(i, "213");
//        }
//        finish = System.currentTimeMillis();
//
//        System.out.println("Время заполнения моей мапы: " + (finish - start));

        Map<Integer, String> mapIntStrForSerch = new TreeMap<>();

        for (int i = 0; i < 10_000_000; i++) {
            mapIntStrCONTROL.put(i, "213");
        }

        start = System.currentTimeMillis();
        for (Map.Entry<Integer, String> element : mapIntStrCONTROL.entrySet()){
            if (element.getValue().length() < 2)  {
                mapIntStrForSerch.put(element.getKey(), element.getValue());
            }
        }
        finish = System.currentTimeMillis();

        System.out.println("Время заполнения TreeMap: " + (finish - start));
        System.out.println("Результат выполнения: ");
        for (Map.Entry<Integer, String> element : mapIntStrForSerch.entrySet()){
            System.out.println(element.getKey() + ": " + element.getValue());
        }
//
//        start = System.currentTimeMillis();
//        mapIntStrForSerch.putAll(mapIntStrCONTROL.entrySet().stream().
//                filter(s -> s.getValue().length() < 2).collect());
//        finish = System.currentTimeMillis();

    }



//ENDITERATOR///////////////////////////////////////
}