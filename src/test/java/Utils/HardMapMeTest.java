package Utils;

import org.junit.*;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class HardMapMeTest {

    private Map<Integer, Person> mapForCONTROL;
    private Map<Integer, Person> myMap;

    @Before
    public void setUp() throws Exception {
        FileReader fr = new FileReader("src/test/resources/listOfPersons.csv");
        Scanner scan = new Scanner(fr);
        mapForCONTROL = new TreeMap<>();
        myMap = new TreeMap<>();

        for (int i = 0; i < 10_000; i++) {
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
            //mapForCONTROL.put(i,human);

        }

    }
//1111
    @Test
    public void findAllMishas() {
        long result = myMap.entrySet().stream().
                                        filter(e -> (e.getValue().getFirstName() == "Миша"))
                                        .count();
        System.out.println("Количество персон с firstname Миша = " + result);
    }

    @Test
    public void findAllMishasParallel() {
        long result = myMap.entrySet().parallelStream()
                                        .filter(e -> e.getValue().getFirstName() == "Миша")
                                        .count();
        System.out.println("Количество персон с firstname Миша = " + result);
    }
//2222
    @Test
    public void getFirst20Persons() {
        List<Map.Entry<Integer, Person>> listOfResults = myMap.entrySet().stream()
                .limit(20)
                .collect(Collectors.toList());
        System.out.println(listOfResults.toArray());
    }

    @Test
    public void getFirst20PersonsParallel() {
        List<Map.Entry<Integer, Person>> listOfResults = myMap.entrySet().parallelStream()
                .limit(20)
                .collect(Collectors.toList());
        System.out.println(listOfResults.toArray());
    }
//3333
    @Test
    public void getFirst20PersonsSkip30() {
        List<Map.Entry<Integer, Person>> listOfResults = myMap.entrySet().stream()
                .skip(30)
                .limit(20)
                .collect(Collectors.toList());
        System.out.println(listOfResults);
    }

    @Test
    public void getFirst20PersonsSkip30Parrallel() {
        List<Map.Entry<Integer, Person>> listOfResults = myMap.entrySet().parallelStream()
                .skip(30)
                .limit(20)
                .collect(Collectors.toList());
        System.out.println(listOfResults);
    }

    @Test
    public void getInfoTable() {

    }

    @Test
    public void getMiddleAge() {


    }

}