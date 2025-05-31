package dion_tests.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class RegistrationPage {

    private static final String URL = "https://frontend-test.dev.dion.vc/signup";

    private final SelenideElement nameInput            = $("[name='userName']");
    private final SelenideElement emailInput           = $("[name='userEmail']");
    private final SelenideElement passwordInput        = $("[name='password']");
    private final SelenideElement confirmPasswordInput = $("[name='confirmPassword']");
    private final SelenideElement termsCheckbox        = $("[name='acceptTerms']");
    private final SelenideElement submitButton         = $("button[type='submit']");
    private final SelenideElement successToast         = $(".toast-success");
    private final SelenideElement errorMessageBox      = $(".form-error"); // общая зона ошибок

    @Step("Открываем страницу регистрации")
    public RegistrationPage openPage() {
        open(URL);
        return this;
    }

    @Step("Заполняем имя: {name}")
    public RegistrationPage setName(String name) {
        nameInput.shouldBe(visible, Duration.ofSeconds(10)).setValue(name);
        return this;
    }

    @Step("Заполняем email: {email}")
    public RegistrationPage setEmail(String email) {
        emailInput.shouldBe(visible).setValue(email);
        return this;
    }

    @Step("Задаём пароль")
    public RegistrationPage setPassword(String password) {
        passwordInput.shouldBe(visible).setValue(password);
        return this;
    }

    @Step("Повторяем пароль")
    public RegistrationPage confirmPassword(String password) {
        confirmPasswordInput.shouldBe(visible).setValue(password);
        return this;
    }

    @Step("Принимаем условия использования")
    public RegistrationPage acceptTerms() {
        termsCheckbox.scrollTo().click();
        return this;
    }

    @Step("Нажимаем кнопку «Зарегистрироваться»")
    public RegistrationPage submit() {
        submitButton.shouldBe(enabled).click();
        return this;
    }

    @Step("Проверяем, что регистрация успешна")
    public void shouldBeSuccessful() {
        successToast.shouldBe(visible, Duration.ofSeconds(10))
                    .shouldHave(text("успешно"));
    }

    @Step("Проверяем наличие ошибки: {expected}")
    public void shouldHaveError(String expected) {
        errorMessageBox.shouldBe(visible, Duration.ofSeconds(10))
                       .shouldHave(text(expected));
    }
}
