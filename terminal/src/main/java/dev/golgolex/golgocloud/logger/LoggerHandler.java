package dev.golgolex.golgocloud.logger;

public interface LoggerHandler {

    void print(LogLevel level, String message, Throwable throwable, Object... objects);

    void close();

}