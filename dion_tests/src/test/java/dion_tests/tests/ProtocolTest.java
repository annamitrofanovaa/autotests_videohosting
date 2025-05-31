package dion_tests.tests;

import com.codeborne.selenide.Configuration;
import dion_tests.pages.LoginPage;
import dion_tests.pages.ProtocolPage;
import dion_tests.pages.VideoPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Epic("Протоколы")
@Feature("Проверка структуры протоколов у видео")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class ProtocolTest {

    private static final Map<String, String> VIDEO_BY_TEMPLATE = Map.of(
            "sobsedovanie", "a1b2c3d4",  // интервью кандидата
            "commercial",    "b2c3d4e5"   // переговоры / презентация
    );

    private final VideoPage videoPage = new VideoPage();

    @BeforeAll
    static void auth() {
        Configuration.browserSize = "1920x1080";
        new LoginPage().openPage().login();
    }


    @ParameterizedTest(name = "01.{index} Проверяем общий протокол «{0}»")
    @MethodSource("commonTemplates")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Общий протокол")
    void shouldDisplayCommonProtocol(
            String templateCode,
            String expectedType,
            List<String> h2Headers,
            List<String> expectedTags) {

        ProtocolPage protocol = videoPage.open(VIDEO_BY_TEMPLATE.get(templateCode))
                                         .openProtocol();

        protocol.shouldHaveType(expectedType)
                .shouldContainH2(h2Headers)
                .shouldContainTags(expectedTags);
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> commonTemplates() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of(
                "sobsedovanie",
                "Собеседование",
                List.of("Содержание", "Хештеги", "Заключение", "Дальнейшие шаги"),
                List.of("Собеседование")
            ),
            org.junit.jupiter.params.provider.Arguments.of(
                "commercial",
                "Переговоры",
                List.of("Содержание", "Хештеги"),
                List.of("Переговоры")
            )
        );
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("02. Доп. протокол «Собеседование» содержит правильные столбцы")
    @Story("Дополнительный протокол")
    void shouldDisplayExtraProtocolForInterview() {
        ProtocolPage protocol = videoPage.open(VIDEO_BY_TEMPLATE.get("sobsedovanie"))
                                         .openProtocol()
                                         .switchToExtra();

        protocol.shouldContainTableHeaders(List.of(
                "Таймкод", "Описание раздела", "Тип раздела", "Оценка",
                "Мотивация", "Подозрительное поведение"
        ));
    }


    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("03. Видео без протокола — отображается заглушка")
    @Story("Обработка отсутствия данных")
    void shouldShowStubWhenNoProtocol() {
        videoPage.open("noProtocolVideoId123")
                 .openProtocol()
                 .shouldShowEmptyStub();
    }


    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("04. Ошибка 500 при загрузке протокола отображается в UI")
    @Story("Обработка ошибок")
    void shouldShowErrorToast() {
        videoPage.open("serverErrorVideoId500")
                 .openProtocol()
                 .shouldShowError("Не удалось загрузить протокол");
    }
}
