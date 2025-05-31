package dion_tests.tests;

import com.codeborne.selenide.Configuration;
import dion_tests.pages.LoginPage;
import dion_tests.pages.UploadVideoPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Paths;
import java.util.stream.Stream;

@Epic("Загрузка видео")
@Feature("Страница загрузки видеофайлов")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class UploadVideoTest {

    private static final String SMALL_MP4      = Paths.get("src/test/resources/videos/small.mp4").toString();
    private static final String BIG_MP4        = Paths.get("src/test/resources/videos/big_6gb.mp4").toString();
    private static final String INVALID_FILE   = Paths.get("src/test/resources/files/text.txt").toString();

    private final UploadVideoPage upload = new UploadVideoPage();

    @BeforeAll
    static void setup() {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 20_000;
        new LoginPage().openPage().login();
    }

    @BeforeEach
    void openUploader() {
        open("https://frontend-video-test.dev.dion.vc/upload");
    }


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("01. Успешная загрузка небольшого MP4-файла")
    @Story("Позитив")
    void shouldUploadSmallVideo() {
        upload.clickAddVideoButton()
              .clickUploadFromDevice()
              .uploadVideoFromComputer(SMALL_MP4)
              .progressShouldAppear()
              .waitUntilUploaded()
              .fillMetadata("Autotest video", "Описание автотеста", "autotest")
              .saveVideoCard()
              .shouldUploadSuccess();
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("02. Ошибка при попытке загрузить файл неподдерживаемого формата")
    @Story("Валидация формата")
    void shouldRejectInvalidFormat() {
        upload.clickAddVideoButton()
              .clickUploadFromDevice()
              .uploadVideoFromComputer(INVALID_FILE)
              .shouldShowError("Формат файла не поддерживается");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("03. Ошибка при превышении максимального размера файла")
    @Story("Валидация размера")
    void shouldRejectTooLargeFile() {
        upload.clickAddVideoButton()
              .clickUploadFromDevice()
              .uploadVideoFromComputer(BIG_MP4)
              .shouldShowError("Размер файла превышает 5 ГБ");
    }

    @ParameterizedTest(name = "04.{index} Ошибка при пустом/слишком длинном названии видео: \"{0}\"")
    @MethodSource("badTitles")
    @Severity(SeverityLevel.MINOR)
    @Story("Валидация title")
    void shouldValidateTitleField(String badTitle) {
        upload.clickAddVideoButton()
              .clickUploadFromDevice()
              .uploadVideoFromComputer(SMALL_MP4)
              .waitUntilUploaded()
              .fillMetadata(badTitle, "Desc", "tag")
              .saveVideoCard()
              .shouldShowError("Название видео должно содержать 2–100 символов");
    }

    static Stream<String> badTitles() {
        return Stream.of("", " ", "a",
                "Very_".repeat(30));          // >100 символов
    }

    @Test
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("05. Загрузка отменяется пользователем")
    @Story("Отмена загрузки")
    void shouldCancelUpload() {
        upload.clickAddVideoButton()
              .clickUploadFromDevice()
              .uploadVideoFromComputer(SMALL_MP4);

        $(".upload-cancel").shouldBe(visible).click();
        upload.shouldShowError("Загрузка отменена");
    }
}
