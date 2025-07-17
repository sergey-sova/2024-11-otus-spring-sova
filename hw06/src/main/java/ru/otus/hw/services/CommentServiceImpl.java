package ru.otus.hw.services;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public Optional<CommentDto> findById(long id) {
        return CommentDto.from(commentRepository.findById(id));
    }

    @Override
    public List<CommentDto> findByBookId(long bookId) {
        return CommentDto.from(commentRepository.findByBookId(bookId));
    }

    @Transactional
    @Override
    public CommentDto add(long bookId, String text) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(bookId));
        }
        Comment comment = new Comment(0, book.get(), text);
        return CommentDto.from(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto update(long id, String text) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new EntityNotFoundException("Comment with id %d not found".formatted(id));
        }
        Comment commentEntity = comment.get();
        commentEntity.setText(text);
        return CommentDto.from(commentRepository.save(commentEntity));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
