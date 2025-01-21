package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.domain.Student;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.TestService;


@ShellComponent
@RequiredArgsConstructor
@Log4j2
public class AppCommands {

    private final TestService testService;

    private final ResultService resultService;

    private final LocalizedIOService ioService;

    @ShellMethod(value = "Старт теста")
    public void test(@ShellOption(help = "Имя", defaultValue = "") String name,
            @ShellOption(help = "Фамилия", defaultValue = "") String surname) {
        var student = new Student(name, surname);
        try {
            var testResult = testService.executeTestFor(student);
            resultService.showResult(testResult);
        } catch (QuestionReadException e) {
            ioService.printLineLocalized("TestRunnerServiceImpl.error.reading.questions");
            log.error(e.getMessage());
        }
    }
}
