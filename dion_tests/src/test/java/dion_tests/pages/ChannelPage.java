package dion_tests.pages;
import static com.codeborne.selenide.Selenide.*;

import java.io.File;

import org.openqa.selenium.By;
import static com.codeborne.selenide.Condition.visible;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import static com.codeborne.selenide.Condition.enabled;

import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;

public class ChannelPage {
    @Step("Открываем страницу: {url}")
    public void open(String url) {
        com.codeborne.selenide.Selenide.open(url);
    }
    @Step("Проверяем, что заголовок канала: {expectedTitle}")
    public void shouldHaveCorrectTitle(String expectedTitle) {
        $("h4").shouldHave(Condition.text(expectedTitle));
    }
    @Step("Проверяем наличие всех основных элементов канала")
    public void checkChannelElements() {
        //System.out.println("🔍 [ChannelPage] Начинаем проверку содержимого канала");
        //System.out.println("🔸 Проверяем заголовок канала");
        $("h4").shouldHave(Condition.text("fdsdfsd"));
        //System.out.println("🔸 Проверяем количество участников");
        $(byText("1 участник")).shouldBe(Condition.visible);
        //System.out.println("🔸 Проверяем количество видео");
        $(byText("0 видео")).shouldBe(Condition.visible);
    
        //System.out.println("🔸 Проверяем описание канала");
        $(byText("dfdsff")).shouldBe(Condition.visible);
    
        //System.out.println("🔸 Проверяем ссылку 'Подробнее'");
        $(byText("Подробнее")).shouldBe(Condition.visible);
    
        //System.out.println("🔸 Проверяем кнопку 'Подписаться'");
        $(byText("Подписаться")).shouldBe(Condition.visible);
        //System.out.println("✅ [ChannelPage] Кнопка 'Подписаться' видна");

        //System.out.println("🔸 Проверяем наличие вкладки 'Видео канала'");
        $(byText("Видео канала")).shouldBe(Condition.visible);
    
        //System.out.println("🔸 Проверяем наличие вкладки 'Плейлисты'");
        $(byText("Плейлисты")).shouldBe(Condition.visible);
    
        //System.out.println("🔸 Проверяем наличие вкладки 'О канале'");
        $(byText("О канале")).shouldBe(Condition.visible);
    
        //System.out.println("🔸 Проверяем, что отображается сообщение 'На канале еще нет видео'");
        $(byText("На канале еще нет видео")).shouldBe(Condition.visible);
    
        //System.out.println("✅ [ChannelPage] Все элементы канала успешно найдены и проверены");
    }
    
    @Step("Подписываемся на канал")
    public void subscribeToChannel() {
        SelenideElement subscribeButton = $(byText("Подписаться"));
        subscribeButton.shouldBe(Condition.visible).click();
        $(byText("Вы подписаны")).shouldBe(Condition.visible);
    }
    @Step("Отписываемся от канала")
    public void unsubscribeFromChannel() {
        SelenideElement subscribedButton = $(byText("Вы подписаны"));
        subscribedButton.shouldBe(visible).click();
        //System.out.println("🔄 [ChannelPage] Клик по 'Вы подписаны'");
        $(byText("Подписаться")).shouldBe(visible);
        //System.out.println("✅ [ChannelPage] Успешно отписались");
    }
    @Step("Открываем страницу всех каналов")
    public void openChannelsPage() {
        open("https://frontend-test.dev.dion.vc/video/channels");
    }
    @Step("Создаём канал")
    public void createChannelDraft(String name, String description) {
        //System.out.println("[ChannelPage] Открываем список каналов");
        openChannelsPage();

        //System.out.println("[ChannelPage] Нажимаем 'Создать канал'");
        $(By.xpath("//button[.='Создать канал']")).shouldBe(visible).click();

        //System.out.println("[ChannelPage] Вводим название канала");
        $(By.xpath("//input[@placeholder='Придумайте понятное название']"))
            .shouldBe(visible)
            .setValue(name);

        //System.out.println("[ChannelPage] Вводим описание канала");
        $(By.xpath("//textarea[@placeholder='Описание канала']"))
            .shouldBe(visible)
            .setValue(description);

        //System.out.println("[ChannelPage] Нажимаем 'Далее'");
        $(By.xpath("//button[.='Далее']")).shouldBe(visible).click();
        //System.out.println("[ChannelPage] Завершаем создание");
        Selenide.sleep(2000); 
        SelenideElement finalCreateButton = $(By.xpath("//button[.//span[text()='Создать канал']]"));
        finalCreateButton.scrollIntoView(true);         // скроллим к кнопке
        Selenide.sleep(1000);                           // небольшая задержка
        finalCreateButton.shouldBe(visible).click();    // нажимаем
        Selenide.sleep(5000); 
    }
    @Step("Редактируем канал: дописываем 'НОВОЕ' в название и описание, меняем обложку")
    public void editChannelAndChangeBanner(String bannerFilePath) {
    // Нажимаем кнопку "Редактировать"
    $(byText("Редактировать")).shouldBe(visible).click();

    // Дописываем "НОВОЕ" в название
    SelenideElement nameInput = $(By.xpath("//input[@placeholder='Придумайте понятное название']"));
    nameInput.shouldBe(visible);
    nameInput.sendKeys(" НОВОЕ");

    // Дописываем "НОВОЕ" в описание
    SelenideElement descriptionInput = $(By.xpath("//textarea[@placeholder='Описание канала']"));
    descriptionInput.shouldBe(visible);
    descriptionInput.sendKeys(" НОВОЕ");
   
    // Нажимаем кнопку "Сохранить" для всей формы
    SelenideElement saveButton = $(byText("Сохранить"));
    saveButton.shouldBe(visible); // Убедиться, что элемент найден
    saveButton.scrollIntoView(true); // Прокрутить до кнопки
    saveButton.click(); // Теперь клик
    Selenide.sleep(3000); // маленькая пауза для сохранения
    }

    @Step("Проверяем отображение рекламного блока на канале")
    public void verifyAdvertisementBlockVisible() {
        // Проверяем, что текст "Разместите свою рекламу" отображается
        $(byText("Разместите свою рекламу")).shouldBe(visible);

        // Можно дополнительно проверить весь текст блока, если нужно
        $(byText("Если вы владелец или модератор канала или владелец видео, вы можете разместить рекламный баннер в DION.Видео. Для размещения напишите по адресу:")).shouldBe(visible);
    }

    @Step("Переходим на вкладку 'Видео канала'")
    public void openVideosTab() {
        $(byText("Видео канала")).shouldBe(visible).click();
    }

    @Step("Переходим на вкладку 'Плейлисты'")
    public void openPlaylistsTab() {
        $(byText("Плейлисты")).shouldBe(visible).click();
    }

    @Step("Переходим на вкладку 'О канале'")
    public void openAboutTab() {
        $(byText("О канале")).shouldBe(visible).click();
    }
    
}
