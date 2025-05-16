package dion_tests.tests;

import dion_tests.pages.VideoPlaybackPage;
import dion_tests.utils.BaseTest;
import io.qameta.allure.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Видео")  
@Feature("Воспроизведение")
@Story("Проверка задержки старта")
@Severity(SeverityLevel.CRITICAL)
public class VideoPlaybackTest extends BaseTest {

    VideoPlaybackPage videoPage = new VideoPlaybackPage();
    String VIDEO_URL = "https://frontend-video-test.dev.dion.vc/video/129d644b-559a-4764-b4c1-cb4f58d8370e?ref_uid=a9771563-f9ea-4640-985a-e86f17d2ef5f";

    @Test
    @DisplayName("Видео должно начать играть после клика Play")
    public void shouldStartPlayingQuicklyAfterClickingPlay() {

        videoPage.open(VIDEO_URL);
        long clickMoment = videoPage.clickPlay();
        videoPage.waitUntilPlaying();
        long delay = System.currentTimeMillis() - clickMoment;
        assertTrue(delay < 10_000, "Видео запустилось дольше 10 сек (delay = " + delay + " мс)");
    }
}
