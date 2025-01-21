package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.config.TestFileNameProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CsvQuestionDaoTest {

    @Autowired
    private TestFileNameProvider fileNameProvider;

    @Test
    void findAllTest() {
        QuestionDao questionDao = new CsvQuestionDao(fileNameProvider);
        assertEquals(5, questionDao.findAll().size());
    }

}