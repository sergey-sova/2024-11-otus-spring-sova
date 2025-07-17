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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Интеграционные тесты сервиса с книгами")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({BookServiceImpl.class, JpaBookRepository.class, JpaGenreRepository.class, JpaAuthorRepository.class})
class BookServiceTest {

    @Autowired
    private BookService bookService;

    private List<AuthorDto> authors;

    private List<GenreDto> genres;

    private List<BookDto> books;

    @BeforeEach
    void setUp() {
        authors = getAuthors();
        genres = getGenres();
        books = getBooks(authors, genres);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getBooks")
    void shouldReturnCorrectBookById(BookDto expectedBook) {
        var actualBook = bookService.findById(expectedBook.id());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookService.findAll();
        assertThat(actualBooks).containsExactlyElementsOf(books);
        actualBooks.forEach(System.out::println);
    }

    private static List<AuthorDto> getAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getBooks(List<AuthorDto> authors, List<GenreDto> genres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id,
                        "BookTitle_" + id,
                        authors.get(id - 1),
                        genres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<BookDto> getBooks() {
        var authors = getAuthors();
        var genres = getGenres();
        return getBooks(authors, genres);
    }
}