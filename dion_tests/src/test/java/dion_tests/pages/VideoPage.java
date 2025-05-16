package dion_tests.pages;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

import com.codeborne.selenide.SelenideElement;

public class VideoPage {

    // 🔹 Элементы на странице видео
    private SelenideElement videoPlayer = $("video");  // Или более точный селектор
    private SelenideElement title = $("h4"); // Заголовок видео
    private SelenideElement likeButton = $x("//button[contains(., 'Нравится')]");
    private SelenideElement dislikeButton = $x("//button[contains(., 'Не очень')]");
    private SelenideElement shareButton = $x("//button[contains(., 'Поделиться')]");
    private SelenideElement commentsBlock = $x("//*[contains(text(), 'Комментарии')]");

    // 🔹 Методы-проверки

    public void checkVideoLoaded() {
        videoPlayer.shouldBe(visible);
    }

    public void checkTitle(String expectedText) {
        title.shouldHave(text(expectedText));
    }

    public void likeVideo() {
        likeButton.shouldBe(visible);
    }

    public void dislikeVideo() {
        dislikeButton.shouldBe(visible);
    }

    public void shareVideo() {
        shareButton.shouldBe(visible);
    }

    public void checkCommentsVisible() {
        commentsBlock.scrollIntoView(true).shouldBe(visible);
    }
}
