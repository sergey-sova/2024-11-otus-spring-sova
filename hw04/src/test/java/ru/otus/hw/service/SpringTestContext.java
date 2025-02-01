package ru.otus.hw.service;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@Import(TestServiceImpl.class)
public class SpringTestContext {
}
