package dion_tests.tests;

import com.codeborne.selenide.Configuration;
import dion_tests.pages.RegistrationPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Epic("Регистрация")
@Feature("Страница регистрации пользователя")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class RegistrationTest {

    private RegistrationPage registrationPage;

    @BeforeAll
    static void setupSelenide() {
        Configuration.baseUrl = "https://frontend-test.dev.dion.vc";
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10_000;
    }

    @BeforeEach
    void openRegistration() {
        registrationPage = new RegistrationPage().openPage();
    }

    private String uniqueEmail() {
        return "autotest+" + System.currentTimeMillis() + "@inno.tech";
    }


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("01. Успешная регистрация нового пользователя")
    @Story("Позитивные сценарии")
    void shouldRegisterSuccessfully() {
        registrationPage
                .setName("QA-Bot")
                .setEmail(uniqueEmail())
                .setPassword("Qwerty123!")
                .confirmPassword("Qwerty123!")
                .acceptTerms()
                .submit()
                .shouldBeSuccessful();
    }

    @ParameterizedTest(name = "02.{index} Негативная регистрация с невалидным email: \"{0}\"")
    @Severity(SeverityLevel.NORMAL)
    @Story("Валидация email")
    @MethodSource("invalidEmails")
    void shouldShowErrorForInvalidEmail(String invalidEmail) {
        registrationPage
                .setName("User")
                .setEmail(invalidEmail)
                .setPassword("Valid123!")
                .confirmPassword("Valid123!")
                .acceptTerms()
                .submit()
                .shouldHaveError("Неверный формат e-mail");
    }

    static Stream<String> invalidEmails() {
        return Stream.of(
                "plainaddress",
                "@no-local-part.com",
                "user@.only-domain",
                "user@domain..double-dot"
        );
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("03. Ошибка при несовпадении паролей")
    @Story("Подтверждение пароля")
    void shouldShowErrorWhenPasswordsMismatch() {
        registrationPage
                .setName("Mismatch User")
                .setEmail(uniqueEmail())
                .setPassword("Password123!")
                .confirmPassword("Password124!")
                .acceptTerms()
                .submit()
                .shouldHaveError("Пароли не совпадают");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("04. Ошибка при невыбранном чекбоксе согласия")
    @Story("Соглашение с условиями")
    void shouldRequireTermsAcceptance() {
        registrationPage
                .setName("NoTerms User")
                .setEmail(uniqueEmail())
                .setPassword("Password123!")
                .confirmPassword("Password123!")
                .submit()
                .shouldHaveError("Необходимо принять условия");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("05. Ошибка при регистрации с уже существующим email")
    @Story("Уникальность email")
    void shouldShowErrorForDuplicateEmail() {
        String duplicated = uniqueEmail();

        registrationPage
                .setName("Original")
                .setEmail(duplicated)
                .setPassword("Password123!")
                .confirmPassword("Password123!")
                .acceptTerms()
                .submit()
                .shouldBeSuccessful();

        new RegistrationPage().openPage()
                .setName("Duplicate")
                .setEmail(duplicated)
                .setPassword("Password123!")
                .confirmPassword("Password123!")
                .acceptTerms()
                .submit()
                .shouldHaveError("Пользователь с таким e-mail уже существует");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("06. Ошибка при слишком коротком пароле")
    @Story("Валидация пароля")
    void shouldValidatePasswordStrength() {
        registrationPage
                .setName("WeakPass User")
                .setEmail(uniqueEmail())
                .setPassword("123")           // слишком коротко
                .confirmPassword("123")
                .acceptTerms()
                .submit()
                .shouldHaveError("Пароль должен содержать не менее 8 символов");
    }

    @Test
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("07. Ошибка при отправке пустой формы")
    @Story("Обязательные поля")
    void shouldValidateEmptyForm() {
        registrationPage.submit()
                        .shouldHaveError("Заполните обязательные поля");
    }
}
