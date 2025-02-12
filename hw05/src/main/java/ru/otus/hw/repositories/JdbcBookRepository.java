package ru.otus.hw.repositories;

import java.util.ArrayList;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbc;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        String sql = """
                 select * from books
                 left join authors on authors.id = books.author_id
                 left join books_genres on books_genres.book_id = books.id
                 left join genres on genres.id = books_genres.genre_id
                 where books.id = :id
                """;
        return jdbc.query(sql, Map.of("id", id), new BookResultSetExtractor());
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from books where id = :id", Map.of("id", id));
    }

    private List<Book> getAllBooksWithoutGenres() {
        String sql = """
                    select * from books
                    left join authors on authors.id = books.author_id
                """;
        return jdbc.query(sql, new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("select book_id, genre_id from books_genres", (rs, rowNum) ->
                new BookGenreRelation(rs.getLong("book_id"), rs.getLong("genre_id")));
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        booksWithoutGenres.forEach(book -> {
            List<Genre> bookGenres = relations.stream()
                    .filter(relation -> book.getId() == relation.bookId)
                    .map(relation -> genres.stream()
                                .filter(g -> relation.genreId == g.getId())
                                .findFirst()
                                .get())
                    .toList();
            book.setGenres(bookGenres);
        });
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        jdbc.update("insert into books (title, author_id) values(:title, :author_id)", params,
                keyHolder, new String[] {"id"});

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        jdbc.update("update books set title = :title, author_id = :author_id where id = :id",
                Map.of("id", book.getId(), "title", book.getTitle(), "author_id", book.getAuthor().getId()));

        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        final int genresCount = book.getGenres().size();
        var params = new MapSqlParameterSource[genresCount];
        for (int i = 0; i < genresCount; i ++) {
            params[i] = new MapSqlParameterSource();
            params[i].addValue("book_id", book.getId());
            params[i].addValue("genre_id", book.getGenres().get(i).getId());
        }
        jdbc.batchUpdate("insert into books_genres (book_id, genre_id) values (:book_id, :genre_id);", params);
    }

    private void removeGenresRelationsFor(Book book) {
        jdbc.update("delete from books_genres where book_id = :book_id", Map.of("book_id", book.getId()));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong("books.id"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(new Author(rs.getLong("authors.id"), rs.getString("full_name")));
            return book;
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Optional<Book>> {

        @Override
        public Optional<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = null;
            while (rs.next()) {
                if (book == null) {
                    book = new Book();
                    book.setId(rs.getLong("books.id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(new Author(rs.getLong("authors.id"), rs.getString("full_name")));
                    book.setGenres(new ArrayList<>());
                }
                Genre genre = new Genre();
                genre.setId(rs.getLong("genres.id"));
                genre.setName(rs.getString("name"));
                book.getGenres().add(genre);
            }
            return Optional.ofNullable(book);
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
