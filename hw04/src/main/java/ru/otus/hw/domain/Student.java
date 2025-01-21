package ru.otus.hw.domain;

import org.apache.commons.lang3.StringUtils;

public record Student(String firstName, String lastName) {
    public String getFullName() {
        return StringUtils.join(firstName, " ", lastName);
    }
}
