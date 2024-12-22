package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;

import java.util.List;
import ru.otus.hw.exceptions.QuestionReadException;

import static lombok.Lombok.checkNotNull;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() throws QuestionReadException {
        ClassLoader classLoader = getClass().getClassLoader();
        String fileName = fileNameProvider.getTestFileName();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            checkNotNull(inputStream, String.format("File %s not found", fileName));
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            return new CsvToBeanBuilder<QuestionDto>(inputStreamReader)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .withType(QuestionDto.class)
                    .build()
                    .parse()
                    .stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();
        } catch (Exception e) {
            throw new QuestionReadException(e.getMessage(), e);
        }
    }
}
