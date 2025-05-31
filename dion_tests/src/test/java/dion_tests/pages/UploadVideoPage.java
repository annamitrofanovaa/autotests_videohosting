package dion_tests.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.io.File;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.byText;

public class UploadVideoPage {

    private final SelenideElement addVideoBtn       = $(byText("ДОБАВИТЬ ВИДЕО"));
    private final SelenideElement uploadFromDevice  = $(byText("Добавить с устройства"));
    private final SelenideElement fileInput         = $("input[type='file']");
    private final SelenideElement progressBar       = $(".upload-progress");
    private final SelenideElement successToast      = $(".toast-success");
    private final SelenideElement errorToast        = $(".toast-error");

    private final SelenideElement titleInput        = $("[name='videoTitle']");
    private final SelenideElement descriptionArea   = $("[name='videoDescription']");
    private final SelenideElement tagInput          = $("[name='videoTags']");
    private final SelenideElement saveBtn           = $(byText("Сохранить"));


    @Step("Нажимаем на кнопку «Добавить видео»")
    public UploadVideoPage clickAddVideoButton() {
        addVideoBtn.shouldBe(visible, Duration.ofSeconds(10)).click();
        return this;
    }

    @Step("Выбираем пункт «Добавить с устройства»")
    public UploadVideoPage clickUploadFromDevice() {
        uploadFromDevice.shouldBe(visible).click();
        return this;
    }

    @Step("Загружаем видеофайл: {filePath}")
    public UploadVideoPage uploadVideoFromComputer(String filePath) {
        fileInput.uploadFile(new File(filePath));
        return this;
    }

    @Step("Заполняем метаданные: title={title}")
    public UploadVideoPage fillMetadata(String title, String description, String tags) {
        titleInput.setValue(title);
        descriptionArea.setValue(description);
        tagInput.setValue(tags).pressEnter();
        return this;
    }

    @Step("Сохраняем карточку видео")
    public UploadVideoPage saveVideoCard() {
        saveBtn.shouldBe(enabled).click();
        return this;
    }


    @Step("Дожидаемся завершения загрузки")
    public UploadVideoPage waitUntilUploaded() {
        progressBar.should(disappear, Duration.ofMinutes(2));
        return this;
    }

    @Step("Проверяем успешную загрузку")
    public void shouldUploadSuccess() {
        successToast.shouldBe(visible).shouldHave(text("успешно загружен"));
    }

    @Step("Показан прогресс-бар")
    public void progressShouldAppear() {
        progressBar.shouldBe(visible);
    }

    @Step("Отобразилась ошибка: {message}")
    public void shouldShowError(String message) {
        errorToast.shouldBe(visible).shouldHave(text(message));
    }
}
