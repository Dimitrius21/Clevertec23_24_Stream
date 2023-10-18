package ru.clevertec.streamtask;

import ru.clevertec.streamtask.entity.Gender;
import ru.clevertec.streamtask.entity.Person;
import ru.clevertec.streamtask.entity.Phone;

import java.util.ArrayList;
import java.util.List;

public class DataGenerator {
    private static String[][] names = {{"Ivan", "MALE"}, {"Olga", "FEMALE"}, {"Elen", "FEMALE"}, {"Kiril", "MALE"},
            {"Serge", "MALE"}, {"Liza", "FEMALE"}, {"Alisa", "FEMALE"}, {"Semen", "MALE"},
            {"Jon", "MALE"}, {"Natasha", "FEMALE"}, {"Julia", "FEMALE"}, {"Vlad", "MALE"}};

    public static List<Person> getPersons(){
        List<Person> persons = new ArrayList<>();
            for (int i=0; i< 12; i++){
                int age = 20 + (int) (Math.random()*40);
                double weight = 50 + Math.random()*30;
                int k = (int) (Math.random()*3+1);
                List<Phone> phones = new ArrayList<>();
                for (int j=0; j<=k; j++){
                    phones.add(Phone.getRandomPhone());
                }
                persons.add(new Person(names[i][0], age, weight, Gender.valueOf(names[i][1]), phones));
            }
        return persons;
    }
}
