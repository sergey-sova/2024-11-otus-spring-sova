package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;

import java.util.List;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() throws QuestionReadException {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileNameProvider.getTestFileName())) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            return new CsvToBeanBuilder(inputStreamReader)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .withType(QuestionDto.class)
                    .build()
                    .parse()
                    .stream()
                    .map(dto -> ((QuestionDto) dto).toDomainObject())
                    .toList();
        } catch (IOException e) {
            throw new QuestionReadException(e.getMessage());
        }
    }
}
