package main.java.com.MP.Nikita;

import java.sql.SQLOutput;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Map<Integer, String> testMap = new MapMe<>();

        testMap.put(1, "One");
        testMap.put(2, "Two");
        testMap.put(3, "Three");
        testMap.put(4, "Four");
        testMap.put(5, "Five");

        testMap.put(4, "Четыре");

        System.out.println(testMap.get(4) + "\n" + testMap.containsValue("Three"));

    }
}
