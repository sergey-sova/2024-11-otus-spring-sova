package hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CsvQuestionDaoTest {

    private TestFileNameProvider fileNameProvider;

    @BeforeEach
    void setUp() {
        fileNameProvider = mock(TestFileNameProvider.class);
        when(fileNameProvider.getTestFileName()).thenReturn("questions.csv");
    }

    @Test
    void findAllTest() {
        QuestionDao questionDao = new CsvQuestionDao(fileNameProvider);
        assertEquals(5, questionDao.findAll().size());
    }

}