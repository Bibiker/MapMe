package main.java.com.MP.Nikita;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        Map<Integer, String> testMap = new MapMe<>();

        testMap.put(1, "One");
        testMap.put(2, "Two");
        testMap.put(3, "Three");
        testMap.put(4, "Four");
        testMap.put(5, "Five");

        System.out.println("Изменение значение Four");
        testMap.put(4, "Четыре");

        System.out.println(testMap.get(4) + "\n" + testMap.containsValue("Three"));
        System.out.println(testMap.get(1));
        System.out.println(testMap.containsValue("One"));

        System.out.println("Удаление элемента с ключом 2");
        testMap.remove(2);

        for (Map.Entry<Integer, String> entry : testMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        Collection<String> lst = testMap.values();

        System.out.println("Удаление из collection элемента Five");
        lst.remove("Five");

        for (String str : lst) {
            System.out.println(str);
        }

        System.out.println("Добавление нового элемента в map, обновление значения One");
        testMap.put(6, "Six");
        testMap.put(1, "Один");

        for (Map.Entry<Integer, String> entry : testMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        for (String str : lst) {
            System.out.println(str);
        }

    }
}
