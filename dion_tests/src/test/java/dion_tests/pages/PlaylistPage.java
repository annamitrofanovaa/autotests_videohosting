package dion_tests.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class PlaylistPage {

    private static final String CHANNEL_URL_PATTERN =
            "https://frontend-video-test.dev.dion.vc/video/channel/%s";
    private static final String PLAYLIST_URL_PATTERN =
            CHANNEL_URL_PATTERN + "/playlist/%s";

    private final SelenideElement createBtn       = $("button#create-playlist");
    private final SelenideElement nameInput       = $("[name='playlistName']");
    private final SelenideElement descriptionArea = $("[name='playlistDescription']");
    private final SelenideElement modalSaveBtn    = $("button[type='submit']");
    private final SelenideElement successToast    = $(".toast-success");
    private final SelenideElement errorToast      = $(".toast-error");

    private final SelenideElement editBtn         = $("button#edit-playlist");
    private final SelenideElement deleteBtn       = $("button#delete-playlist");
    private final SelenideElement confirmDelete   = $("button#confirm-delete");

    private final SelenideElement addVideoBtn     = $("button#add-video");
    private final ElementsCollection videoCards   = $$(".playlist-video-card");
    private final SelenideElement saveOrderBtn    = $("button#save-order");

    @Step("Открываем канал: {channelId}")
    public PlaylistPage openChannel(String channelId) {
        open(String.format(CHANNEL_URL_PATTERN, channelId));
        return this;
    }

    @Step("Открываем плейлист в канале: {playlistId}")
    public PlaylistPage openPlaylist(String channelId, String playlistId) {
        open(String.format(PLAYLIST_URL_PATTERN, channelId, playlistId));
        return this;
    }

    @Step("Создаём новый плейлист с именем: {name}")
    public PlaylistPage createPlaylist(String name, String description) {
        createBtn.shouldBe(visible).click();
        nameInput.setValue(name);
        descriptionArea.setValue(description);
        modalSaveBtn.click();
        return this;
    }

    @Step("Редактируем плейлист: новое имя = {newName}")
    public PlaylistPage editPlaylist(String newName) {
        editBtn.click();
        nameInput.clear();      
        nameInput.setValue(newName);
        modalSaveBtn.click();
        return this;
    }

    @Step("Удаляем плейлист")
    public PlaylistPage deletePlaylist() {
        deleteBtn.click();
        confirmDelete.shouldBe(visible).click();
        return this;
    }

    @Step("Добавляем видео '{videoTitle}' в плейлист")
    public PlaylistPage addVideo(String videoTitle) {
        addVideoBtn.click();
        $$(".video-search-result").findBy(text(videoTitle))
                                  .scrollTo().$("button.add").click();
        return this;
    }

    @Step("Удаляем видео '{videoTitle}' из плейлиста")
    public PlaylistPage removeVideo(String videoTitle) {
        videoCards.findBy(text(videoTitle))
                  .hover().$("button.remove-video").click();
        return this;
    }

    @Step("Перемещаем видео '{videoTitle}' на позицию {targetPos}")
    public PlaylistPage reorderVideo(String videoTitle, int targetPos) {
        SelenideElement video = videoCards.findBy(text(videoTitle));
        SelenideElement target = videoCards.get(targetPos - 1);     // позиции начинаются с 1
        actions().dragAndDrop(video, target).perform();
        saveOrderBtn.click();
        return this;
    }

    @Step("Плейлист успешно создан/изменён")
    public void shouldBeSuccessful() {
        successToast.shouldBe(visible, Duration.ofSeconds(10));
    }

    @Step("Должна отображаться ошибка: {message}")
    public void shouldShowError(String message) {
        errorToast.shouldBe(visible, Duration.ofSeconds(10))
                  .shouldHave(text(message));
    }

    @Step("Плейлист содержит видео '{videoTitle}'")
    public void shouldContainVideo(String videoTitle) {
        videoCards.findBy(text(videoTitle)).shouldBe(visible);
    }

    @Step("Плейлист не содержит видео '{videoTitle}'")
    public void shouldNotContainVideo(String videoTitle) {
        videoCards.findBy(text(videoTitle)).shouldNot(exist);
    }

    @Step("Видео '{videoTitle}' находится на позиции {expectedPos}")
    public void videoShouldBeAtPosition(String videoTitle, int expectedPos) {
        videoCards.get(expectedPos - 1)
                  .shouldHave(text(videoTitle));
    }
}
