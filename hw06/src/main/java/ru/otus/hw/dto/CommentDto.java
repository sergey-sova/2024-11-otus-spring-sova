package ru.otus.hw.dto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import ru.otus.hw.models.Comment;

public record CommentDto (long id, long bookId, String text) {

    public static CommentDto from(Comment comment) {
        return new CommentDto(comment.getId(), comment.getBook().getId(), comment.getText());
    }

    public static Optional<CommentDto> from(Optional<Comment> comment) {
        return comment.map(CommentDto::from);
    }

    public static List<CommentDto> from(Collection<Comment> comments) {
        return comments.stream().map(CommentDto::from).toList();
    }
}
