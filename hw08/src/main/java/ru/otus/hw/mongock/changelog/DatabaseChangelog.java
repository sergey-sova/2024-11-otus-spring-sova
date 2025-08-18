package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "s.sova", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertData", author = "s.sova")
    public void insertBooks(AuthorRepository authorRepository, GenreRepository genreRepository,
                            BookRepository books, CommentRepository commentRepository) {
        Author author1 = authorRepository.save(Author.builder().fullName("Author_1").build());
        Author author2 = authorRepository.save(Author.builder().fullName("Author_2").build());
        Author author3 = authorRepository.save(Author.builder().fullName("Author_3").build());
        Genre genre1 = genreRepository.save(Genre.builder().name("Genre_1").build());
        Genre genre2 = genreRepository.save(Genre.builder().name("Genre_2").build());
        Genre genre3 = genreRepository.save(Genre.builder().name("Genre_3").build());
        Genre genre4 = genreRepository.save(Genre.builder().name("Genre_4").build());
        Genre genre5 = genreRepository.save(Genre.builder().name("Genre_5").build());
        Genre genre6 = genreRepository.save(Genre.builder().name("Genre_6").build());
        var book1 = books.save(Book.builder().title("Book_1").author(author1).genres(List.of(genre1, genre2)).build());
        var book2 = books.save(Book.builder().title("Book_2").author(author2).genres(List.of(genre3, genre4)).build());
        var book3 = books.save(Book.builder().title("Book_3").author(author3).genres(List.of(genre5, genre6)).build());
        commentRepository.save(Comment.builder().bookId(book1.getId()).text("Comment_1_1").build());
        commentRepository.save(Comment.builder().bookId(book1.getId()).text("Comment_1_2").build());
        commentRepository.save(Comment.builder().bookId(book2.getId()).text("Comment_2_1").build());
        commentRepository.save(Comment.builder().bookId(book2.getId()).text("Comment_2_2").build());
        commentRepository.save(Comment.builder().bookId(book3.getId()).text("Comment_3_1").build());
    }
}
