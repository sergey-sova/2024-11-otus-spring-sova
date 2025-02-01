package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestFileNameProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class CsvQuestionDaoTest {

    @MockBean
    TestFileNameProvider fileNameProvider;

    @Autowired
    QuestionDao questionDao;

    @BeforeEach
    void setUp() {
        when(fileNameProvider.getTestFileName()).thenReturn("questions.csv");
    }

    @Test
    void findAllTest() {
        assertEquals(5, questionDao.findAll().size());
    }

}