package ru.otus.hw.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

@Log
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        try {
            printQuestions(questionDao.findAll());
        } catch (QuestionReadException e) {
            ioService.printLine("Error reading the questions");
            log.severe(e.getMessage());
        }
    }

    private void printQuestions(List<Question> questions) {
        for (Question question : questions) {
            ioService.printLine(question.text());
            List<Answer> answers = question.answers();
            for (int i = 0; i < answers.size(); i++) {
                ioService.printLine(i + 1 + ". " + answers.get(i).text());
            }
            ioService.printLine("");
        }
    }
}
