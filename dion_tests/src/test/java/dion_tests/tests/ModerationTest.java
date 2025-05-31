package dion_tests.tests;

import com.codeborne.selenide.Configuration;
import dion_tests.pages.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.List;

@Epic("Доступ")
@Feature("Типы доступа к видео и каналам")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class AccessTest {

    private static final String VIDEO_ID   = "videoAccess123";
    private static final String CHANNEL_ID = "channelAccess456";

    @BeforeAll
    static void login() {
        Configuration.browserSize = "1920x1080";
        new LoginPage().openPage().login();         // owner
    }


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("01. Видео: выставление публичного доступа")
    void videoPublicAccess() {
        new VideoEditPage().open(VIDEO_ID)
                           .openAccessTab()
                           .setPublic()
                           .save()
                           .shouldBeSaved();

        new VideoPage().open(VIDEO_ID)
                       .$("span.access-badge")
                       .shouldHave(text("Публичный"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("02. Видео: доступ «По ссылке» (Unlisted)")
    void videoUnlistedAccess() {
        String link;

        AccessSettingsPage asp = new VideoEditPage().open(VIDEO_ID)
                                                    .openAccessTab()
                                                    .setByLink()
                                                    .save();
        asp.shouldBeSaved();

        link = $("input#share-link").getValue();

        new LoginPage().openPage(); // выходим (simplified)
        open(link);
        $(".video-player").shouldBe(visible);               // доступ есть
        $("[data-testid='search-result']").shouldNot(exist);// в индексе нет
        new LoginPage().openPage().login();                 // логинимся обратно
    }

    @Test
    @DisplayName("03. Видео: приватный доступ (только владелец)")
    @Severity(SeverityLevel.CRITICAL)
    void videoPrivateAccess() {
        new VideoEditPage().open(VIDEO_ID)
                           .openAccessTab()
                           .setPrivate()
                           .save()
                           .shouldBeSaved();

        new LoginPage().openPage(); // logout
        open("https://frontend-video-test.dev.dion.vc/video/" + VIDEO_ID);
        $(".locked-placeholder").shouldBe(visible);
        new LoginPage().openPage().login(); // вернули owner
    }

    @Test
    @DisplayName("04. Видео: доступ ограниченному кругу лиц")
    @Severity(SeverityLevel.NORMAL)
    void videoCircleAccess() {
        List<String> circle = List.of("qa.friend@mail.com", "pm@mail.com");

        new VideoEditPage().open(VIDEO_ID)
                           .openAccessTab()
                           .setCircle(circle)
                           .save()
                           .shouldBeSaved()
                           .shouldContainInvited(circle);
    }

    @Test
    @DisplayName("05. Видео: доступ группам организаций")
    @Severity(SeverityLevel.NORMAL)
    void videoGroupAccess() {
        List<String> groups = List.of("HR-Team", "Developers");

        new VideoEditPage().open(VIDEO_ID)
                           .openAccessTab()
                           .setGroups(groups)
                           .save()
                           .shouldBeSaved()
                           .shouldContainGroups(groups);
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("06. Канал: публичный → приватный переход")
    void channelChangeAccess() {
        // Публичный
        new ChannelEditPage().open(CHANNEL_ID)
                             .openAccessTab()
                             .setPublic()
                             .save()
                             .shouldBeSaved();
        $("span.access-badge").shouldHave(text("Публичный"));

        // Приватный
        new ChannelEditPage().open(CHANNEL_ID)
                             .openAccessTab()
                             .setPrivate()
                             .save()
                             .shouldBeSaved();
        $("span.access-badge").shouldHave(text("Закрытый"));
    }
}
