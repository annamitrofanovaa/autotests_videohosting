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
        $("h4").shouldHave(Condition.text("fdsdfsd"));
        $(byText("1 участник")).shouldBe(Condition.visible);
        $(byText("0 видео")).shouldBe(Condition.visible);
    
        $(byText("dfdsff")).shouldBe(Condition.visible);
    
        $(byText("Подробнее")).shouldBe(Condition.visible);
    
        $(byText("Подписаться")).shouldBe(Condition.visible);
        $(byText("Видео канала")).shouldBe(Condition.visible);
    
        $(byText("Плейлисты")).shouldBe(Condition.visible);
    
        $(byText("О канале")).shouldBe(Condition.visible);
    
        $(byText("На канале еще нет видео")).shouldBe(Condition.visible);
    
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
        $(byText("Подписаться")).shouldBe(visible);
    }
    @Step("Открываем страницу всех каналов")
    public void openChannelsPage() {
        open("https://frontend-test.dev.dion.vc/video/channels");
    }
    @Step("Создаём канал")
    public void createChannelDraft(String name, String description) {
        openChannelsPage();

        $(By.xpath("//button[.='Создать канал']")).shouldBe(visible).click();

        $(By.xpath("//input[@placeholder='Придумайте понятное название']"))
            .shouldBe(visible)
            .setValue(name);

        $(By.xpath("//textarea[@placeholder='Описание канала']"))
            .shouldBe(visible)
            .setValue(description);

        $(By.xpath("//button[.='Далее']")).shouldBe(visible).click();
        Selenide.sleep(2000); 
        SelenideElement finalCreateButton = $(By.xpath("//button[.//span[text()='Создать канал']]"));
        finalCreateButton.scrollIntoView(true);         // скроллим к кнопке
        Selenide.sleep(1000);                           // небольшая задержка
        finalCreateButton.shouldBe(visible).click();    // нажимаем
        Selenide.sleep(5000); 
    }
    @Step("Редактируем канал: дописываем 'НОВОЕ' в название и описание, меняем обложку")
    public void editChannelAndChangeBanner(String bannerFilePath) {
    $(byText("Редактировать")).shouldBe(visible).click();

    SelenideElement nameInput = $(By.xpath("//input[@placeholder='Придумайте понятное название']"));
    nameInput.shouldBe(visible);
    nameInput.sendKeys(" НОВОЕ");

    SelenideElement descriptionInput = $(By.xpath("//textarea[@placeholder='Описание канала']"));
    descriptionInput.shouldBe(visible);
    descriptionInput.sendKeys(" НОВОЕ");
   
    SelenideElement saveButton = $(byText("Сохранить"));
    saveButton.shouldBe(visible); 
    saveButton.scrollIntoView(true); 
    saveButton.click(); 
    Selenide.sleep(3000); 
    }

    @Step("Проверяем отображение рекламного блока на канале")
    public void verifyAdvertisementBlockVisible() {
        $(byText("Разместите свою рекламу")).shouldBe(visible);

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
    public void Unsubscribe() {

        refresh();                                 

        int centerX = 1920 / 2;                    
        int centerY = 1080 / 2;
        executeJavaScript(
                "document.elementFromPoint(" + centerX + ", " + centerY + ").click();");

        unsubscribeFromChannel();                  
    }
}
