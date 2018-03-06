package Utils;

import java.util.Objects;

public class Person {
    private final String firstName;
    private final String lastName;
    private final String gender;
    private final int age;


    public Person(String firstName, String lastName, String gender, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return firstName + ',' + lastName + ',' + gender + ',' + Integer.toString(age);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null ) {
            return false;
        }

        Person person = (Person) object;
        return age == person.age
                && firstName.equals(person.firstName)
                && lastName.equals(person.lastName)
                && gender.equals(person.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, gender, age);
    }
}
