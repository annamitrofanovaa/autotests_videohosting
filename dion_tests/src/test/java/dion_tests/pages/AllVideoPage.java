package dion_tests.pages;

import static com.codeborne.selenide.Selenide.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;

import static com.codeborne.selenide.Condition.*;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;


import static com.codeborne.selenide.Selectors.byText;

public class AllVideoPage {

    // 🔗 Открытие страницы по ссылке
    public void open(String url) {
        com.codeborne.selenide.Selenide.open(url); // ✅ вызывает метод Selenide

        System.out.println("🌐 [AllVideoPage] Открыли страницу: " + url);
    }

    // 🔽 Открытие выпадающего фильтра
    public void openFilterDropdown() {
        $(byText("Выберите фильтр")).scrollIntoView(true).click();
        System.out.println("🎛️ [AllVideoPage] Открыли фильтр");
    }

    // ✅ Клик по обоим фильтрам и кнопке сброса
    public void clickAllFiltersAndReset() {
        System.out.println("🎯 [AllVideoPage] Кликаем по фильтрам и сбрасываем их");

        $(byText("Мои подписки")).shouldBe(visible).click();
        $(byText("Еще не просмотренные")).shouldBe(visible).click();
        $(byText("Сбросить фильтры")).shouldBe(visible).click();
    }

    public void clickSortDropdown(String optionText) {
        $(byText(optionText)).click();
    }
    
    // Выбрать пункт сортировки по тексту

    public void selectSortOption(String optionText) {
        $(byText(optionText)).click();
        System.out.println("👉 Выбрали сортировку: " + optionText);
    }
    public void clickOkButtonTwice() {
        // Первый клик
        $(byText("Хорошо")).shouldBe(visible).click();
// Клик по крестику закрытия попапа

        System.out.println("☑ Первый клик по 'Хорошо'");
        sleep(2000);
        // Второй клик (если элемент появляется заново, подожди снова)
        $(byText("Хорошо")).shouldBe(visible).click();
// Клик по крестику закрытия попапа

        System.out.println("☑ Второй клик по 'Хорошо'");
    }
    public void checkSidebarElementsVisible() {
        $("#search-field").shouldBe(visible); // поле поиска
        $(byText("Все видео")).shouldBe(visible);
        $(byText("Каналы")).shouldBe(visible);
        $(byText("Мои видео")).shouldBe(visible);
        $(byText("История")).shouldBe(visible);
        $(byText("ВСЕ")).shouldBe(visible);
    }

    public void shouldRenderFirstVideoCardProperly() {
        System.out.println("📦 Получаем первую карточку видео...");
        SelenideElement firstCard = $$("div.MuiGrid2-root.MuiGrid2-direction-xs-row").first();
    
        // Превью
        System.out.println("🖼 Проверяем наличие превью (img или video)...");
        firstCard.$("div.MuiBox-root").shouldBe(visible);
    
        // Заголовок
        System.out.println("📝 Проверяем наличие заголовка (h6)...");
        firstCard.$("h6").shouldBe(visible);
    
        // Подпись под заголовком
        System.out.println("📄 Проверяем наличие подписи под заголовком (span или p)...");
        firstCard.$("span, p").shouldBe(visible);
    
        // Дата
        System.out.println("📅 Проверяем наличие текста с датой (например, 2025)...");
        firstCard.shouldHave(text("2025"));
    
        // Лайк и просмотры
        System.out.println("🔍 Проверяем иконку лайка...");
        firstCard.$("svg.MuiSvgIcon-root").shouldBe(visible);
        System.out.println("✅ Иконка лайка найдена");

        // Количество лайков
        System.out.println("🔍 Проверяем количество лайков...");
        firstCard.$("span.MuiTypography-root").shouldBe(visible);
        System.out.println("✅ Количество лайков отображается");
    
        
    
        System.out.println("✅ Проверка карточки видео завершена успешно!");
    }
    
    public void shouldScrollPageWithoutErrors() {
        // Открываем страницу
        // Проверка, что начальный список карточек есть
        ElementsCollection cardsBeforeScroll = $$("div.MuiGrid2-root.MuiGrid2-direction-xs-row");
        int initialSize = cardsBeforeScroll.size();
        System.out.println("🟢 Найдено карточек до скролла: " + initialSize);
        assertTrue(initialSize > 0, "Карточки должны быть до скролла");

        // Скроллим вниз (например, 3 раза)
        for (int i = 0; i < 3; i++) {
            executeJavaScript("window.scrollBy(0, document.body.scrollHeight)");
            Selenide.sleep(1500); // небольшая пауза для подгрузки (если есть)
        }

        // Проверка, что карточек стало больше или они остались
        ElementsCollection cardsAfterScroll = $$("div.MuiGrid2-root.MuiGrid2-direction-xs-row");
        int afterSize = cardsAfterScroll.size();
        System.out.println("🟢 Найдено карточек после скролла: " + afterSize);
        assertTrue(afterSize >= initialSize, "Карточек должно быть не меньше после скролла");
    }

    public void shouldScrollToBannerAndSeeIt() {
        $("[class*='css-1du517f']")
        .scrollIntoView(true)
        .shouldBe(visible);

        System.out.println("✅ Кнопка с классом 'css-1du517f' видима");
    }

    public void shouldCloseBannerOnClick() {
        // Скроллим до кнопки закрытия
        SelenideElement closeButton = $("[class*='css-1du517f']");
        closeButton.scrollIntoView(true).shouldBe(visible);
    
        System.out.println("✅ Крестик найден и видим");
    
        // Кликаем по крестику
        closeButton.click();
    
        System.out.println("🖱 Клик по крестику выполнен");
        sleep(3000);
        // Проверяем, что баннер исчез
        // Проверяем, что баннер исчез (его img нет на странице)
        $("img.MuiBox-root.css-se6dv").shouldNotBe(visible);
    
        System.out.println("✅ Баннер успешно исчез со страницы");
    }
    public void checkFirstVideoIsFromChannel(String expectedChannelName) {
        System.out.println("🔎 Проверяем, что первое видео принадлежит каналу: " + expectedChannelName);
    
        // Находим первую карточку видео
        SelenideElement firstCard = $$("div.MuiGrid2-root.MuiGrid2-direction-xs-row").first();
    
        // Проверяем, что текст с названием канала присутствует
        firstCard.shouldHave(text(expectedChannelName));
    
        System.out.println("✅ Название канала в первой карточке соответствует ожиданию!");
    }
    
}
