package dion_tests.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.visible;

public class VideoPlaybackPage {

    private final SelenideElement playButton =
        $("button[aria-label='Воспроизведение видео']");
    private final SelenideElement video      = $("video");

    @Step("Открываем страницу видео: {url}")
    public void open(String url) {
        com.codeborne.selenide.Selenide.open(url);
    }

    @Step("Нажимаем кнопку Play")
    public long clickPlay() {
        playButton.shouldBe(visible).click();
        return System.currentTimeMillis();  
    }

    @Step("Ожидаем, пока currentTime станет > 0")
    public void waitUntilPlaying() {
        for (int i = 0; i < 100; i++) {

            Number ct = executeJavaScript(          
                "return arguments[0].currentTime", video);

            if (ct != null && ct.doubleValue() > 0.0) {
                return;                             
            }
            sleep(100);
        }
        throw new AssertionError(
            "currentTime так и остался 0 за 2 сек — видео не стартовало");
    }
}
