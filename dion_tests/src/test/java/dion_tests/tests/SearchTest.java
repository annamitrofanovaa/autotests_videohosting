package dion_tests.tests;

import com.codeborne.selenide.Configuration;
import dion_tests.pages.LoginPage;
import dion_tests.pages.SearchResultPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.stream.Stream;

@Epic("Поиск")
@Feature("Поиск по видео и каналам")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class SearchTest {

    private SearchResultPage searchPage;

    @BeforeAll
    static void setupEnv() {
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10_000;
        new LoginPage().openPage().login();          
    }

    @BeforeEach
    void openSearch() { searchPage = new SearchResultPage().openPage(); }


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("01. Поиск находит нужное видео")
    @Story("Поиск видео")
    void shouldFindVideo() {
        searchPage.searchFor("Demo video #1")
                  .switchToFilter("videos")
                  .shouldContainVideo("Demo video #1");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("02. Поиск находит нужный канал")
    @Story("Поиск каналов")
    void shouldFindChannel() {
        searchPage.searchFor("QA Automation Channel")
                  .switchToFilter("channels")
                  .shouldContainChannel("QA Automation Channel");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("03. Фильтр «Видео» не содержит каналов")
    @Story("Фильтрация результатов")
    void videoFilterShouldHideChannels() {
        searchPage.searchFor("music")
                  .switchToFilter("videos")
                  .channelsShouldBeEmpty();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("04. Фильтр «Каналы» не содержит видео")
    @Story("Фильтрация результатов")
    void channelFilterShouldHideVideos() {
        searchPage.searchFor("game")
                  .switchToFilter("channels")
                  .videosShouldBeEmpty();
    }


    @ParameterizedTest(name="05.{index} Пустые результаты по запросу: \"{0}\"")
    @MethodSource("gibberishQueries")
    @Severity(SeverityLevel.MINOR)
    @Story("Пустая выдача")
    void shouldShowEmptyResult(String junk) {
        searchPage.searchFor(junk)
                  .shouldShowEmptyState();
    }

    static Stream<String> gibberishQueries() {
        return Stream.of("asdj12312lkjh", "☺☻♥♦", "非存在的视频");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("06. Ошибка при слишком коротком поисковом запросе")
    @Story("Валидация запроса")
    void shouldValidateShortQuery() {
        searchPage.searchFor("a")      // < минимальной длины
                  .shouldShowError("Введите минимум 2 символа");
    }

    @Test
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("07. Поиск игнорирует пробелы по краям")
    @Story("Тримминг запроса")
    void shouldTrimSpaces() {
        searchPage.searchFor("   Demo video #1   ")
                  .switchToFilter("videos")
                  .shouldContainVideo("Demo video #1");
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("08. Догрузка результатов при скролле")
    @Story("Пагинация")
    void shouldLoadMoreResultsOnScroll() {
        searchPage.searchFor("test")
                  .switchToFilter("videos");

        int initial = searchPage.videoCards.size();
        executeJavaScript("window.scrollTo(0, document.body.scrollHeight)");
        $(".search-spinner").should(appear).should(disappear);
        Assertions.assertTrue(
                searchPage.videoCards.size() > initial,
                "Ожидалось больше карточек после скролла"
        );
    }
}
