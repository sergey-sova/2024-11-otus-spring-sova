package ru.otus.hw.services;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Интеграционные тесты сервиса комментариев")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({CommentServiceImpl.class, JpaCommentRepository.class, JpaBookRepository.class})
class CommentServiceTest {

    private static final long FIRST_BOOK_ID = 1L;

    @Autowired
    private CommentService commentService;

    private List<CommentDto> comments;

    @BeforeEach
    void setUp() {
        comments = getComments();
    }

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
        var actualBookComments = commentService.findByBookId(FIRST_BOOK_ID);
        assertThat(actualBookComments).containsExactlyElementsOf(comments);
        actualBookComments.forEach(System.out::println);
    }

    private static List<CommentDto> getComments() {
        return IntStream.range(1, 3).boxed()
                .map(id -> new CommentDto(id, 1, "Comment_1_" + id))
                .toList();
    }
}