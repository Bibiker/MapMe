package Utils;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.io.FileReader;
import java.lang.annotation.Repeatable;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(Theories.class)
public class HardMapMeTest {

    private static Map<Integer, Person> mapForCONTROL;
    private static MapMe<Integer, Person> myMap;

    @BeforeClass
    public static void setUp() throws Exception {
        FileReader fr = new FileReader("src/test/resources/listOfPersons.csv");
        Scanner scan = new Scanner(fr);

        myMap = new MapMe<>();
       // myMap.setmaxRange();

        for (int i = 0; i < 4_000_000; i++) {
            String readedLine = scan.next();
            List<String> properties = new ArrayList<>();

            for(String reval : readedLine.split(",")) {
                properties.add(reval);
            }

            Person human = new Person(properties.get(1),
                                        properties.get(2),
                                        properties.get(3),
                                        Integer.parseInt(properties.get(4)));

            myMap.put(i, human);
        }

    }
//1111
    @Test
    public void findAllMishas() {
        System.out.println("findAllMishas");

        long result = myMap.values().stream().
                filter(e -> (e.getFirstName().equals("Миша")))
                .count();
        System.out.println(result);
    }

    @Test
    public void findAllMishasParallel() {
        System.out.println("findAllMishasParallel");

        long result = myMap.values().parallelStream().
                filter(e -> (e.getFirstName().equals("Миша")))
                .count();
        System.out.println(result);
    }
//2222
    @Test
    public void getFirst20Persons() {
        System.out.println("getFirst20Persons");

        List<Map.Entry<Integer, Person>> listOfResults = myMap.entrySet().stream()
                .limit(20)
                .collect(Collectors.toList());
    }

    @Test
    public void getFirst20PersonsParallel() {
        System.out.println("getFirst20PersonsParallel");

        List<Map.Entry<Integer, Person>> listOfResults = myMap.entrySet().parallelStream()
                .limit(20)
                .collect(Collectors.toList());
    }
//3333
    @Test
    public void getFirst20PersonsSkip30() {
        System.out.println("getFirst20PersonsSkip30");

        List<Map.Entry<Integer, Person>> listOfResults = myMap.entrySet().stream()
                .skip(30)
                .limit(20)
                .collect(Collectors.toList());
    }

    @Test
    public void getFirst20PersonsSkip30Parrallel() {
        System.out.println("getFirst20PersonsSkip30Parallel");

        List<Map.Entry<Integer, Person>> listOfResults = myMap.entrySet().parallelStream()
                .skip(30)
                .limit(20)
                .collect(Collectors.toList());
    }

    @Test
    public void getInfoTable() {
        System.out.println("getInfoTable");

        Map<String, Map<Integer, List<Person>>> map = myMap.values().stream()
                .collect(Collectors.groupingBy(Person::getGender, Collectors.groupingBy(Person::getAge)));

        List<Integer> listOfResults = map.values().stream()
                .flatMap(e -> e.values().stream())
                .map((List e) -> e.size())
                .collect(Collectors.toList());

        //Для вывода значений
//        map.values().stream()
//                .flatMap(e -> e.values().stream())
//                .forEach((List<Person> e) -> System.out.println(
//                        e.get(0).getGender() + ' ' +
//                        e.get(0).getAge() + ' ' +
//                        e.size()));
    }

    @Test
    public void getInfoTableParallel() {
        System.out.println("getInfoTableParallel");

        Map<String, Map<Integer, List<Person>>> map = myMap.values().parallelStream()
                .collect(Collectors.groupingBy(Person::getGender, Collectors.groupingBy(Person::getAge)));

        List<Integer> listOfResults = map.values().parallelStream()
                .flatMap(e -> e.values().stream())
                .map((List e) -> e.size())
                .collect(Collectors.toList());

        //Для вывода значений
//        map.values().stream()
//                .flatMap(e -> e.values().stream())
//                .forEach((List<Person> e) -> System.out.println(
//                        e.get(0).getGender() + ' ' +
//                        e.get(0).getAge() + ' ' +
//                        e.size()));
    }

    @Test
    public void getMiddleAge() {
        System.out.println("getMiddleAge");

        double middle = myMap.values().stream()
                .collect(Collectors.averagingInt(Person::getAge));
        System.out.println(middle);
    }

    @Test
    public void getMiddleAgeParallel() {
        System.out.println("getMiddleAgeParallel");

        double middle = myMap.values().parallelStream()
                .collect(Collectors.averagingInt(Person::getAge));
        System.out.println(middle);
    }

    @Test
    public void getMedian() {
        System.out.println("getMedian");

        double median = myMap.values().stream()
                .mapToInt(Person::getAge)
                .sorted()
                .skip((int)((double)myMap.size() / 2 + 0.5))
                .limit((myMap.size() % 2) == 0 ? 2 : 1)
                .average().getAsDouble();
        System.out.println(median);
    }

    @Test
    public void getMedianParallel() {
        System.out.println("getMedianParallel");

        double median = myMap.values().parallelStream()
                .mapToInt(Person::getAge)
                .sorted()
                .skip((int)((double)myMap.size() / 2 + 0.5))
                .limit((myMap.size() % 2) == 0 ? 2 : 1)
                .average().getAsDouble();
        System.out.println(median);
    }

    @Test
    public void getDistribution() {
        System.out.println("getDistribution");

        Map<Integer, Long> mapDistr = myMap.values().stream()
                .collect(Collectors.groupingBy(Person::getAge, Collectors.counting()));
        System.out.println(mapDistr);
    }

    @Test
    public void getDistributionParallel() {
        System.out.println("getDistributionParallel");

        Map<Integer, Long> mapDistr = myMap.values().parallelStream()
                .collect(Collectors.groupingBy(Person::getAge, Collectors.counting()));
        System.out.println(mapDistr);
    }

    @Test
    public void percentil() {
        int percentNumber = 0;
        Map<Integer, Long> mapDistr = myMap.values().stream()
                .collect(Collectors.groupingBy(Person::getAge, Collectors.counting()));

        double percentil = mapDistr.values().stream()
                .mapToLong(Long::longValue)
                .sorted()
                .skip(mapDistr.size() / 4 * percentNumber)
                .limit(mapDistr.size() / 4)
                .average()
                .getAsDouble();
        System.out.println(percentil);
    }

    @Test
    public void percentrang() {
        Map<Integer, Long> mapDistr = myMap.values().stream()
                .collect(Collectors.groupingBy(Person::getAge,
                        Collectors.collectingAndThen(Collectors.counting(), e -> (e / myMap.size()) * 100)));

        System.out.println(mapDistr);
        System.out.println(mapDistr.values()
                .stream().reduce(0l, (a, v) -> a += v).byteValue());
    }
}