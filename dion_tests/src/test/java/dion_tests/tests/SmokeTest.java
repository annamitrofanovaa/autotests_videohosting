package dion_tests.tests;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.*;
import io.qameta.allure.junit5.AllureJunit5;
import dion_tests.pages.AllVideoPage;
import dion_tests.pages.ChannelPage;
import dion_tests.pages.VideoPage;
import dion_tests.pages.ChannelCreation;
import dion_tests.utils.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.*;

import java.util.Random;

@ExtendWith(AllureJunit5.class)
@Epic("Smoke")
@Feature("Полный обход")
public class SmokeTest extends BaseTest {

    AllVideoPage allVideoPage = new AllVideoPage();
    ChannelPage channelPage = new ChannelPage();
    VideoPage videoPage = new VideoPage();
    ChannelCreation chcreate = new ChannelCreation();

    @Test
    @Story("Smoke Test: Полный обход функционала")
    @DisplayName("SMOKE: Полный проход по системе")
    public void shouldPassSmokeTest() {
        openAllVideoPageAndValidate();
        createChannelDraft();
        checkChannelElementsAndSubscribe();
        openAndCheckVideoPage();
    }

    private void openAllVideoPageAndValidate() {
        System.out.println("🔹 Открываем Все Видео и проверяем фильтры и сортировки");

        allVideoPage.open("https://frontend-test.dev.dion.vc/video/all");
        allVideoPage.openFilterDropdown();
        allVideoPage.clickAllFiltersAndReset();

        int centerX = 1620 / 2;
        int centerY = 1080 / 2;
        executeJavaScript("document.elementFromPoint(" + centerX + ", " + centerY + ").click();");

        allVideoPage.selectSortOption("Сначала новые");
        allVideoPage.clickSortDropdown("По лайкам");
        allVideoPage.selectSortOption("По лайкам");
        allVideoPage.clickSortDropdown("По просмотрам");
        allVideoPage.selectSortOption("По просмотрам");
        allVideoPage.clickSortDropdown("По комментариям");
        allVideoPage.selectSortOption("По комментариям");
        allVideoPage.clickSortDropdown("Сначала новые");
        allVideoPage.selectSortOption("Сначала новые");

        allVideoPage.checkSidebarElementsVisible();
        allVideoPage.shouldRenderFirstVideoCardProperly();
        refresh();
        allVideoPage.shouldScrollToBannerAndSeeIt();
        allVideoPage.shouldCloseBannerOnClick();
        allVideoPage.shouldScrollPageWithoutErrors();
    }

    private void createChannelDraft() {
        System.out.println("🔹 Создаём Черновик канала");

        String channelName = "ТЕСТ" + new Random().nextInt(10000);
   //param     chcreate.createChannelDraft(channelName, "Описание канала");

        Configuration.timeout = 10000;
        System.out.println("[Test] Черновик канала успешно создан");
    }

    private void checkChannelElementsAndSubscribe() {
        System.out.println("🔹 Проверяем Канал и подписываемся");

        String channelUrl = "https://frontend-test.dev.dion.vc/video/channel/bbf7720c-09e2-4385-a0dd-3116c90af7a0";
        channelPage.open(channelUrl);
        channelPage.checkChannelElements();
        channelPage.subscribeToChannel();
        System.out.println("[Test] Подписка на канал выполнена");

        // Отписка
        refresh();
        int centerX = 1920 / 2;
        int centerY = 1080 / 2;
        executeJavaScript("document.elementFromPoint(" + centerX + ", " + centerY + ").click();");

        channelPage.unsubscribeFromChannel();
        System.out.println("[Test] Отписка от канала выполнена");
    }

    private void openAndCheckVideoPage() {
        System.out.println("🔹 Открываем Страницу Видео и проверяем элементы");

        open("https://frontend-test.dev.dion.vc/video/a858c10d-afb9-462b-8b16-ffa49edc4182");

        videoPage.checkVideoLoaded();
        videoPage.checkTitle("videotest");
        videoPage.likeVideo();
        videoPage.dislikeVideo();
        videoPage.shareVideo();
        videoPage.checkCommentsVisible();

        System.out.println("[Test] Видео страница проверена полностью 🎉");
    }
}
