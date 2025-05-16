package dion_tests.utils;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;

import dion_tests.pages.LoginPage;
import io.qameta.allure.selenide.AllureSelenide;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {
    protected LoginPage loginPage;
    
    @BeforeAll
    public void setUp(TestInfo testInfo) {
        System.out.println("🔥 [BaseTest] ➤ setUp вызван для теста: " + testInfo.getDisplayName());
        System.out.println("[BaseTest] setUp начался");
        // 1) Регистрируем Allure-Selenide-лисенер
        SelenideLogger.addListener("AllureSelenide", 
            new AllureSelenide()
                .screenshots(true)     
                .savePageSource(false) 
        );



        
        // 🔐 Запуск Chrome с реальным кэшем и пользовательским профилем
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:/temp/test-profile");
//options.addArguments("user-data-dir=C:/Users/22ami/AppData/Local/Google/Chrome/User Data");
        //options.addArguments("--profile-directory=Default");
       

        // 🔕 Отключаем уведомления и предложения сохранить пароль
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        Configuration.browserCapabilities = options;
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10000;

        System.out.println("[BaseTest] Открыта стартовая страница");

        loginPage = new LoginPage();
        loginPage.openPage();
        loginPage.login();

        System.out.println("[BaseTest] Авторизация завершена");
        //sleep(3000); // дожидаемся завершения авторизации


    }

    @AfterAll
    public void logout() {
        System.out.println("🔚 [BaseTest] Выход из профиля после теста");

        // Клик по кнопке "меню" (аватар пользователя)
        $("[aria-label='menu']").scrollIntoView(true).shouldBe(visible).click();
        Selenide.sleep(1000);
        $(byText("Выход из профиля")).shouldBe(visible).click();
        Selenide.sleep(3000);
        System.out.println("[Logout] ✅ Выход выполнен");
    }
}
