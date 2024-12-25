package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.QuestionReadException;

@Service
@Log
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    @Override
    public void run() {
        var student = studentService.determineCurrentStudent();
        try {
            var testResult = testService.executeTestFor(student);
            resultService.showResult(testResult);
        } catch (QuestionReadException e) {
            log.severe(e.getMessage());
        }
    }
}
