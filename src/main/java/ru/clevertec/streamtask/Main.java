package ru.clevertec.streamtask;


import ru.clevertec.streamtask.entity.Gender;
import ru.clevertec.streamtask.entity.Operator;
import ru.clevertec.streamtask.entity.Person;
import ru.clevertec.streamtask.entity.Phone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.stream.Collectors.teeing;

public class Main {
    public static void main(String[] args) throws IOException {

        List<Person> persons = DataGenerator.getPersons();
        persons.stream().forEach(System.out::println);
        System.out.println("-----------------------------------------------------------------");

        int age = 30;
        double weight = 65;
        System.out.println("1.  Получите список Person и отфильтруйте только те, у которых age > n и выведите в консоль.");
        persons.stream().filter(p -> p.getAge() > age).forEach(System.out::println);
        System.out.println("-----------------------------------------------------------------");

        System.out.println("2.  Получите список Person, отфильтруйте только те, у кого weight > n, преобразуйте в name и выведите в консоль.");
        persons.stream().filter(p -> p.getAge() > age).map(Person::getName).forEach(System.out::println);
        System.out.println("-----------------------------------------------------------------");

        System.out.println("3.  Получите список Person, отфильтруйте только те, у кого кол-во телефонов > n, преобразуйте в номера телефонов и выведите в консоль.");
        int phoneQtity = 2;
        persons.stream().filter(p -> p.getPhones().size() > phoneQtity)
                .flatMap(p -> p.getPhones().stream())
                .forEach(System.out::println);
        System.out.println("-----------------------------------------------------------------");

        System.out.println("4.  Получите список Person, преобразуйте в name и затем преобразуйте в строку, что бы имена были через запятую.");
        String personsName = persons.stream().map(Person::getName).collect(Collectors.joining(", "));
        System.out.println(personsName);
        System.out.println("-----------------------------------------------------------------");

        System.out.println("5.  Получите список Person и отсортируйте их по возрасту в порядке убывания, если возраст равен, то по именам и выведите в консоль.");
        persons.stream().sorted(Comparator.comparingInt(Person::getAge).reversed().thenComparing(Person::getName))
                .forEach(System.out::println);
        System.out.println("-----------------------------------------------------------------");

        System.out.println("6.  Получите список Person и сгруппируйте их по полу.");
        Map<Gender, List<Person>> genderMap = persons.stream().collect(Collectors.groupingBy(Person::getGender));
        String res = "Male: " + genderMap.get(Gender.MALE).stream().map(Person::getName).collect(Collectors.joining(", "))
                + "\n" + "Female: " + genderMap.get(Gender.FEMALE).stream().map(Person::getName).collect(Collectors.joining(", "));
        System.out.println(res);
        System.out.println("-----------------------------------------------------------------");

        System.out.println("7.  Получите список Person и проверьте есть ли в этом списке человек, у которого номер телефона N значению.");
        String phoneNumber = persons.get(2).getPhones().get(0).getNumber();
        boolean hasPhoneNumber = persons.stream().flatMap(p -> p.getPhones().stream())
                .map(phone -> phone.getNumber())
                .anyMatch(number -> phoneNumber.equals(number));
        System.out.println(hasPhoneNumber);
        System.out.println("-----------------------------------------------------------------");

        System.out.println("8.  Получите список Person, получите n по порядку человека и получите операторов его телефонов исключая дублирование.");
        int[] i = {-1};
        Integer n = 3;
        String operators = persons.stream()
                .filter(p -> {
                    i[0]++;
                    return i[0] == n;
                })
                .flatMap(p -> p.getPhones().stream())
                .map(ph -> ph.getOperator().toString()).distinct()
                .collect(Collectors.joining(", "));
        System.out.println(operators);
        System.out.println("-----------------------------------------------------------------");

        System.out.println("9.  Получите список Person и получите их средний вес.");
        double avrWeight = persons.stream().mapToDouble(Person::getWeight).average().orElse(0);
        System.out.println(avrWeight);
        System.out.println("-----------------------------------------------------------------");

        System.out.println("10. Получние список Person и найдите самого младшего по возрасту.");
        Person person = persons.stream().sorted(Comparator.comparingInt(Person::getAge))
                .limit(1)
                .toList().get(0);
        System.out.println(person);
        System.out.println("-----------------------------------------------------------------");


        System.out.println("11. Получние список Person, получите их телефоны, сгруппируйте по оператору и рузальтатом группировки должны быть только номера телефонов.");
        Map<Operator, List<String>> phones = persons.stream()
                .flatMap(p -> p.getPhones().stream())
                .collect(Collectors.groupingBy(Phone::getOperator, Collectors.mapping(Phone::getNumber, Collectors.toList()))); //, Phone::getNumber));
        res = "";
        for (Operator operator : phones.keySet()) {
            res += (operator + ": " + phones.get(operator).stream().collect(Collectors.joining(", ")) + "\n");
        }
        System.out.println(res);
        System.out.println("-----------------------------------------------------------------");

        System.out.println("12. Получние список Person, сгруппируйте их по полу и результатом группировки должно быть их кол-во.");
        Map<Gender, Long> genderQtity = persons.stream().collect(Collectors.groupingBy(Person::getGender, Collectors.counting()));
        res = "";
        res = "Male: " + genderQtity.get(Gender.MALE)
                + "\n" + "Female: " + genderQtity.get(Gender.FEMALE);
        System.out.println(res);
        System.out.println("-----------------------------------------------------------------");

        System.out.println("13. Прочтите содержимое текстового файла и сделайте из него частотный словарик. (слово -> и какое кол-во раз это слово встречается в нём)");
        Map<String, Long> frequence = Files.readAllLines(Paths.get("text.txt")).stream()
                .flatMap(st -> Arrays.stream(st.split("[(,?\\s+) | (\\.\\s+)]")))
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        frequence.keySet().stream().forEach(k -> System.out.println(k + " : " + frequence.get(k)));

        System.out.println("-----------------------------------------------------------------");

        System.out.println("14. Получите список дат и найдите количество дней между первой и последней датой.");
        LocalDate[] dates = {LocalDate.of(2023, 5, 5), LocalDate.of(2023, 6, 25),
                LocalDate.of(2023, 7, 25), LocalDate.of(2023, 5, 25)};

        long period = Arrays.stream(dates).skip(dates.length - 1)
                .map(it -> DAYS.between(dates[0], it))
                .findFirst().get();
        System.out.println(period + "days");
        System.out.println("-----------------------------------------------------------------");

        System.out.println("15. Получите список строк, преобразуйте их в числа, и посчитайте среднее значение (не забудьте отфильтровать не валидные строки)");
        String[] strings = {"3", "as", "8", "5", "df"};
        Double avr = Arrays.stream(strings).filter(it -> {
                    try {
                        Double.parseDouble(it);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }).mapToDouble(Double::parseDouble)
                .average().orElse(0);
        System.out.println(avr);
        System.out.println("-----------------------------------------------------------------");

        System.out.println("16. Сгенерируйте миллион рандомных чисел и посчитайте их сумму используя parallelStream с двумя потоками.");
        List<Double> largeDataset = new ArrayList<>(1000000);
        IntStream.range(1, 1000000).forEach(l -> {
            largeDataset.add(Math.random() * 1000);
        });
        ForkJoinPool myPool = new ForkJoinPool(2);
        Double sum = largeDataset.parallelStream().reduce(Double::sum).get();
        System.out.println(sum);
        System.out.println("-----------------------------------------------------------------");
//-------------------------------------------------------------------------------------------------------------------------
//Когда-то вот такое задание делал:)
        Collection<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        // Прибавить к числам 3 и получить статистику: количество элементов, их сумму, макс и мин. значения, а также их среднее.
        String statistics = numbers.stream()
                .collect(teeing(
                        teeing(
                                teeing(Collectors.counting(), Collectors.summingInt(el -> el + 3), (el1, el2) -> "count=" + el1 + ", sum=" + el2),
                                teeing(Collectors.minBy(Integer::compareTo), Collectors.averagingDouble(el -> el + 3), (el1, el2) -> "min=" + (el1.get() + 3) + ", average=" + el2),
                                (el1, el2) -> el1 + ", " + el2),
                        Collectors.maxBy(Integer::compareTo), (el1, el2) -> "{" + el1 + ", " + "max=" + (el2.get() + 3) + "}"));
        // напечатает statistics = {count=4, sum=22, min=4, average=5.500000, max=7}
        System.out.println("statistics = " + statistics);
    }
}