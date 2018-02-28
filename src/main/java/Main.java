import Utils.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main  {

    public static void main(String[]args) throws Exception {

        List<String> listOfFirstNames = new ArrayList<>(30),
                listOfSecondNames = new ArrayList<>(30),
                listOfGender = new ArrayList<>(2);

        listOfFirstNames.add("Миша");
        listOfFirstNames.add("Егор");
        listOfFirstNames.add("Андрей");
        listOfFirstNames.add("Антон");
        listOfFirstNames.add("Кирилл");
        listOfFirstNames.add("Олег");
        listOfFirstNames.add("Никита");
        listOfFirstNames.add("Стас");
        listOfFirstNames.add("Слава");
        listOfFirstNames.add("Игнат");
        listOfFirstNames.add("Рома");
        listOfFirstNames.add("Захар");
        listOfFirstNames.add("Данил");
        listOfFirstNames.add("Игорь");
        listOfFirstNames.add("Тимур");

        listOfFirstNames.add("Света");
        listOfFirstNames.add("Наташа");
        listOfFirstNames.add("Аня");
        listOfFirstNames.add("Алёна");
        listOfFirstNames.add("Настя");
        listOfFirstNames.add("Роза");
        listOfFirstNames.add("Рита");
        listOfFirstNames.add("Вика");
        listOfFirstNames.add("Кристина");
        listOfFirstNames.add("Люда");
        listOfFirstNames.add("Галя");
        listOfFirstNames.add("Маша");
        listOfFirstNames.add("Алиса");
        listOfFirstNames.add("Лиза");
        listOfFirstNames.add("Полина");
/////////////////////////////////////////////////////////
        listOfSecondNames.add("Шувалов");
        listOfSecondNames.add("Дудин");
        listOfSecondNames.add("Петров");
        listOfSecondNames.add("Иванов");
        listOfSecondNames.add("Путин");
        listOfSecondNames.add("Петренко");
        listOfSecondNames.add("Сидоров");
        listOfSecondNames.add("Наговицын");
        listOfSecondNames.add("Мальцов");
        listOfSecondNames.add("Довлатов");
        listOfSecondNames.add("Хорьков");
        listOfSecondNames.add("Беляев");
        listOfSecondNames.add("Смирнов");
        listOfSecondNames.add("Киселёв");
        listOfSecondNames.add("Егоров");

        listOfSecondNames.add("Журавлёва");
        listOfSecondNames.add("Петрова");
        listOfSecondNames.add("Шумихина");
        listOfSecondNames.add("Иванова");
        listOfSecondNames.add("Шкляева");
        listOfSecondNames.add("Зубина");
        listOfSecondNames.add("Хорькова");
        listOfSecondNames.add("Беляева");
        listOfSecondNames.add("Перевозчикова");
        listOfSecondNames.add("Шутова");
        listOfSecondNames.add("Безносова");
        listOfSecondNames.add("Суворова");
        listOfSecondNames.add("Мухаметзянова");
        listOfSecondNames.add("Сидорова");
        listOfSecondNames.add("Егорова");

        listOfGender.add("male");
        listOfGender.add("female");

        FileWriter fw = new FileWriter("listOfPersons.csv");
        Random random = new Random();
        //BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("listOfPersons.csv"), "cp1251"));

        for (int i = 0; i < 5_000_000; i++) {
            int shift = 0;
            String gender = listOfGender.get(random.nextInt(2));
            if (gender == "male") {
                shift = 0;
            } else {
                shift = 15;
            }

            Person human = new Person(listOfFirstNames.get(shift + random.nextInt(15)),
                    listOfSecondNames.get(shift + random.nextInt(15)),
                    gender, 18 + random.nextInt(82));
            //out.append(human.toString() + '\n');
            fw.write(Integer.toString(i)+ ',' + human.toString() + '\n');
        }
        //out.close();
        fw.close();

        FileReader fr = new FileReader("listOfPersons.csv");
        Scanner scanner = new Scanner(fr);
        for (int i = 0; i < 5; i++) {

            System.out.println(scanner.nextLine());
        }
    }

}
