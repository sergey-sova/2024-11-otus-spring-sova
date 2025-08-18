package ru.otus.hw.services;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Override
    public Optional<CommentDto> findById(String id) {
        return CommentDto.from(commentRepository.findById(id));
    }

    @Override
    public List<CommentDto> findByBookId(String bookId) {
        return CommentDto.from(commentRepository.findByBookId(bookId));
    }

    @Override
    public CommentDto add(String bookId, String text) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id %s not found".formatted(bookId));
        }
        Comment comment = Comment.builder().bookId(bookId).text(text).build();
        return CommentDto.from(commentRepository.save(comment));
    }

    @Override
    public CommentDto update(String id, String text) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new EntityNotFoundException("Comment with id %s not found".formatted(id));
        }
        Comment commentEntity = comment.get();
        commentEntity.setText(text);
        return CommentDto.from(commentRepository.save(commentEntity));
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }
}
