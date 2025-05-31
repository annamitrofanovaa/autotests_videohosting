package dion_tests.pages;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Step;

public class VideoPage {

    private SelenideElement videoPlayer = $("video");  
    private SelenideElement title = $("h4"); 
    private SelenideElement likeButton = $x("//button[contains(., 'Нравится')]");
    private SelenideElement dislikeButton = $x("//button[contains(., 'Не очень')]");
    private SelenideElement shareButton = $x("//button[contains(., 'Поделиться')]");
    private SelenideElement commentsBlock = $x("//*[contains(text(), 'Комментарии')]");


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
    private static final String VIDEO_URL_PATTERN =
            "https://frontend-video-test.dev.dion.vc/video/%s";   // {videoId}

    private final SelenideElement protocolBtn = $("button#open-protocol");

    @Step("Открываем видео {videoId}")
    public VideoPage open(String videoId) {
        open(String.format(VIDEO_URL_PATTERN, videoId));
        return this;
    }

    @Step("Открываем вкладку «Протокол»")
    public ProtocolPage openProtocol() {
        protocolBtn.shouldBe(visible).click();
        return new ProtocolPage();
    }
}
