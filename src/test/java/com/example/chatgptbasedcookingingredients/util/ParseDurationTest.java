package com.example.chatgptbasedcookingingredients.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParseDurationTest {

    @ParameterizedTest
    @CsvSource({
            "2h30m,     9000000",
            "45s,       45000",
            "1h,        3600000",
            "30m,       1800000",
            "1h2m3s,    3723000",
            "1m30s,     90000",
            "2h45s,     7245000",
            "0h5m,      300000"
    })
    void parsesValidDurationStrings(String input, long expectedMillis) {
        assertThat(ParseDuration.parseDuration(input)).isEqualTo(expectedMillis);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "   ",
            "abc"
    })
    void rejectsInvalidDurationStrings(String input) {
        assertThatThrownBy(() -> ParseDuration.parseDuration(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void invalidInput_exceptionHasClearNonBlankMessage() {
        assertThatThrownBy(() -> ParseDuration.parseDuration("abc"))
                .isInstanceOf(IllegalArgumentException.class)
                .extracting(Throwable::getMessage)
                .satisfies(message -> assertThat(message).isNotBlank());
    }

    @Test
    void overflowingDuration_throwsIllegalArgumentExceptionWithClearMessage() {
        assertThatThrownBy(() -> ParseDuration.parseDuration("9999999999999h"))
                .isInstanceOf(IllegalArgumentException.class)
                .extracting(Throwable::getMessage)
                .satisfies(message -> assertThat(message).isNotBlank());
    }
}