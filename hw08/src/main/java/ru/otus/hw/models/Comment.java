package ru.otus.hw.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "documents")
@Data
@Builder
public class Comment {
    @Id
    private String id;

    private String bookId;

    private String text;
}
