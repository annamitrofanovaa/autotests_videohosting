package dion_tests.pages;

import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.visible;

import java.time.Duration;

public class LoginPage {
    private final String URL = "https://frontend-test.dev.dion.vc";

    private SelenideElement emailInput = $("[name='userEmail']");
    private SelenideElement loginButton = $("button[type='submit']");
    private SelenideElement passwordInput = $("input[type='password']");
    private SelenideElement submitButton = $("button[type='submit']");

    @Step("Открываем страницу логина")
    public void openPage() {
        open(URL);
    }

    @Step("Выполняем логин с предустановленными данными")
    public void login() {
        //System.out.println("⏳ Начинаю логин...");
    
        emailInput.shouldBe(visible, Duration.ofSeconds(10)).setValue("anyatest@inno.tech");
        //System.out.println("✅ Email введён");
    
        loginButton.shouldBe(visible).click();
        //System.out.println("✅ Нажата кнопка 'Войти'");
    
        passwordInput.shouldBe(visible, Duration.ofSeconds(10)).setValue("Anna1243)");
        //System.out.println("✅ Пароль введён");
    
        submitButton.shouldBe(visible).click();
        //System.out.println("✅ Вход подтверждён");
    
        sleep(3000);
        //System.out.println("✅ Подождали немного после входа");
    }
    
    @Step("Проверяем успешность логина")
    public boolean isLoginSuccessful() {
        return !$(".error-message").exists();
    }
}
