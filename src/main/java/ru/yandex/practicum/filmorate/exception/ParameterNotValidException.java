package ru.yandex.practicum.filmorate.exception;

public class ParameterNotValidException extends IllegalArgumentException {
    private final String parameter;
    private final String reason;

    public ParameterNotValidException(String parameter, String reason) {
        super("Некорректное значение параметра " + parameter + ": " + reason);
        this.parameter = parameter;
        this.reason = reason;
    }

    public String getParameter() {
        return parameter;
    }

    public String getReason() {
        return reason;
    }
}