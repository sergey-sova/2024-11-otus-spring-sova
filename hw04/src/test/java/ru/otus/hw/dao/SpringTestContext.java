package ru.otus.hw.dao;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@Import(CsvQuestionDao.class)
public class SpringTestContext {

}
