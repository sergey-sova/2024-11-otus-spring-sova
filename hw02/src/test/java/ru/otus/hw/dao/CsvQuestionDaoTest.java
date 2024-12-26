package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;

import static org.junit.jupiter.api.Assertions.*;

class CsvQuestionDaoTest {

    private TestFileNameProvider fileNameProvider;

    @BeforeEach
    void setUp() {
        fileNameProvider = new AppProperties(3, "questions.csv");
    }

    @Test
    void findAllTest() {
        QuestionDao questionDao = new CsvQuestionDao(fileNameProvider);
        assertEquals(5, questionDao.findAll().size());
    }

}