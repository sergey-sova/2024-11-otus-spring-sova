package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public Optional<BookDto> findById(String id) {
        return BookDto.from(bookRepository.findById(id));
    }

    @Override
    public List<BookDto> findAll() {
        return BookDto.from(bookRepository.findAll());
    }

    @Override
    public BookDto insert(String title, String authorId, Set<String> genresIds) {
        return BookDto.from(save(null, title, authorId, genresIds));
    }

    @Override
    public BookDto update(String id, String title, String authorId, Set<String> genresIds) {
        return BookDto.from(save(id, title, authorId, genresIds));
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    private Book save(String id, String title, String authorId, Set<String> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genres = genreRepository.findAllById(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var builder = Book.builder().title(title).author(author).genres(genres);
        if (id != null) {
            builder.id(id);
        }
        return bookRepository.save(builder.build());
    }
}
