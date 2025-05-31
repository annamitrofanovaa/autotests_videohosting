package dion_tests.tests;

import com.codeborne.selenide.Configuration;
import dion_tests.pages.CommentSection;
import dion_tests.pages.LoginPage;
import dion_tests.pages.VideoPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

@Epic("Комментарии")
@Feature("Работа с комментариями под видео")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class CommentsTest {

    private static final String VIDEO_ID = "a1b2c3d4"; // любое тестовое видео

    private VideoPage videoPage;
    private CommentSection comments;

    @BeforeAll
    static void login() {
        Configuration.browserSize = "1920x1080";
        new LoginPage().openPage().login();
    }

    @BeforeEach
    void openVideo() {
        videoPage = new VideoPage().open(VIDEO_ID);
        comments  = new CommentSection();
    }


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("01. Пользователь может оставить комментарий")
    @Story("Создание")
    void shouldAddComment() {
        String text = "Autotest comment " + System.currentTimeMillis();
        int before = comments.totalComments();

        comments.addComment(text)
                .shouldShowSuccess()
                .shouldContain(text);

        Assertions.assertEquals(before + 1, comments.totalComments(),
                "Счётчик комментариев должен увеличиться на 1");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("02. Пользователь может редактировать комментарий")
    @Story("Редактирование")
    void shouldEditComment() {
        String original = "Comment to edit " + System.currentTimeMillis();
        String edited   = original + " (edited)";

        comments.addComment(original).shouldShowSuccess();
        comments.editComment(original, edited)
                .shouldShowSuccess()
                .shouldContain(edited);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("03. Пользователь может удалить комментарий")
    @Story("Удаление")
    void shouldDeleteComment() {
        String text = "Comment to delete " + System.currentTimeMillis();

        comments.addComment(text).shouldShowSuccess();
        comments.deleteComment(text)
                .shouldShowSuccess()
                .shouldNotContain(text);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("04. Лайк увеличивает счётчик лайков")
    @Story("Лайки")
    void shouldLikeComment() {
        String text = "Like me " + System.currentTimeMillis();

        comments.addComment(text).shouldShowSuccess();
        comments.likeComment(text);
        comments.likeCountShouldBe(text, 1);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("05. Ответ отображается под родительским комментарием")
    @Story("Ответы")
    void shouldReplyToComment() {
        String parent = "Parent " + System.currentTimeMillis();
        String child  = "Child reply";

        comments.addComment(parent).shouldShowSuccess();
        comments.replyToComment(parent, child)
                .shouldShowSuccess();
        comments.replyShouldBeUnder(parent, child);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("06. Сортировка «Новые» отображает последний комментарий первым")
    @Story("Сортировка")
    void shouldSortByNewest() {
        String newest = "Newest " + System.currentTimeMillis();
        comments.addComment("Old").shouldShowSuccess();
        comments.addComment(newest).shouldShowSuccess();

        comments.sortBy("Новые");
        comments.shouldContain(newest); // первый элемент:
        $$("div.comment-item").first().shouldHave(text(newest));
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("07. Догрузка комментариев при нажатии «Показать ещё»")
    @Story("Пагинация")
    void shouldLoadMoreComments() {
        int before = comments.totalComments();
        comments.loadMore();
        Assertions.assertTrue(comments.totalComments() > before,
                "После загрузки должно быть больше комментариев");
    }


    @ParameterizedTest(name = "08.{index} Ошибка при невалидном комментарии: \"{0}\"")
    @MethodSource("invalidTexts")
    @Severity(SeverityLevel.MINOR)
    @Story("Валидация ввода")
    void shouldValidateComment(String badText) {
        comments.addComment(badText)
                .shouldShowError("Комментарий должен содержать 1–500 символов");
    }

    private static Stream<String> invalidTexts() {
        return Stream.of(
                "", " ",               // пустой
                "a".repeat(501)        // >500 символов
        );
    }

    @Test
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("09. Блокировка на спам: один и тот же комментарий подряд")
    @Story("Спам-защита")
    void shouldBlockDuplicateComment() {
        String text = "Spam test";

        comments.addComment(text).shouldShowSuccess();
        comments.addComment(text)
                .shouldShowError("Повторяющийся комментарий");
    }

    @Test
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("10. Ошибка 500 отображается тостом")
    @Story("Обработка ошибок сервера")
    void shouldDisplayServerError() {
        videoPage.open("videoReturning500OnComments"); // спец-видео/мок
        new CommentSection().addComment("test");
        new CommentSection().shouldShowError("Не удалось отправить комментарий");
    }
}
