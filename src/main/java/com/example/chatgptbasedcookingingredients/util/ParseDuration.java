package com.example.chatgptbasedcookingingredients.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ParseDuration {

    private static final Pattern DURATION_PATTERN = Pattern.compile("^(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s)?$");

    private ParseDuration() {
    }

    public static long parseDuration(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Duration must not be null or blank");
        }

        Matcher matcher = DURATION_PATTERN.matcher(input);
        if (!matcher.matches() || (matcher.group(1) == null && matcher.group(2) == null && matcher.group(3) == null)) {
            throw new IllegalArgumentException("Invalid duration format: " + input);
        }

        long hours = matcher.group(1) != null ? Long.parseLong(matcher.group(1)) : 0;
        long minutes = matcher.group(2) != null ? Long.parseLong(matcher.group(2)) : 0;
        long seconds = matcher.group(3) != null ? Long.parseLong(matcher.group(3)) : 0;

        try {
            long totalSeconds = Math.addExact(Math.addExact(Math.multiplyExact(hours, 3600L), Math.multiplyExact(minutes, 60L)), seconds);
            return Math.multiplyExact(totalSeconds, 1000L);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Duration is too large: " + input);
        }
    }
}