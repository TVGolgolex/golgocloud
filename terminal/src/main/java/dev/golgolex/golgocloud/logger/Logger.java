package dev.golgolex.golgocloud.logger;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
public final class Logger {

    private final LoggerFactory loggerFactory;
    @Setter
    @Accessors(fluent = true)
    private boolean debugMode;

    public void info(String message, Object... objects) {
        this.log(LogLevel.INFO, message, null, objects);
    }

    public void error(String message, Throwable throwable, Object... objects) {
        this.log(LogLevel.ERROR, message, throwable, objects);
    }

    public void success(String message, Object... objects) {
        this.log(LogLevel.SUCCESS, message, null, objects);
    }

    public void warn(String message, Object... objects) {
        this.log(LogLevel.WARN, message, null, objects);
    }

    public void debug(String message, Object... objects) {
        if (!debugMode) return;
        this.log(LogLevel.DEBUG, message, null, objects);
    }

    private void log(LogLevel level, String message, Throwable throwable, Object... objects) {
        this.loggerFactory.print(level, message, throwable, objects);
    }
}
