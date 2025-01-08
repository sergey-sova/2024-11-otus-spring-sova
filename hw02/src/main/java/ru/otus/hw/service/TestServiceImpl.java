package ru.otus.hw.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            boolean isAnswerValid = askQuestion(question);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private boolean askQuestion(Question question) {
        String questionText = convertToString(question);
        ioService.printLine(questionText);

        var answers = question.answers();
        int answerNumber = ioService.readIntForRangeWithPrompt(1, answers.size(),
                "Please input the right answer number", "Answer number is out of range");
        int rightAnswerNumber = getRightAnswerNumber(answers);
        return answerNumber == rightAnswerNumber;
    }

    private String convertToString(Question question) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append(question.text());
        sb.append("\n");

        var answers = question.answers();
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            sb.append(i + 1);
            sb.append(". ");
            sb.append(answer.text());
            sb.append("\n");
        }

        return sb.toString();
    }

    private int getRightAnswerNumber(List<Answer> answers) {
        int rightAnswerNumber = 0;
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            if (answer.isCorrect()) {
                rightAnswerNumber = i + 1;
            }
        }
        return rightAnswerNumber;
    }
}
