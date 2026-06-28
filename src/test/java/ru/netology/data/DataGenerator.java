package ru.netology.data;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("ru"));
    private static final String[] CITIES = {
            "Казань", "Москва", "Санкт-Петербург", "Новосибирск",
            "Екатеринбург", "Нижний Новгород", "Самара", "Омск"
    };

    private DataGenerator() {

    }

    public static String generateDate(int daysToAdd) {
        return LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateCity() {
        return CITIES[ThreadLocalRandom.current().nextInt(CITIES.length)];
    }

    public static String generateName() {
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String generatePhone() {
        return "+79" + faker.number().numberBetween(100000000L, 999999999L);
    }

    public static UserData generateUser() {
        return new UserData(
                generateCity(),
                generateName(),
                generatePhone(),
                generateDate(3)
        );
    }

    public static UserData generateUserWithDifferentDate() {
        return new UserData(
                generateCity(),
                generateName(),
                generatePhone(),
                generateDate(5)
        );
    }
}