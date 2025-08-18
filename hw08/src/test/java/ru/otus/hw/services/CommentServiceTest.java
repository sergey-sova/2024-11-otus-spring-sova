package ru.otus.hw.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.mongock.changelog.TestDatabaseChangelog.BOOK1;
import static ru.otus.hw.mongock.changelog.TestDatabaseChangelog.BOOK3;
import static ru.otus.hw.mongock.changelog.TestDatabaseChangelog.COMMENT31;

@DisplayName("Интеграционные тесты сервиса комментариев")
@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({CommentServiceImpl.class})
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getComments")
    void shouldReturnCorrectCommentById(CommentDto expectedComment) {
        var actualComment = commentService.findById(expectedComment.id());
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список всех комментариев по книге")
    @Test
    void shouldReturnCorrectCommentsList() {
        var actualBookComments = commentService.findByBookId(BOOK1.getId());
        assertThat(actualBookComments).containsExactlyElementsOf(getComments());
        actualBookComments.forEach(System.out::println);
    }

    @DisplayName("должен добавлять комментарий к книге")
    @Test
    void shouldSaveNewComment() {
        String expectedBookId = BOOK1.getId();
        String expectedText = "NewComment";

        CommentDto returnedComment = commentService.add(expectedBookId, expectedText);
        assertThat(returnedComment.bookId()).isEqualTo(expectedBookId);
        assertThat(returnedComment.text()).isEqualTo(expectedText);

        Optional<Comment> savedComment = commentRepository.findById(returnedComment.id());
        assertThat(savedComment).isPresent();
        assertThat(savedComment.get().getBookId()).isEqualTo(expectedBookId);
        assertThat(savedComment.get().getText()).isEqualTo(expectedText);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        String expectedText = "Updated";

        CommentDto returnedComment = commentService.update(COMMENT31.getId(), expectedText);
        assertThat(returnedComment.text()).isEqualTo(expectedText);

        Optional<Comment> savedComment = commentRepository.findById(COMMENT31.getId());
        assertThat(savedComment).isPresent();
        assertThat(savedComment.get().getText()).isEqualTo(expectedText);
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    void shouldDeleteComment() {
        assertThat(commentRepository.findById(COMMENT31.getId())).isPresent();
        commentService.deleteById(COMMENT31.getId());
        assertThat(commentRepository.findById(COMMENT31.getId())).isEmpty();
    }

    private static List<CommentDto> getComments() {
        return IntStream.range(1, 3).boxed()
                .map(id -> new CommentDto(String.valueOf(id), BOOK1.getId(), "Comment_1_" + id))
                .toList();
    }
}