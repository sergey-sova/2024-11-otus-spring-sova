package ru.otus.hw.dto;

import java.util.Collection;
import java.util.List;
import ru.otus.hw.models.Author;

public record AuthorDto (long id, String fullName) {

    public static AuthorDto from(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }

    public static List<AuthorDto> from(Collection<Author> authors) {
        return authors.stream().map(AuthorDto::from).toList();
    }
}