package ru.netology.web;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.Condition;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class CardDeliveryTest {

    private String generateDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }


    @Test
    void shouldSendValidData() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        String planningDate = generateDate(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").sendKeys(planningDate);
        $("[data-test-id='name'] input").setValue("Василий Какин");
        $("[data-test-id='phone'] input").setValue("+79506669999");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldInvalidCityTest() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Село Нижние Пупки");
        $("[data-test-id='date'] input").setValue("22.10.2023");
        $("[data-test-id='name'] input").setValue("Василий Какин");
        $("[data-test-id='phone'] input").setValue("+79506669999");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldSkipCityTest() {
        open("http://localhost:9999");
        $("[data-test-id='date'] input").setValue("22.10.2023");
        $("[data-test-id='name'] input").setValue("Василий Какин");
        $("[data-test-id='phone'] input").setValue("+79506669999");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldInvalidDateTest() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").sendKeys("30.05.2023");
        $("[data-test-id='name'] input").setValue("Василий Какин");
        $("[data-test-id='phone'] input").setValue("+79506669999");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldInvalidNameTest() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").setValue("22.10.2023");
        $("[data-test-id='name'] input").setValue("sdfgdfg");
        $("[data-test-id='phone'] input").setValue("+79506669999");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldSkipNameTest() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").setValue("22.10.2023");
        $("[data-test-id='phone'] input").setValue("+79506669999");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldInvalidPhoneTest() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").setValue("22.10.2023");
        $("[data-test-id='name'] input").setValue("Петро Мужичков");
        $("[data-test-id='phone'] input").setValue("893453");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldSkipPhoneTest() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").setValue("22.10.2023");
        $("[data-test-id='name'] input").setValue("Петро Мужичков");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendDataWithoutAgreement() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").setValue("22.10.2023");
        $("[data-test-id='name'] input").setValue("Петро Мужичков");
        $("[data-test-id='phone'] input").setValue("+79156669999");
        $(".button").click();
        $("[data-test-id='agreement']").shouldHave(Condition.cssClass("input_invalid"));
    }

}
