package dion_tests.tests;

import dion_tests.pages.VideoPage;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;


import static com.codeborne.selenide.Selenide.open;
import dion_tests.utils.BaseTest;


public class VideoPageTest extends BaseTest {

    @Test
    @Disabled
    @Order(5)
    @DisplayName("UI: Проверка элементов страницы просмотра видео")
    void shouldDisplayAllVideoPageElements() {
        System.out.println("[Test] 🔍 Открываем страницу видео");

        // 👉 Заменить на актуальный URL видео
        open("https://frontend-test.dev.dion.vc/video/a858c10d-afb9-462b-8b16-ffa49edc4182");

        VideoPage videoPage = new VideoPage();

        System.out.println("[Test] ✅ Проверка наличия видеоплеера");
        videoPage.checkVideoLoaded();

        System.out.println("[Test] ✅ Проверка заголовка видео");
        videoPage.checkTitle("videotest"); // при необходимости замени на свой заголовок

        System.out.println("[Test] ✅ Проверка кнопки 'Нравится'");
        videoPage.likeVideo();

        System.out.println("[Test] ✅ Проверка кнопки 'Не очень'");
        videoPage.dislikeVideo();

        System.out.println("[Test] ✅ Проверка кнопки 'Поделиться'");
        videoPage.shareVideo();

        System.out.println("[Test] ✅ Проверка блока комментарии");
        videoPage.checkCommentsVisible();

        System.out.println("[Test] 🎉 Все элементы UI успешно проверены!");
    }
}
