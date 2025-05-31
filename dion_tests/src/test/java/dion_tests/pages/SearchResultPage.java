package dion_tests.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class SearchResultPage {

    private static final String URL = "https://frontend-video-test.dev.dion.vc/search";

    private final SelenideElement searchInput     = $("input[name='query']");
    private final SelenideElement submitBtn       = $("button[type='submit']");

    private final SelenideElement allFilter       = $("[data-testid='filter-all']");
    private final SelenideElement videosFilter    = $("[data-testid='filter-videos']");
    private final SelenideElement channelsFilter  = $("[data-testid='filter-channels']");

    private final ElementsCollection videoCards   = $$(".card.video");
    private final ElementsCollection channelCards = $$(".card.channel");

    private final SelenideElement emptyState      = $(".empty-search");
    private final SelenideElement errorToast      = $(".toast-error");
    private final SelenideElement spinner         = $(".search-spinner");

    @Step("Открываем страницу поиска")
    public SearchResultPage openPage() {
        open(URL);
        return this;
    }

    @Step("Выполняем поиск по запросу: {query}")
    public SearchResultPage searchFor(String query) {
        searchInput.shouldBe(visible).setValue(query);
        submitBtn.click();
        spinner.should(appear).should(disappear, Duration.ofSeconds(10));
        return this;
    }

    @Step("Переключаем фильтр на: {filterName}")
    public SearchResultPage switchToFilter(String filterName) {
        switch (filterName.toLowerCase()) {
            case "videos"   -> videosFilter.click();
            case "channels" -> channelsFilter.click();
            default         -> allFilter.click();
        }
        spinner.should(disappear);
        return this;
    }

    @Step("В результатах должен присутствовать видео-титул: {title}")
    public void shouldContainVideo(String title) {
        videoCards.findBy(text(title)).shouldBe(visible);
    }

    @Step("В результатах должен присутствовать канал: {channelName}")
    public void shouldContainChannel(String channelName) {
        channelCards.findBy(text(channelName)).shouldBe(visible);
    }

    @Step("Видео-результатов не должно быть")
    public void videosShouldBeEmpty() { videoCards.shouldBe(empty); }

    @Step("Каналов не должно быть")
    public void channelsShouldBeEmpty() { channelCards.shouldBe(empty); }

    @Step("Отображается пустое состояние «Ничего не найдено»")
    public void shouldShowEmptyState() { emptyState.shouldBe(visible); }

    @Step("Показывается ошибка: {message}")
    public void shouldShowError(String message) {
        errorToast.shouldBe(visible).shouldHave(text(message));
    }
}
