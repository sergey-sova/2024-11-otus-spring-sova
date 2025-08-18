package ru.otus.hw.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.mongock.changelog.TestDatabaseChangelog.AUTHOR1;
import static ru.otus.hw.mongock.changelog.TestDatabaseChangelog.AUTHOR3;
import static ru.otus.hw.mongock.changelog.TestDatabaseChangelog.BOOK3;
import static ru.otus.hw.mongock.changelog.TestDatabaseChangelog.GENRE1;
import static ru.otus.hw.mongock.changelog.TestDatabaseChangelog.GENRE2;
import static ru.otus.hw.mongock.changelog.TestDatabaseChangelog.GENRE4;
import static ru.otus.hw.mongock.changelog.TestDatabaseChangelog.GENRE5;

@DisplayName("Интеграционные тесты сервиса с книгами")
@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({BookServiceImpl.class})
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

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
        assertThat(actualBooks).containsExactlyElementsOf(getBooks(getAuthors(), getGenres()));
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        String expectedTitle = "NewBook";
        Author expectedAuthor = AUTHOR1;
        List<Genre> expectedGenres = List.of(GENRE1, GENRE2);

        BookDto returnedBook = bookService.insert(expectedTitle, expectedAuthor.getId(),
                expectedGenres.stream().map(g -> g.getId()).collect(Collectors.toSet()));
        assertThat(returnedBook.title()).isEqualTo(expectedTitle);
        assertThat(returnedBook.author()).isEqualTo(AuthorDto.from(expectedAuthor));
        assertThat(returnedBook.genres()).isEqualTo(expectedGenres.stream().map(g -> GenreDto.from(g)).toList());

        Optional<Book> savedBook = bookRepository.findById(returnedBook.id());
        assertThat(savedBook).isPresent();
        assertThat(savedBook.get().getTitle()).isEqualTo(expectedTitle);
        assertThat(savedBook.get().getAuthor()).isEqualTo(expectedAuthor);
        assertThat(savedBook.get().getGenres()).isEqualTo(expectedGenres);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        String expectedTitle = "UpdatedBook";
        Author expectedAuthor = AUTHOR3;
        List<Genre> expectedGenres = List.of(GENRE4, GENRE5);

        BookDto returnedBook = bookService.update(BOOK3.getId(), expectedTitle, expectedAuthor.getId(),
                expectedGenres.stream().map(g -> g.getId()).collect(Collectors.toSet()));
        assertThat(returnedBook.title()).isEqualTo(expectedTitle);
        assertThat(returnedBook.author()).isEqualTo(AuthorDto.from(expectedAuthor));
        assertThat(returnedBook.genres()).isEqualTo(expectedGenres.stream().map(g -> GenreDto.from(g)).toList());

        Optional<Book> savedBook = bookRepository.findById(BOOK3.getId());
        assertThat(savedBook).isPresent();
        assertThat(savedBook.get().getTitle()).isEqualTo(expectedTitle);
        assertThat(savedBook.get().getAuthor()).isEqualTo(expectedAuthor);
        assertThat(savedBook.get().getGenres()).isEqualTo(expectedGenres);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(bookRepository.findById(BOOK3.getId())).isPresent();
        bookService.deleteById(BOOK3.getId());
        assertThat(bookRepository.findById(BOOK3.getId())).isEmpty();
    }

    private static List<AuthorDto> getAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(String.valueOf(id), "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto(String.valueOf(id), "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getBooks(List<AuthorDto> authors, List<GenreDto> genres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(String.valueOf(id),
                        "Book_" + id,
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