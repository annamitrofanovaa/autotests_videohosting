package dion_tests.pages;

import com.codeborne.selenide.SelenideElement;

import java.io.File;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selectors.byText;

public class UploadVideoPage {

    public void clickAddVideoButton() {
        System.out.println("Нажимаем на кнопку 'Добавить видео'");
        $(byText("ДОБАВИТЬ ВИДЕО")).shouldBe(visible).click();
        
    }

    public void clickUploadFromDevice() {
        System.out.println("Нажимаем 'Добавить с устройства'");
        $(byText("Добавить с устройства")).shouldBe(visible).click();
    }

    public void uploadVideoFromComputer(String filePath) {
        System.out.println("📂 Загружаем видеофайл с компа: " + filePath);
        $("input[type='file']").uploadFile(new File(filePath));
    }

    public void verifyUploadSuccess() {
        System.out.println("✅ Проверяем сообщение об успешной загрузке");
        $(byText("успешно загружен")).shouldBe(visible);
    }
}
