package dion_tests.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

import java.io.File;

import org.openqa.selenium.By;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;

public class ChannelCreation {

    public void open(String url) {
        //open(url);
        com.codeborne.selenide.Selenide.open(url);
    }
    public void openChannelsPage() {
        open("https://frontend-test.dev.dion.vc/video/channels");
    }


    public void uploadChannelImage() {
        System.out.println("🖼 Загружаем изображение канала");

        // Указываем путь к картинке
        File image = new File("C:/Dion_tests/dion_tests/test_img/1.jpg");

        // Ищем настоящий input[type='file'] и загружаем
        $("input[type='file']").uploadFile(image);
        $(byText("Сохранить")).closest("button").click();
        System.out.println("✅ Фото загружено");
    }
    


    public void createChannelDraft(String name, String description, String accessType) {
        System.out.println("[ChannelPage] Открываем список каналов");
        openChannelsPage();
    
        System.out.println("[ChannelPage] Нажимаем 'Создать канал'");
        $(By.xpath("//button[.='Создать канал']")).shouldBe(visible).click();
    
        System.out.println("[ChannelPage] Вводим название канала");
        $(By.xpath("//input[@placeholder='Придумайте понятное название']"))
            .shouldBe(visible)
            .setValue(name);
    
        System.out.println("[ChannelPage] Вводим описание канала");
        $(By.xpath("//textarea[@placeholder='Описание канала']"))
            .shouldBe(visible)
            .setValue(description);
    
        uploadChannelImage();
        System.out.println("[ChannelPage] Нажимаем 'Далее'");
        $(By.xpath("//button[.='Далее']")).shouldBe(visible).click();
        Selenide.sleep(2000);
    
        // Теперь выбираем доступ, который передал тест
        selectAccessType(accessType);
    
        SelenideElement finalCreateButton = $(By.xpath("//button[.//span[text()='Создать канал']]"));
        finalCreateButton.scrollIntoView(true);
        Selenide.sleep(1000);
        finalCreateButton.shouldBe(visible).click();
        Selenide.sleep(5000);
    
        System.out.println("✅ [ChannelPage] Канал успешно создан");
    }
    

    public void selectUsers() {
        System.out.println("👥 Выбираем пользователей");
    
        // Открыть модалку
        $(byText("Добавить пользователей")).shouldBe(visible).click();
    
        // Вводим имя в поле поиска
        $("input[placeholder='Поиск']").shouldBe(visible).setValue("Anna");
    
        // Ждём, пока появится чекбокс для пользователя и кликаем
        $("input[type='checkbox']").parent().click();
    
        // Ждём, пока кнопка "Добавить" станет активной и жмём её
        $(byText("Добавить")).shouldBe(enabled).click();
    
        System.out.println("✅ Пользователь выбран и добавлен");
    }
    
    
    public void selectAccessType(String type) {
        if (!type.equals("Доступен сотрудникам моей компании")) {
            
        
            System.out.println("🔐 Выбираем тип доступа: " + type);
            $("[role='combobox']").click();
            Selenide.sleep(2000); 
            $(byText(type)).shouldBe(visible).click();
            $("[role='combobox']").shouldHave(text(type));
            if (type.equals("Доступен выбранным пользователям")) {
                selectUsers(); // вызов метода, который должен быть реализован ниже
            }
        }

    }

}
