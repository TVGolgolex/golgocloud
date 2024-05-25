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

import dev.golgolex.quala.utils.color.ConsoleColor;
import jline.console.ConsoleReader;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * A custom Formatter for formatting log records with ANSI colors and timestamps.
 */
public class LogFormatting extends Formatter {

    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss"); // Date format for timestamps.

    /**
     * Calculates the ConsoleColor based on the log record's level.
     *
     * @param record The log record.
     * @return The ConsoleColor corresponding to the log record's level.
     */
    private static ConsoleColor calculateColor(LogRecord record) {
        var ansiColor = ConsoleColor.AQUA;
        if (record.getLevel().equals(Level.SEVERE)) {
            ansiColor = ConsoleColor.RED;
        }
        if (record.getLevel().equals(Level.WARNING)) {
            ansiColor = ConsoleColor.ORANGE;
        }
        return ansiColor;
    }

    /**
     * Calculates the ConsoleColor corresponding to the log record's level.
     *
     * @param record The log record.
     * @return The ConsoleColor corresponding to the log record's level.
     */
    private static ConsoleColor calculateColorMsg(LogRecord record) {
        if (record.getLevel().equals(Level.SEVERE)) {
            return ConsoleColor.RED;
        }
        if (record.getLevel().equals(Level.WARNING)) {
            return ConsoleColor.ORANGE;
        }
        return null;
    }

    /**
     * Formats the given log record into a string with ANSI colors and timestamps.
     * Overrides the format method of the Formatter class.
     *
     * @param record The log record to be formatted.
     * @return The formatted string representation of the log record.
     */
    @Override
    public String format(LogRecord record) {
        var builder = new StringBuilder();
        // If there is a throwable associated with the log record, append its stack trace to the builder.
        if (record.getThrown() != null) {
            var writer = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(writer));
            builder.append(writer).append('\n');
        }
        var msg = formatMessage(record);

        if (record.getLevel().equals(Level.ALL)) {
            return ConsoleColor.DEFAULT.ansiCode()
                    + ConsoleReader.RESET_LINE
                    + (msg.endsWith("\n") ? msg : msg + "\n")
                    + builder;
        }
        return ConsoleColor.DEFAULT.ansiCode()
                + ConsoleReader.RESET_LINE
                + dateFormat.format(record.getMillis())
                + " "
                + ConsoleColor.DARK_GRAY.ansiCode() + "|"
                + " "
                + calculateColor(record).ansiCode()
                + record.getLevel().getName()
                + ": "
                + ConsoleColor.DEFAULT.ansiCode()
                + (msg.endsWith("\n") ? msg : msg + "\n")
                + builder
                + ConsoleColor.DEFAULT.ansiCode();

        // Build the formatted log record string with ANSI colors and timestamp.
/*        return ConsoleColor.DEFAULT.ansiCode()
                + ConsoleReader.RESET_LINE +
                "[" + dateFormat.format(record.getMillis()) + "] "
                + calculateColor(record).ansiCode()
                + record.getLevel().getName()
                + ConsoleColor.DEFAULT.ansiCode()
                + ": "
                + formatMessage(record)
                + '\n'
                + builder
                + ConsoleColor.DEFAULT.ansiCode();*/
    }
}
