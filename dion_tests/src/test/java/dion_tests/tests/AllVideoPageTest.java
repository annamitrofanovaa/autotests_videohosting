package dion_tests.tests;

import dion_tests.pages.AllVideoPage;
import dion_tests.utils.BaseTest;


import static com.codeborne.selenide.Selenide.executeJavaScript;
import java.util.Random;

import static com.codeborne.selenide.Selenide.executeJavaScript;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class AllVideoPageTest extends BaseTest {

    AllVideoPage allVideoPage = new AllVideoPage();
 
    @Test
    //@Disabled
    @Order(1)
   // @Disabled
    @DisplayName("UI: Проверка работы всех фильтров и сброса")
    public void shouldClickFiltersAndReset() {
        allVideoPage.open("https://frontend-test.dev.dion.vc/video/all");
        allVideoPage.openFilterDropdown();
        allVideoPage.clickAllFiltersAndReset();
        // 🔹 Рандомный клик по экрану
        int x = new Random().nextInt(1920);
        int y = new Random().nextInt(1080);
        String script = "document.elementFromPoint(" + x + ", " + y + ").click();";
        executeJavaScript(script);
        System.out.println("🎯 Рандомный клик по экрану: x=" + x + ", y=" + y);
    }

    @Test
    @Order(2)
    @DisplayName("UI: Проверка открытия и выбора всех вариантов сортировки")
    public void shouldOpenAndCycleSortOptions() {
        allVideoPage.open("https://frontend-test.dev.dion.vc/video/all");
        System.out.println("🔽 Открыли выпадающий список сортировки");
        
        allVideoPage.selectSortOption("Сначала новые");
        // 🔽 Шаг 1: Открыть меню сортировки и выбрать "По лайкам"
        allVideoPage.clickSortDropdown("По лайкам");
        allVideoPage.selectSortOption("По лайкам");

        // 🔽 Шаг 2: Снова открыть и выбрать "По просмотрам"
        allVideoPage.clickSortDropdown("По просмотрам");
        allVideoPage.selectSortOption("По просмотрам");

        // 🔽 Шаг 3: Снова открыть и выбрать "По комментариям"
        allVideoPage.clickSortDropdown("По комментариям");
        allVideoPage.selectSortOption("По комментариям");

        // 🔽 Шаг 4: Вернуться к "Сначала новые"
        allVideoPage.clickSortDropdown("Сначала новые");
        allVideoPage.selectSortOption("Сначала новые");
        int x = new Random().nextInt(1920);
        int y = new Random().nextInt(1080);
        String script = "document.elementFromPoint(" + x + ", " + y + ").click();";
        executeJavaScript(script);
        System.out.println("✅ Протестировали все пункты сортировки по очереди");
    }



}
