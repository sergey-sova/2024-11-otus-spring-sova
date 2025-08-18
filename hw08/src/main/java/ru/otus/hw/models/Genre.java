package ru.otus.hw.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "genres")
@Data
@Builder
public class Genre {
    @Id
    private String id;

    private String name;
}
