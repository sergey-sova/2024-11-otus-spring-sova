package ru.otus.hw.dto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import ru.otus.hw.models.Book;

public record BookDto (long id, String title, AuthorDto author, List<GenreDto> genres) {

    public static BookDto from(Book book) {
        return new BookDto(book.getId(), book.getTitle(), AuthorDto.from(book.getAuthor()),
                GenreDto.from(book.getGenres()));
    }

    public static Optional<BookDto> from(Optional<Book> book) {
        return book.map(BookDto::from);
    }

    public static List<BookDto> from(Collection<Book> books) {
        return books.stream().map(BookDto::from).toList();
    }
}
