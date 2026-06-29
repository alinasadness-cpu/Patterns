package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {
    private DataGenerator.UserInfo user;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.headless = true;
        Configuration.timeout = 15000;
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        user = DataGenerator.Registration.generateUser("ru");
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    private void clearDateField() {
        $("[data-test-id='date'] input").doubleClick();
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
    }

    private void fillFormAndSubmit(String date) {
       
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='city'] .menu-item").shouldBe(Condition.visible, Duration.ofSeconds(15)).click();

        clearDateField();
        $("[data-test-id='date'] input").setValue(date);

        $("[data-test-id='name'] input").setValue(user.getName());

        $("[data-test-id='phone'] input").setValue(user.getPhone());

        
        $("[data-test-id='agreement']").click();

       
        $(".button").click();
    }

    @Test
    @DisplayName("Should successfully reschedule meeting when date changed")
    void shouldRescheduleMeetingWhenDateChanged() {
        String firstDate = DataGenerator.generateDate(3);
        String secondDate = DataGenerator.generateDate(7);

        fillFormAndSubmit(firstDate);

        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstDate));

    
        $("[data-test-id='success-notification'] .icon-button").click();

     
        clearDateField();
        $("[data-test-id='date'] input").setValue(secondDate);

    
        $(".button").click();

        $("[data-test-id='replan-notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));

        $(byText("Перепланировать")).click();

        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondDate));
    }

    @Test
    @DisplayName("Should show validation messages for invalid data")
    void shouldShowValidationMessagesForInvalidData() {
    
        $(".button").click();

    
        $("[data-test-id='city'] .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Поле обязательно для заполнения"));

        $("[data-test-id='date'] .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Неверно введена дата"));

      
        $("[data-test-id='name'] .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Поле обязательно для заполнения"));

        $("[data-test-id='phone'] .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("Should accept valid data and plan meeting")
    void shouldPlanMeetingWithValidData() {
        String meetingDate = DataGenerator.generateDate(5);

        fillFormAndSubmit(meetingDate);

        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + meetingDate));
    }

    @Test
    @DisplayName("Should select city from dropdown")
    void shouldSelectCityFromDropdown() {
        String city = "Казань";
        String date = DataGenerator.generateDate(7);

        
        $("[data-test-id='city'] input").setValue("Ка");
        $(".menu-item__control").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $$(".menu-item__control").findBy(Condition.text(city)).click();

       
        clearDateField();
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id='agreement']").click();
        $(".button").click();

        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + date));
    }
}
