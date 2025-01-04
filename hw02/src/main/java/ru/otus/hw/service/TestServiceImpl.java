package ru.otus.hw.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.QuestionReadException;

@Service
@RequiredArgsConstructor
@Log
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        try {
            var questions = questionDao.findAll();
            var testResult = new TestResult(student);

            for (var question: questions) {
                test(question, testResult);
            }
            return testResult;
        } catch (QuestionReadException e) {
            ioService.printLine("Error reading the questions");
            log.severe(e.getMessage());
            return null;
        }
    }

    private void test(Question question, TestResult testResult) {
        ioService.printLine(question.text());
        var answers = question.answers();
        int rightAnswerNumber = printAnswers(answers);
        int answerNumber = ioService.readIntForRangeWithPrompt(1, answers.size(),
                "Please input the right answer number", "Answer number is out of range");
        var isAnswerValid = answerNumber == rightAnswerNumber;
        testResult.applyAnswer(question, isAnswerValid);
        ioService.printLine("");
    }

    /**
     * Вывод ответов пользователю
     *
     * @param answers перечень ответов
     * @return номер правильного ответа
     */
    private int printAnswers(List<Answer> answers) {
        int rightAnswerNumber = 0;
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            int number = i + 1;
            ioService.printLine(number + ". " + answer.text());
            if (answer.isCorrect()) {
                rightAnswerNumber = number;
            }
        }
        return rightAnswerNumber;
    }
}
