package ru.otus.hw.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authors")
@Data
@Builder
public class Author {
    @Id
    private String id;

    private String fullName;
}
