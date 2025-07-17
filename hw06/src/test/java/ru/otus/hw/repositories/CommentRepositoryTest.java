package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями")
@DataJpaTest
@Import({JpaCommentRepository.class})
class CommentRepositoryTest {

    private static final long FIRST_BOOK_ID = 1L;

    private static final long FIRST_COMMENT_ID = 1L;

    private static final long NEW_COMMENT_ID = 3L;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать комментарий по id")
    void shouldReturnCorrectCommentById() {
        var actualComment = commentRepository.findById(FIRST_COMMENT_ID);
        var expectedComment = em.find(Comment.class, FIRST_COMMENT_ID);
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список всех комментариев по книге")
    @Test
    void shouldReturnCorrectCommentsList() {
        var actualComments = commentRepository.findByBookId(FIRST_BOOK_ID);
        assertThat(actualComments).hasSize(2);
        for (var comment : actualComments) {
            var expectedComment = em.find(Comment.class, comment.getId());
            assertThat(comment)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedComment);
        }
    }

    @DisplayName("должен сохранять новый комментарий к книге")
    @Test
    void shouldSaveNewComment() {
        Book book1 = em.find(Book.class, FIRST_BOOK_ID);
        var expectedComment = new Comment(0, book1, "Comment_1_10500");

        var returnedComment = commentRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(em.find(Comment.class, NEW_COMMENT_ID)).isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        Book book1 = em.find(Book.class, FIRST_BOOK_ID);
        var expectedComment = new Comment(FIRST_COMMENT_ID, book1, "Comment_1_10500");

        assertThat(em.find(Comment.class, expectedComment.getId())).isNotEqualTo(expectedComment);

        var returnedComment = commentRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(em.find(Comment.class, FIRST_COMMENT_ID)).isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        assertThat(em.find(Comment.class, FIRST_COMMENT_ID)).isNotNull();
        commentRepository.deleteById(FIRST_COMMENT_ID);
        assertThat(em.find(Comment.class, FIRST_COMMENT_ID)).isNull();
    }
}