package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TestServiceImplTest {

    private IOService ioService;

    private QuestionDao questionDao;

    private TestService testService;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
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