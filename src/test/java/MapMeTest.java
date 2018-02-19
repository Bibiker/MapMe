package test.java;


import main.java.com.MP.Nikita.MapMe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MapMeTest extends Assert{


    //csv (orm)


    private Map<String, String>  mapStringString;
    private Map<Double, Long>    mapDoubleLong;
    private Map<String, Integer> mapStringInteger;
    private Map<Integer, String> mapIntegerString;
    private Map<Long, Double>    mapLongDouble;

    private Map<String, String>  mapStringStringCONTROL;
    private Map<Double, Long>    mapDoubleLongCONTROL;
    private Map<String, Integer> mapStringIntegerCONTROL;
    private Map<Integer, String> mapIntegerStringCONTROL;
    private Map<Long, Double>    mapLongDoubleCONTROL;

    private String[] arrayOfString;
    private String[] arrayOfString2;
    private Integer[] arrayOfInteger;
    private Integer[] arrayOfInteger2;
    private Long[] arrayOfLong;
    private Long[] arrayOfLong2;
    private Double[] arrayOfDouble;
    private Double[] arrayOfDouble2;


    @Before
    public void initialization() {

        Map<String, String>  mapStringString = new MapMe<>();
        Map<Double, Long>    mapDoubleLong = new MapMe<>();
        Map<String, Integer> mapStringInteger = new MapMe<>();
        Map<Integer, String> mapIntegerString = new MapMe<>();
        Map<Long, Double>    mapLongDouble = new MapMe<>();

        Map<String, String>  mapStringStringCONTROL = new HashMap<>();
        Map<Double, Long>    mapDoubleLongCONTROL = new HashMap<>();
        Map<String, Integer> mapStringIntegerCONTROL = new HashMap<>();
        Map<Integer, String> mapIntegerStringCONTROL = new HashMap<>();
        Map<Long, Double>    mapLongDoubleCONTROL = new HashMap<>();

        arrayOfString =  new String[]{"Ким-Чен-Ын", "уголь", "киRпи4", "-123", "123124.24"};
        arrayOfString2 = new String[]{"", null, "\n", " ", "=-"};

        arrayOfInteger = new Integer[]{1, 2, 3, -1, 0};
        arrayOfInteger2 = new Integer[]{100, 555, 100, 0, -213};

        arrayOfLong = new Long[]{1L, 2L, 3L, -1L, 0L};
        arrayOfLong2 = new Long[]{100L, 555L, 100L, 0L, -213L};

        arrayOfDouble = new Double[]{1D, 2D, 3D, -1D, 0D};
        arrayOfDouble2 = new Double[]{100D, 555D, 100D, 0D, -213D};
    }

    @Test
    public void put() {
        sout
        mapStringStringCONTROL.put("123", "1234");
        mapStringString.put(arrayOfString[0], arrayOfString2[0]);

        for (int i = 0; i < 5; i++) {
            mapStringString.put (arrayOfString [i], arrayOfString2 [i]);
            mapDoubleLong.put   (arrayOfDouble [i], arrayOfLong    [i]);
            mapStringInteger.put(arrayOfString2[i], arrayOfInteger2[i]);
            mapIntegerString.put(arrayOfInteger[i], arrayOfString  [i]);
            mapLongDouble.put   (arrayOfLong2  [i], arrayOfDouble2 [i]);

            mapStringStringCONTROL.put (arrayOfString [i], arrayOfString2 [i]);
            mapDoubleLongCONTROL.put   (arrayOfDouble [i], arrayOfLong    [i]);
            mapStringIntegerCONTROL.put(arrayOfString2[i], arrayOfInteger2[i]);
            mapIntegerStringCONTROL.put(arrayOfInteger[i], arrayOfString  [i]);
            mapLongDoubleCONTROL.put   (arrayOfLong2  [i], arrayOfDouble2 [i]);
        }

        assertEquals(mapDoubleLong, mapDoubleLongCONTROL);
        assertEquals(mapIntegerString, mapIntegerStringCONTROL);
        assertEquals(mapLongDouble, mapLongDoubleCONTROL);
        assertEquals(mapStringInteger, mapStringIntegerCONTROL);
        assertEquals(mapStringString, mapStringStringCONTROL);
    }

    @Test
    public void size() {
    }

    @Test
    public void isEmpty() {
    }

    @Test
    public void containsKey() {
    }

    @Test
    public void containsValue() {
    }
}