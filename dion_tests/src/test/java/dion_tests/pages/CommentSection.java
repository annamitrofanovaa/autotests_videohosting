package dion_tests.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class CommentSection {

    private final SelenideElement commentInput   = $("textarea#new-comment");
    private final SelenideElement sendBtn        = $("button#send-comment");
    private final ElementsCollection comments    = $$(".comment-item");
    private final SelenideElement loadMoreBtn    = $("button#load-more-comments");
    private final SelenideElement sortDropdown   = $("div#sort-comments");
    private final SelenideElement errorToast     = $(".toast-error");
    private final SelenideElement successToast   = $(".toast-success");

    @Step("Пишем комментарий: {text}")
    public CommentSection addComment(String text) {
        commentInput.shouldBe(visible).setValue(text);
        sendBtn.shouldBe(enabled).click();
        return this;
    }

    @Step("Редактируем комментарий «{oldText}» → «{newText}»")
    public CommentSection editComment(String oldText, String newText) {
        comments.findBy(text(oldText))
                .hover().$("button.edit").click();
        $("textarea.edit-area").clear();
        $("textarea.edit-area").setValue(newText);
        $("button.save-edit").click();
        return this;
    }

    @Step("Удаляем комментарий «{text}»")
    public CommentSection deleteComment(String text) {
        comments.findBy(text(text))
                .hover().$("button.delete").click();
        $("button.confirm-delete").click();
        return this;
    }

    @Step("Ставим лайк комментарию «{text}»")
    public CommentSection likeComment(String text) {
        comments.findBy(text(text)).$("button.like").click();
        return this;
    }

    @Step("Отвечаем на комментарий «{parentText}» текстом: {reply}")
    public CommentSection replyToComment(String parentText, String reply) {
        comments.findBy(text(parentText))
                .$("button.reply").click();
        $("textarea#reply-input").setValue(reply);
        $("button#send-reply").click();
        return this;
    }

    @Step("Загружаем ещё комментарии")
    public CommentSection loadMore() {
        loadMoreBtn.shouldBe(visible).click();
        loadMoreBtn.should(disappear); // если исчезает после последней страницы
        return this;
    }

    @Step("Сортируем комментарии по: {criteria}")
    public CommentSection sortBy(String criteria) {
        sortDropdown.click();
        $(byText(criteria)).click();  
        return this;
    }

    @Step("Комментарий «{text}» должен отображаться")
    public void shouldContain(String text) {
        comments.findBy(text(text))
                .shouldBe(visible, Duration.ofSeconds(10));
    }

    @Step("Комментарий «{text}» не должен отображаться")
    public void shouldNotContain(String text) {
        comments.findBy(text(text)).shouldNot(exist);
    }

    @Step("Ответ «{reply}» должен быть под комментарием-родителем «{parent}»")
    public void replyShouldBeUnder(String parent, String reply) {
        comments.findBy(text(parent))
                .$(".replies").find(byText(reply))
                .shouldBe(visible);
    }

    @Step("Для комментария «{text}» счётчик лайков = {expected}")
    public void likeCountShouldBe(String text, int expected) {
        comments.findBy(text(text))
                .$("span.like-count").shouldHave(exactText(String.valueOf(expected)));
    }

    @Step("Показывается ошибка: {msg}")
    public void shouldShowError(String msg) {
        errorToast.shouldBe(visible).shouldHave(text(msg));
    }

    @Step("Успешное действие подтверждено тостом")
    public void shouldShowSuccess() {
        successToast.shouldBe(visible);
    }

    public int totalComments() {
        return comments.size();
    }
}
