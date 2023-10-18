package ru.clevertec.streamtask.entity;

import java.util.List;
import java.util.stream.Collectors;

public class Person {
    private String name;
    private int age;
    private double weight;
    private Gender gender;
    List<Phone> phones;

    public Person(String name, int age, double weight, Gender gender, List<Phone> phones) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.gender = gender;
        this.phones = phones;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public Gender getGender() {
        return gender;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    @Override
    public String toString() {
        return name + ", age=" + age + ", weight=" + weight + ", gender=" + gender +
                ", phones=" + phones.stream().map(Phone::toString).collect(Collectors.joining(", ")) ;
    }
}
