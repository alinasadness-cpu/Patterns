package ru.netology.delivery.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
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

    @Test
    @DisplayName("Should successfully reschedule meeting when date changed")
    void shouldRescheduleMeetingWhenDateChanged() {
        String firstDate = DataGenerator.generateDate(3);
        String secondDate = DataGenerator.generateDate(7);

    
        fillFormAndSubmit(firstDate);

     
        $("[data-test-id=success-notification]").shouldBe(visible);
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + firstDate));

 
        $("[data-test-id=success-notification] .icon-button").click();

  
        $(".calendar-input__native-control").setValue(secondDate);

       
        $("[data-test-id=replan] .button").click();

      
        $("[data-test-id=replan-notification]").shouldBe(visible);
        $("[data-test-id=replan-notification] .notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));

      
        $(byText("Перепланировать")).click();

        $("[data-test-id=success-notification]").shouldBe(visible);
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + secondDate));
    }

    @Test
    @DisplayName("Should show validation messages for invalid data")
    void shouldShowValidationMessagesForInvalidData() {
       
        $("[data-test-id=replan] .button").click();

       
        $("[data-test-id=city] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
        
        
        $("[data-test-id=date] .input__sub").shouldHave(text("Неверно введена дата"));
        
        $("[data-test-id=name] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
        
        $("[data-test-id=phone] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    @DisplayName("Should accept valid data and plan meeting")
    void shouldPlanMeetingWithValidData() {
        String meetingDate = DataGenerator.generateDate(5);

        fillFormAndSubmit(meetingDate);

        $("[data-test-id=success-notification]").shouldBe(visible);
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + meetingDate));
    }

    private void fillFormAndSubmit(String date) {
      
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=city] .menu-item").shouldBe(visible).click();

      
        $(".calendar-input__native-control").setValue(date);

       
        $("[data-test-id=name] input").setValue(user.getName());

      
        $("[data-test-id=phone] input").setValue(user.getPhone());

        
        $("[data-test-id=agreement] .checkbox__text").click();

      
        $("[data-test-id=replan] .button").click();
    }
}
