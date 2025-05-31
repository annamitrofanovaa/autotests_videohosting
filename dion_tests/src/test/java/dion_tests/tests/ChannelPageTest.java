package dion_tests.tests;

import dion_tests.pages.ChannelPage;
import dion_tests.utils.BaseTest;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import java.util.Random;

import org.junit.jupiter.api.*;

import com.codeborne.selenide.Configuration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChannelPageTest extends BaseTest {
    private final ChannelPage channelPage = new ChannelPage();

    @Test
    @Disabled
    @Order(1)
    public void shouldOpenChannelPage() {

        channelPage.open("https://frontend-test.dev.dion.vc/video/channel/bbf7720c-09e2-4385-a0dd-3116c90af7a0");

        System.out.println("🌐 [Test] ➤ Канал открыт, проверяем заголовок...");
        channelPage.shouldHaveCorrectTitle("fdsdfsd");

        System.out.println("✅ [Test] ➤ Заголовок канала совпал");*/
        String channelUrl = "https://frontend-test.dev.dion.vc/video/channel/bbf7720c-09e2-4385-a0dd-3116c90af7a0";
        channelPage.open(channelUrl);
        channelPage.checkChannelElements();
    }

    @Test
    @Disabled
    @Order(2)
    
    public void shouldSubscribeSuccessfully() {
        System.out.println("🚀 [Test] ➤ Запускается: shouldSubscribeSuccessfully");

        channelPage.open("https://frontend-test.dev.dion.vc/video/channel/bbf7720c-09e2-4385-a0dd-3116c90af7a0");


        System.out.println("🔔 [Test] ➤ Канал открыт, нажимаем 'Подписаться'");
        channelPage.subscribeToChannel();

        System.out.println("✅ [Test] ➤ Подписка прошла успешно");
    }

    @Test
    @Disabled
    @Order(3)
    public void shouldUnsubscribeSuccessfully() {
        System.out.println("🚀 [Test] ➤ Запуск теста отписки");

        String channelUrl = "https://frontend-test.dev.dion.vc/video/channel/bbf7720c-09e2-4385-a0dd-3116c90af7a0";
        channelPage.open(channelUrl);

        channelPage.unsubscribeFromChannel();
    }
    @Test
    @Disabled
    @Order(4)
    @DisplayName("Создание нового канала")
    public void shouldCreateChannelDraftSuccessfully() {
        System.out.println("[Test] Запускается: shouldCreateChannelDraftSuccessfully");

        String channelName = "ТЕСТ" + new Random().nextInt(10000); // например: ТЕСТ5678
        channelPage.createChannelDraft(channelName, "Описание канала");
        Configuration.timeout = 10000;
        System.out.println("[Test] Черновик канала успешно создан и шаг 'Доступы и модераторы' доступен");
    }
    @Test
    @Disabled
    @Order(5)
    @DisplayName("Редактирование ранее созданного канала")
    public void shouldEditChannelSuccessfully() {
        System.out.println("[Test] Запускается: shouldEditChannelSuccessfully");

        String channelName = "ТЕСТ" + new Random().nextInt(10000);
        String channelDescription = "Описание для создания канала";
        channelPage.createChannelDraft(channelName, channelDescription);
        Configuration.timeout = 10000;

        String bannerFilePath = "C:/Dion_tests/dion_tests/test_img/1.jpg"; // путь до новой обложки
        channelPage.editChannelAndChangeBanner(bannerFilePath);

        System.out.println("[Test] Канал успешно создан и отредактирован");
    }
    @Test
    @Order(6)
    @DisplayName("Проверка отображения рекламного блока на канале")
    public void shouldSeeAdvertisementBlock() {
        System.out.println("[Test] Проверяем отображение блока рекламы");

        String channelUrl = "https://frontend-video-test.dev.dion.vc/video/channel/854a0dfd-23ab-43e5-8508-ec1b29b0de58";
        channelPage.open(channelUrl);

        channelPage.verifyAdvertisementBlockVisible();

        System.out.println("[Test] Блок рекламы успешно найден");
    }
    
}
