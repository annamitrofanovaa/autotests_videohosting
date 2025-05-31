package dion_tests.tests;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.*;
import io.qameta.allure.junit5.AllureJunit5;
import dion_tests.pages.*;
import dion_tests.utils.BaseTest;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.Random;

import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Smoke")
@Feature("Базовый UI-функционал")
@Owner("qa-team")
@Severity(SeverityLevel.BLOCKER)
@ExtendWith(AllureJunit5.class)
@Tag("smoke")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class SmokeTest extends BaseTest {

    AllVideoPage       allVideoPage   = new AllVideoPage();
    ChannelPage        channelPage    = new ChannelPage();
    VideoPage          videoPage      = new VideoPage();
    ChannelCreation    chcreate       = new ChannelCreation();

    LoginPage          loginPage      = new LoginPage();
    UploadVideoPage    uploadPage     = new UploadVideoPage();
    CommentSection     comments       = new CommentSection();
    SearchResultPage   searchPage     = new SearchResultPage();

    private static final String SMALL_MP4 =
            Paths.get("src/test/resources/videos/small.mp4").toString();
    private static final String VIDEO_ID   = "a858c10d-afb9-462b-8b16-ffa49edc4182";
    private static final String CHANNEL_URL =
            "https://frontend-test.dev.dion.vc/video/channel/bbf7720c-09e2-4385-a0dd-3116c90af7a0";

    @Test
    @Story("Smoke Test: ключевой функционал")
    @DisplayName("01. Авторизация и главная страница")
    public void TestAllVideo() {
        /* login + каталог */
        step("Логинимся", () -> {
            loginPage.openPage().login();
            assertThat(loginPage.isLoginSuccessful()).isTrue();
        });

        openAllVideoPageAndValidate();
        assertTrue(true,
                "Smoke-проход каталога видео завершён без критических ошибок");
    }

    @Test
    @DisplayName("02. Создание черновика канала")
    public void TestChannels() {
        createChannelDraft();
        assertTrue(true,
                "Smoke-проход функционала каналов завершён без критических ошибок");
    }

    @Test
    @DisplayName("03. Страница канала")
    public void TestChannelsUI() {
        checkChannelElementsAndSubscribe();
        assertTrue(true,
                "Smoke-проход страниц каналов завершён без критических ошибок");
    }

    @Test
    @DisplayName("04. Просмотр видео")
    public void TestVideoPage() {
        openAndCheckVideoPage();
        assertTrue(true, "Smoke-проход Видео завершён без критических ошибок");
    }

    @Test
    @Feature("Загрузка видео")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("05. Загрузка видеофайла")
    public void TestUploadVideo() {
        step("Happy-path загрузки", () -> {
            uploadPage.clickAddVideoButton()
                      .clickUploadFromDevice()
                      .uploadVideoFromComputer(SMALL_MP4)
                      .waitUntilUploaded()
                      .fillMetadata("Smoke video",
                                    "Описание смока", "smoke")
                      .saveVideoCard()
                      .shouldUploadSuccess();
        });
    }

    @Test
    @Feature("Комментарии")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("06. Мини-проверка комментариев")
    public void TestComments() {
        open("https://frontend-test.dev.dion.vc/video/" + VIDEO_ID);
        String text = "Smoke comment " + System.currentTimeMillis();

        comments.addComment(text).shouldShowSuccess();
        comments.shouldContain(text);
    }
    @Test
    @Feature("Core-функции")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("07. Поиск и быстрая подписка")
    public void TestCoreFlow() {
        step("Поиск видео", () -> {
            searchPage.openPage()
                      .searchFor("videotest")
                      .switchToFilter("videos")
                      .shouldContainVideo("videotest");
        });

        step("Подписка на канал", () -> {
            channelPage.open(CHANNEL_URL);
            channelPage.subscribeToChannel();
            assertThat(channelPage.isSubscribed()).isTrue();
        });
    }

    @Step("Открываем страницу «Все видео» и проверяем интерфейс и фильтрацию")
    private void openAllVideoPageAndValidate() {
        allVideoPage.open("https://frontend-test.dev.dion.vc/video/all");

        allVideoPage.validateFiltersAndSorts();
        allVideoPage.checkSidebarElementsVisible();
        allVideoPage.shouldRenderFirstVideoCardProperly();
        refresh();
        allVideoPage.shouldScrollToBannerAndSeeIt();
        allVideoPage.shouldCloseBannerOnClick();
        allVideoPage.shouldScrollPageWithoutErrors();
    }

    @Step("Создаём черновик канала")
    private void createChannelDraft() {
        String channelName = "SMOKE_CH_" + new Random().nextInt(10_000);
        chcreate.createChannelDraft(channelName, "Smoke draft");
    }

    @Step("Проверяем UI канала и подписку")
    private void checkChannelElementsAndSubscribe() {
        channelPage.open(CHANNEL_URL);
        channelPage.checkChannelElements();
        channelPage.subscribeToChannel();
        channelPage.Unsubscribe();
    }

    @Step("Открываем видео и выполняем лайк/дизлайк/share")
    private void openAndCheckVideoPage() {
        open("https://frontend-test.dev.dion.vc/video/" + VIDEO_ID);
        videoPage.checkVideoLoaded();
        videoPage.checkTitle("videotest");
        videoPage.likeVideo();
        videoPage.dislikeVideo();
        videoPage.shareVideo();
        videoPage.checkCommentsVisible();
    }
}
