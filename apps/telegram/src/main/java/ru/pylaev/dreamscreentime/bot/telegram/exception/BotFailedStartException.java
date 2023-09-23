package ru.pylaev.dreamscreentime.bot.telegram.exception;

public class BotFailedStartException extends RuntimeException {
    public BotFailedStartException(Throwable cause) {
        super(cause);
    }
}
