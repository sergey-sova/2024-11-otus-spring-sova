package ru.otus.hw.services;

import java.util.List;
import java.util.Optional;
import ru.otus.hw.dto.CommentDto;

public interface CommentService {
    Optional<CommentDto> findById(String id);

    List<CommentDto> findByBookId(String bookId);

    CommentDto add(String bookId, String text);

    CommentDto update(String id, String text);

    void deleteById(String id);
}
