package dion_tests.tests;

import com.codeborne.selenide.Configuration;
import dion_tests.pages.LoginPage;
import dion_tests.pages.SubscriptionsPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

@Epic("Подписки")
@Feature("Управление подписками на каналы")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class SubscriptionsTest {

    // Тестовый канал (существует в базе)
    private static final String CHANNEL_ID   = "ebc99ea3-c050-4ac7-88b7-9b547bbd4686";
    private static final String CHANNEL_NAME = "QA Automation Channel";

    private final SubscriptionsPage subs = new SubscriptionsPage();
    private int initialCount;

    @BeforeAll
    static void setupEnv() {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10_000;
        new LoginPage().openPage().login();  
    }

    @BeforeEach
    void captureCount() {
        initialCount = subs.openSubscriptions().subscriptionsCount();
    }


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("01. Подписка на канал")
    @Story("Подписка")
    void shouldSubscribeToChannel() {
        subs.openChannel(CHANNEL_ID)
            .subscribe()
            .shouldBeSuccessful();

        subs.openSubscriptions()
            .shouldContainChannel(CHANNEL_NAME);

        Assertions.assertEquals(initialCount + 1, subs.subscriptionsCount(),
                "Счётчик подписок должен увеличиться на 1");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("02. Отписка от канала")
    @Story("Отписка")
    void shouldUnsubscribe() {
        subs.openChannel(CHANNEL_ID).subscribe().shouldBeSuccessful();

        subs.openSubscriptions()
            .unsubscribeFromList(CHANNEL_NAME)
            .shouldBeSuccessful()
            .shouldNotContainChannel(CHANNEL_NAME);

        Assertions.assertEquals(initialCount, subs.subscriptionsCount(),
                "Кол-во подписок должно вернуться к исходному");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("03. Подписка сохраняется после перезагрузки страницы")
    @Story("Персистентность")
    void subscriptionPersistsAfterReload() {
        subs.openChannel(CHANNEL_ID)
            .subscribe()
            .shouldBeSuccessful();

        subs.openSubscriptions().shouldContainChannel(CHANNEL_NAME);
        refresh();
        subs.shouldContainChannel(CHANNEL_NAME);
    }


    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("04. Повторная подписка на тот же канал не создаёт дубль")
    @Story("Защита от дублей")
    void shouldNotDuplicateSubscription() {
        subs.openChannel(CHANNEL_ID).subscribe().shouldBeSuccessful();
        int countAfterFirst = subs.openSubscriptions().subscriptionsCount();

        subs.openChannel(CHANNEL_ID).subscribe(); // кнопка может быть задизейблена
        subs.shouldShowError("Вы уже подписаны");

        Assertions.assertEquals(countAfterFirst, subs.openSubscriptions().subscriptionsCount(),
                "Кол-во подписок не должно измениться");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("05. Список подписок пуст — отображается заглушка")
    @Story("Пустое состояние")
    void shouldShowEmptyWhenNoSubs() {
        subs.openSubscriptions();
        while (subs.subscriptionsCount() > 0) {
            subs.unsubscribeFromList(subs.channelCards.get(0).getText()).shouldBeSuccessful();
        }
        subs.shouldShowEmptyState();
    }
}
