package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@SpringBootTest
class TestServiceImplTest {

    @MockBean
    private LocalizedIOService ioService;

    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private TestService testService;

    @Test
    void executeTest() {
        Student student = new Student("Aygul", "Yusupova");
        testService.executeTestFor(student);
        verify(ioService).printLine(anyString());
        verify(questionDao).findAll();
    }
}