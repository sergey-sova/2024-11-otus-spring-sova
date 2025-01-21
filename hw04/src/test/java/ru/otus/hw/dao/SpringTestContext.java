package ru.otus.hw.dao;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import ru.otus.hw.config.TestFileNameProvider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootConfiguration
public class SpringTestContext {

    @Bean
    TestFileNameProvider testFileNameProvider() {
        TestFileNameProvider fileNameProvider = mock(TestFileNameProvider.class);
        when(fileNameProvider.getTestFileName()).thenReturn("questions.csv");
        return fileNameProvider;
    }
}
