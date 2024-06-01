package dev.golgolex.golgocloud.logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;

public final class LoggerFactory implements LoggerHandler {

    private final Set<LoggerHandler> handlers = new LinkedHashSet<>();

    public void registerLoggers(LoggerHandler... loggerHandler) {
        this.handlers.addAll(Arrays.stream(loggerHandler).toList());
    }

    @Override
    public void print(LogLevel level, String message, Throwable throwable, Object... objects) {
        handlers.forEach(it -> it.print(level, message, throwable, objects));
    }

    @Override
    public void close() {
        this.handlers.forEach(LoggerHandler::close);
    }
}
