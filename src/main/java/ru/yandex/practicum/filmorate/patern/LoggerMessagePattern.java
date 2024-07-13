package ru.yandex.practicum.filmorate.patern;

public class LoggerMessagePattern {
    public static final String DEBUG = "Action: {}, data: {}";
    public static final String WARN = "Action: {}, data: {}, message: {}, exception: {}";

    private LoggerMessagePattern() {
    }
}