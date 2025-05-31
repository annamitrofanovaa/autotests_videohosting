package dion_tests.tests;

import com.codeborne.selenide.Configuration;
import dion_tests.pages.LoginPage;
import dion_tests.pages.PlaylistPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.stream.Stream;

@Epic("Плейлисты")
@Feature("Управление плейлистами в канале")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class PlaylistTest {

    private static final String CHANNEL_ID = "ebc99ea3-c050-4ac7-88b7-9b547bbd4686";
    private PlaylistPage playlistPage;
    private String createdPlaylistId;   


    @BeforeAll
    static void setup() {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10_000;
        new LoginPage().openPage().login();     
    }

    @BeforeEach
    void initPageObject() {
        playlistPage = new PlaylistPage();
    }

    private String uniqueName() {
        return "QA_Playlist_" + System.currentTimeMillis();
    }


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("01. Создание нового плейлиста в канале")
    @Story("CRUD плейлистов")
    void shouldCreatePlaylist() {
        String name = uniqueName();
        playlistPage.openChannel(CHANNEL_ID)
                    .createPlaylist(name, "Описание автотеста")
                    .shouldBeSuccessful();
        createdPlaylistId = url().substring(url().lastIndexOf('/') + 1);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("02. Добавление видео в плейлист")
    @Story("Операции с видео")
    void shouldAddVideoToPlaylist() {
        String videoTitle = "Demo video #1";
        playlistPage.openPlaylist(CHANNEL_ID, createdPlaylistId)
                    .addVideo(videoTitle)
                    .shouldBeSuccessful()
                    .shouldContainVideo(videoTitle);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("03. Перемещение видео внутри плейлиста")
    @Story("Сортировка видео")
    void shouldReorderVideos() {
        playlistPage.openPlaylist(CHANNEL_ID, createdPlaylistId)
                    .addVideo("Demo video #2")
                    .reorderVideo("Demo video #2", 1)
                    .shouldBeSuccessful()
                    .videoShouldBeAtPosition("Demo video #2", 1);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("04. Редактирование имени плейлиста")
    @Story("CRUD плейлистов")
    void shouldEditPlaylistName() {
        String newName = "Edited_" + uniqueName();
        playlistPage.openPlaylist(CHANNEL_ID, createdPlaylistId)
                    .editPlaylist(newName)
                    .shouldBeSuccessful();
        $("h1.playlist-title").shouldHave(text(newName));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("05. Удаление видео из плейлиста")
    @Story("Операции с видео")
    void shouldRemoveVideo() {
        String videoTitle = "Demo video #1";
        playlistPage.openPlaylist(CHANNEL_ID, createdPlaylistId)
                    .removeVideo(videoTitle)
                    .shouldBeSuccessful()
                    .shouldNotContainVideo(videoTitle);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("06. Удаление плейлиста")
    @Story("CRUD плейлистов")
    void shouldDeletePlaylist() {
        playlistPage.openPlaylist(CHANNEL_ID, createdPlaylistId)
                    .deletePlaylist()
                    .shouldBeSuccessful();
    }


    @ParameterizedTest(name = "07.{index} Ошибка при создании плейлиста с некорректным именем: \"{0}\"")
    @MethodSource("invalidNames")
    @Severity(SeverityLevel.NORMAL)
    @Story("Валидация имени плейлиста")
    void shouldValidateName(String badName) {
        playlistPage.openChannel(CHANNEL_ID)
                    .createPlaylist(badName, "Описание")
                    .shouldShowError("Название плейлиста не может быть пустым");
    }

    static Stream<String> invalidNames() {
        return Stream.of("", " ", "a");   
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("08. Ошибка при добавлении дублирующего видео")
    @Story("Операции с видео")
    void shouldNotAllowDuplicateVideo() {
        String videoTitle = "Duplicate video";
        playlistPage.openChannel(CHANNEL_ID)
                    .createPlaylist(uniqueName(), "")
                    .addVideo(videoTitle)
                    .shouldBeSuccessful()
                    .addVideo(videoTitle)       
                    .shouldShowError("Видео уже есть в плейлисте");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("09. Попытка сохранить пустой плейлист")
    @Story("Обязательные условия")
    void shouldNotSaveEmptyPlaylist() {
        playlistPage.openChannel(CHANNEL_ID)
                    .createPlaylist(uniqueName(), "")
                    .submit()                    
                    .shouldShowError("Добавьте хотя бы одно видео");
    }
}
