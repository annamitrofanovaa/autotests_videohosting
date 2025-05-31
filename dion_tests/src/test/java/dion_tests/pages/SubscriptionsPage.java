package dion_tests.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class SubscriptionsPage {

    private static final String SUBSCRIPTIONS_URL = "https://frontend-video-test.dev.dion.vc/subscriptions";
    private static final String CHANNEL_URL_PATTERN =
            "https://frontend-video-test.dev.dion.vc/video/channel/%s";

    private final SelenideElement subscribeBtn      = $("button#subscribe");
    private final SelenideElement unsubscribeBtn    = $("button#unsubscribe");
    private final SelenideElement confirmUnsubBtn   = $("button#confirm-unsub");

    private final SelenideElement successToast      = $(".toast-success");
    private final SelenideElement errorToast        = $(".toast-error");

    private final ElementsCollection channelCards   = $$(".subscription-card");
    private final SelenideElement emptyState        = $(".empty-subscriptions");

    @Step("Открываем страницу подписок")
    public SubscriptionsPage openSubscriptions() {
        open(SUBSCRIPTIONS_URL);
        return this;
    }

    @Step("Открываем канал по id: {channelId}")
    public SubscriptionsPage openChannel(String channelId) {
        open(String.format(CHANNEL_URL_PATTERN, channelId));
        return this;
    }

    @Step("Подписываемся на канал")
    public SubscriptionsPage subscribe() {
        subscribeBtn.shouldBe(visible).click();
        return this;
    }

    @Step("Отписываемся от канала из самой страницы канала")
    public SubscriptionsPage unsubscribe() {
        unsubscribeBtn.shouldBe(visible).click();
        confirmUnsubBtn.shouldBe(visible).click();
        return this;
    }

    @Step("Отписываемся от канала «{channelName}» из списка подписок")
    public SubscriptionsPage unsubscribeFromList(String channelName) {
        channelCards.findBy(text(channelName))
                    .hover().$("button.remove-subscription").click();
        confirmUnsubBtn.click();
        return this;
    }

    @Step("Операция прошла успешно")
    public void shouldBeSuccessful() {
        successToast.shouldBe(visible, Duration.ofSeconds(10));
    }

    @Step("Должна отображаться ошибка: {msg}")
    public void shouldShowError(String msg) {
        errorToast.shouldBe(visible).shouldHave(text(msg));
    }

    @Step("Канал «{channelName}» присутствует в списке подписок")
    public void shouldContainChannel(String channelName) {
        channelCards.findBy(text(channelName)).shouldBe(visible);
    }

    @Step("Канал «{channelName}» отсутствует в списке подписок")
    public void shouldNotContainChannel(String channelName) {
        channelCards.findBy(text(channelName)).shouldNot(exist);
    }

    @Step("Список подписок пуст")
    public void shouldShowEmptyState() { emptyState.shouldBe(visible); }

    public int subscriptionsCount() { return channelCards.size(); }
}
