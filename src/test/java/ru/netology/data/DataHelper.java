package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private DataHelper() {}

    private static final Faker faker = new Faker(new Locale("ru"));
    private static final Random random = new Random();

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }

    public static String generateDate(int daysToAdd) {
        return LocalDate.now().plusDays(daysToAdd)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateCity() {
        String[] cities = {"Москва", "Санкт-Петербург", "Казань", 
                          "Нижний Новгород", "Екатеринбург", "Новосибирск"};
        return cities[random.nextInt(cities.length)];
    }

    public static String generateName() {
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String generatePhone() {
        return "+7" + faker.number().digits(10);
    }

    public static UserInfo generateUserInfo() {
        return new UserInfo(generateCity(), generateName(), generatePhone());
    }
}
