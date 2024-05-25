package dev.golgolex.golgocloud.logger;

/*
 * MIT License
 *
 * Copyright (c) 2024 ClayCloud contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import jline.console.ConsoleReader;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * A custom Handler that publishes log records to multiple ICloudLoggerHandlers and to the console.
 */
@AllArgsConstructor
public class PublishHandler extends Handler {
    private final List<Printer> logPrinters; // List of ICloudLoggerHandlers to handle log records.
    private final ConsoleReader consoleReader; // ConsoleReader for displaying log records on the console.
    private boolean closed; // Flag indicating whether the handler is closed.

    /**
     * Publishes a log record to the configured ICloudLoggerHandlers and to the console.
     * Overrides the publish method of the Handler class.
     */
    @Override
    public void publish(LogRecord record) {
        // If the handler is closed, do nothing.
        if (this.closed) {
            return;
        }
        // Publish the log record to each ICloudLoggerHandler in the list.
        for (var iCloudLoggerHandler : this.logPrinters) {
            iCloudLoggerHandler.handleConsole(this.getFormatter().formatMessage(record));
        }

        // If the log record is loggable, display it on the console.
        if (isLoggable(record)) {
            try {
                // Print the log record to the console, overwriting the previous line.
                this.consoleReader.print(ConsoleReader.RESET_LINE + getFormatter().format(record));
                // Draw the current line in the console.
                this.consoleReader.drawLine();
                // Flush the console output.
                this.consoleReader.flush();
            } catch (Throwable ignored) {
                // Ignore any exceptions that occur during printing to the console.
            }
        }
    }

    /**
     * Does nothing, as there is no need to flush any buffered data.
     * Overrides the flush method of the Handler class.
     */
    @Override
    public void flush() {
        // Do nothing.
    }

    /**
     * Closes the handler by setting the 'closed' flag to true.
     * Overrides the close method of the Handler class.
     *
     * @throws SecurityException If a security manager exists and denies access.
     */
    @Override
    public void close() throws SecurityException {
        // Mark the handler as closed.
        this.closed = true;
    }
}