package dion_tests.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class ProtocolPage {

    private final SelenideElement commonTab      = $(byText("Общий протокол"));
    private final SelenideElement extraTab       = $(byText("Дополнительный протокол"));

    private final SelenideElement typeH1         = $("h1.protocol-type");
    private final ElementsCollection h2Headers   = $$("h2");
    private final ElementsCollection h3Headers   = $$("h3");
    private final ElementsCollection tagBadges   = $$(".tag-badge");   // цветные теги из примера

    private final ElementsCollection tableHeaders  = $$("table thead th");

    private final SelenideElement notFoundStub   = $(".no-protocol");
    private final SelenideElement errorToast     = $(".toast-error");

    @Step("Переключаемся на вкладку «Дополнительный протокол»")
    public ProtocolPage switchToExtra() {
        extraTab.click();
        return this;
    }

    @Step("Переключаемся на вкладку «Общий протокол»")
    public ProtocolPage switchToCommon() {
        commonTab.click();
        return this;
    }


    @Step("Тип протокола = {expected}")
    public void shouldHaveType(String expected) {
        typeH1.shouldBe(visible, Duration.ofSeconds(10))
              .shouldHave(text(expected));
    }

    @Step("Должны присутствовать подзаголовки h2: {expected}")
    public void shouldContainH2(List<String> expected) {
        for (String title : expected) {
            h2Headers.findBy(text(title)).shouldBe(visible);
        }
    }

    @Step("Подзаголовок h3 «{title}» присутствует")
    public void shouldContainH3(String title) {
        h3Headers.findBy(text(title)).shouldBe(visible);
    }

    @Step("Должны присутствовать теги: {expectedTags}")
    public void shouldContainTags(List<String> expectedTags) {
        for (String tag : expectedTags) {
            tagBadges.findBy(text(tag)).shouldBe(visible);
        }
    }

    @Step("Для доп. протокола должны быть заголовки таблицы: {expected}")
    public void shouldContainTableHeaders(List<String> expected) {
        for (String th : expected) {
            tableHeaders.findBy(text(th)).shouldBe(visible);
        }
    }

    @Step("Протокол отсутствует — показана заглушка «Нет данных»")
    public void shouldShowEmptyStub() {
        notFoundStub.shouldBe(visible);
    }

    @Step("Показалась ошибка: {msg}")
    public void shouldShowError(String msg) {
        errorToast.shouldBe(visible).shouldHave(text(msg));
    }
}
