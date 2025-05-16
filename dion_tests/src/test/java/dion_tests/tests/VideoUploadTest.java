package dion_tests.tests;

import dion_tests.pages.UploadVideoPage;
import dion_tests.pages.ChannelCreation;
import dion_tests.pages.AllVideoPage;
import dion_tests.utils.BaseTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import io.qameta.allure.*;

@Epic("Access")
@Feature("Загрузка видео в канал")
public class VideoUploadTest extends BaseTest {

    UploadVideoPage uploadPage = new UploadVideoPage();
    ChannelCreation channelCreation = new ChannelCreation();
    AllVideoPage allVideoPage = new AllVideoPage();

    @Test
    @Story("Создание публичного канала и загрузка видео")
    @DisplayName("Создание публичного канала и проверка добавленного видео")
    public void createPublicChannelAndUploadVideo() {
        String channelName = "Тестовый Канал " + System.currentTimeMillis();
        channelCreation.createChannelDraft(channelName, "Описание тестового канала", "Доступен сотрудникам моей компании");
        uploadPage.clickAddVideoButton();
        uploadPage.clickUploadFromDevice();
        uploadPage.uploadVideoFromComputer("C:/Users/22ami/Downloads/тествидео.mov");
        uploadPage.verifyUploadSuccess();

        allVideoPage.open("https://frontend-test.dev.dion.vc/video/all");
        allVideoPage.checkFirstVideoIsFromChannel(channelName);
    }
}


