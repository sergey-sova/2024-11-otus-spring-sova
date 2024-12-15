package ru.otus.hw.dao;

import ru.otus.hw.domain.Question;

import java.util.List;
import ru.otus.hw.exceptions.QuestionReadException;

public interface QuestionDao {
    List<Question> findAll() throws QuestionReadException;
}
