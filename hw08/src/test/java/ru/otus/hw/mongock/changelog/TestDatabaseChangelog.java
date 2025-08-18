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
public class TestDatabaseChangelog {

    public static Author AUTHOR1 = Author.builder().id("1").fullName("Author_1").build();
    public static Author AUTHOR2 = Author.builder().id("2").fullName("Author_2").build();
    public static Author AUTHOR3 = Author.builder().id("3").fullName("Author_3").build();

    public static Genre GENRE1 = Genre.builder().id("1").name("Genre_1").build();
    public static Genre GENRE2 = Genre.builder().id("2").name("Genre_2").build();
    public static Genre GENRE3 = Genre.builder().id("3").name("Genre_3").build();
    public static Genre GENRE4 = Genre.builder().id("4").name("Genre_4").build();
    public static Genre GENRE5 = Genre.builder().id("5").name("Genre_5").build();
    public static Genre GENRE6 = Genre.builder().id("6").name("Genre_6").build();

    public static Book BOOK1 = Book.builder().id("1").title("Book_1").author(AUTHOR1).genres(List.of(GENRE1, GENRE2))
            .build();

    public static Book BOOK2 = Book.builder().id("2").title("Book_2").author(AUTHOR2).genres(List.of(GENRE3, GENRE4))
            .build();

    public static Book BOOK3 = Book.builder().id("3").title("Book_3").author(AUTHOR3).genres(List.of(GENRE5, GENRE6))
            .build();

    public static Comment COMMENT11 = Comment.builder().id("1").bookId(BOOK1.getId()).text("Comment_1_1").build();
    public static Comment COMMENT12 = Comment.builder().id("2").bookId(BOOK1.getId()).text("Comment_1_2").build();
    public static Comment COMMENT21 = Comment.builder().id("3").bookId(BOOK2.getId()).text("Comment_2_1").build();
    public static Comment COMMENT22 = Comment.builder().id("4").bookId(BOOK2.getId()).text("Comment_2_2").build();
    public static Comment COMMENT31 = Comment.builder().id("5").bookId(BOOK3.getId()).text("Comment_3_1").build();

    @ChangeSet(order = "001", id = "dropDb", author = "s.sova", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertData", author = "s.sova")
    public void insertBooks(AuthorRepository authorRepository, GenreRepository genreRepository,
                            BookRepository books, CommentRepository commentRepository) {
        authorRepository.save(AUTHOR1);
        authorRepository.save(AUTHOR2);
        authorRepository.save(AUTHOR3);
        genreRepository.save(GENRE1);
        genreRepository.save(GENRE2);
        genreRepository.save(GENRE3);
        genreRepository.save(GENRE4);
        genreRepository.save(GENRE5);
        genreRepository.save(GENRE6);
        books.save(BOOK1);
        books.save(BOOK2);
        books.save(BOOK3);
        commentRepository.save(COMMENT11);
        commentRepository.save(COMMENT12);
        commentRepository.save(COMMENT21);
        commentRepository.save(COMMENT22);
        commentRepository.save(COMMENT31);
    }
}