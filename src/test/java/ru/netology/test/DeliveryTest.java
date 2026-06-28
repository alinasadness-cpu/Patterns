package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.UserData;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {
    private UserData user;

    @BeforeAll
    static void setUpAll() {
        Configuration.browserSize = "1920x1080";
        Configuration.holdBrowserOpen = false;
    }

    @BeforeEach
    void setUp() {

        open("http://localhost:9999");

        user = DataGenerator.generateUser();
    }

    @AfterEach
    void tearDown() {

        Selenide.closeWebDriver();
    }

    @Test
    void shouldPlanAndReplanDelivery() {

        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(user.getDate());
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();


        $(byText("Запланировать")).click();


        $("[data-test-id=success-notification]").shouldBe(visible)
                .shouldHave(text("Встреча успешно запланирована на " + user.getDate()));


        UserData newUser = DataGenerator.generateUserWithDifferentDate();

        String newDate = newUser.getDate();


        $("[data-test-id=date] input").doubleClick().sendKeys(newDate);


        $(byText("Запланировать")).click();


        $("[data-test-id=replan-notification]").shouldBe(visible)
                .shouldHave(text("У вас уже запланирована встреча на другую дату"));


        $(byText("Перепланировать")).click();


        $("[data-test-id=success-notification]").shouldBe(visible)
                .shouldHave(text("Встреча успешно запланирована на " + newDate));
    }
}