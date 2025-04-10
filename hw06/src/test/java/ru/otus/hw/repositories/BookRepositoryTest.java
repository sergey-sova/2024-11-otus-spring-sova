package ru.otus.hw.repositories;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами")
@DataJpaTest
@Import({JpaBookRepository.class})
class BookRepositoryTest {

    private static final long FIRST_BOOK_ID = 1L;

    private static final long NEW_BOOK_ID = 4L;

    private static final long FIRST_AUTHOR_ID = 1L;

    private static final long SECOND_AUTHOR_ID = 2L;

    private static final long FIRST_GENRE_ID = 1L;

    private static final long SECOND_GENRE_ID = 2L;

    private static final long FOURTH_GENRE_ID = 4L;

    private static final long FIFTH_GENRE_ID = 5L;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать книгу по id")
    void shouldReturnCorrectBookById() {
        var actualBook = bookRepository.findById(FIRST_BOOK_ID);
        var expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepository.findAll();

        assertThat(actualBooks).hasSize(3);
        for (var book : actualBooks) {
            var expectedBook = em.find(Book.class, book.getId());
            assertThat(book)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedBook);
        }
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var author1 = em.find(Author.class, FIRST_AUTHOR_ID);
        var genre1 = em.find(Genre.class, FIRST_GENRE_ID);
        var genre2 = em.find(Genre.class, SECOND_GENRE_ID);
        var expectedBook = new Book(0, "BookTitle_10500", author1, List.of(genre1, genre2));

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, NEW_BOOK_ID)).isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var author2 = em.find(Author.class, SECOND_AUTHOR_ID);
        var genre4 = em.find(Genre.class, FOURTH_GENRE_ID);
        var genre5 = em.find(Genre.class, FIFTH_GENRE_ID);
        var expectedBook = new Book(FIRST_BOOK_ID, "BookTitle_10500", author2, List.of(genre4, genre5));

        assertThat(em.find(Book.class, FIRST_BOOK_ID)).isNotEqualTo(expectedBook);

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, FIRST_BOOK_ID)).isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(em.find(Book.class, FIRST_BOOK_ID)).isNotNull();
        bookRepository.deleteById(FIRST_BOOK_ID);
        assertThat(em.find(Book.class, FIRST_BOOK_ID)).isNull();
    }
}