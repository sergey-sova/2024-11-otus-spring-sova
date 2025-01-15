package hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestService;
import ru.otus.hw.service.TestServiceImpl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TestServiceImplTest {

    private LocalizedIOService ioService;

    private QuestionDao questionDao;

    private TestService testService;

    @BeforeEach
    void setUp() {
        ioService = mock(LocalizedIOService.class);
        questionDao = mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    void executeTest() {
        Student student = new Student("Aygul", "Yusupova");
        testService.executeTestFor(student);
        verify(ioService).printLine(anyString());
        verify(questionDao).findAll();
    }
}