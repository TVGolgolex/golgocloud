/*
 * Copyright (c) Tarek Hosni El Alaoui 2017
 */

package dev.golgolex.golgocloud.logger;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Asynchronous print stream that takes print statements without blocking.
 */
@Getter
public class AsyncPrintStream extends PrintStream {

    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private final Thread worker = new WorkerThread(queue);

    /**
     * Constructs a new asynchronous print stream.
     *
     * @param out the output stream to write to
     * @throws UnsupportedEncodingException when UTF-8 is mysteriously unavailable
     */
    public AsyncPrintStream(OutputStream out) throws UnsupportedEncodingException {
        super(out, true, StandardCharsets.UTF_8.name());
    }

    /**
     * Prints a given boolean value.
     *
     * @param x the boolean value to be printed
     */
    @Override
    public void print(boolean x) {
        if (Thread.currentThread() != worker) {
            var ignored = queue.offer(() -> print0(x));
        } else {
            super.print(x);
        }
    }

    /**
     * Prints the specified character.
     *
     * @param x the character to be printed
     */
    @Override
    public void print(char x) {
        if (Thread.currentThread() != worker) {
            var ignored = queue.offer(() -> print0(x));
        } else {
            super.print(x);
        }
    }

    /**
     * Prints the specified integer.
     *
     * @param x the integer to be printed
     */
    @Override
    public void print(int x) {
        if (Thread.currentThread() != worker) {
            var ignored = queue.offer(() -> print0(x));
        } else {
            super.print(x);
        }
    }

    /**
     * Calls the {@code print} method of the superclass with the specified integer value.
     *
     * @param x the integer value to be printed
     */
    private void print0(int x) {
        super.print(x);
    }

    /**
     * Prints the specified value if the calling thread is not the worker thread.
     * If the calling thread is the worker thread, the value is printed using the superclass's print method.
     *
     * @param x the value to be printed
     */
    @Override
    public void print(long x) {
        if (Thread.currentThread() != worker) {
            var ignored = queue.offer(() -> print0(x));
        } else {
            super.print(x);
        }
    }

    /**
     * Prints the given long value.
     *
     * @param x the long value to be printed
     */
    private void print0(long x) {
        super.print(x);
    }

    /**
     * Prints a float value.
     *
     * @param x the float value to be printed
     */
    @Override
    public void print(float x) {
        if (Thread.currentThread() != worker) {
            var ignored = queue.offer(() -> print0(x));
        } else {
            super.print(x);
        }
    }

    /**
     * Prints the given double value.
     * If called from a different thread than the worker thread, the value is added to a queue and processed by the worker thread.
     * If called from the worker thread, the value is directly printed using the superclass implementation.
     *
     * @param x the double value to be printed
     */
    @Override
    public void print(double x) {
        if (Thread.currentThread() != worker) {
            var ignored = queue.offer(() -> print0(x));
        } else {
            super.print(x);
        }
    }

    /**
     * Prints the given double value to the output stream.
     *
     * @param x the double value to be printed
     */
    private void print0(double x) {
        super.print(x);
    }

    /**
     * Prints an array of characters.
     *
     * @param x the array of characters to be printed
     */
    @Override
    public void print(char @NotNull [] x) {
        if (Thread.currentThread() != worker) {
            var ignored = queue.offer(() -> print0(x));
        } else {
            super.print(x);
        }
    }

    /**
     * Prints the given string.
     *
     * @param x the string to be printed
     */
    @Override
    public void print(String x) {
        if (Thread.currentThread() != worker) {
            var ignored = queue.offer(() -> print0(x));
        } else {
            super.print(x);
        }
    }

    /**
     * This method prints the given string.
     *
     * @param x the string to be printed
     */
    private void print0(String x) {
        super.print(x);
    }

    /**
     * Prints the specified object to the standard output. If the current thread is not the worker thread,
     * the object is enqueued and printed by the worker thread. Otherwise, it is printed directly by
     * calling the super method.
     *
     * @param x the object to be printed
     */
    @Override
    public void print(Object x) {
        if (Thread.currentThread() != worker) {
            var ignored = queue.offer(() -> print0(x));
        } else {
            super.print(x);
        }
    }

    /**
     * Prints a new line to the standard output stream.
     * This method will add a newline character to the output and flush the stream.
     */
    @Override
    public void println() {
        var ignored = queue.offer(this::println0);
    }

    /**
     * Prints a line of text.
     * <p>
     * This method overrides the `println` method of the superclass and calls it.
     * It is used to print a line of text to the standard output stream.
     *
     * @see PrintStream#println()
     */
    private void println0() {
        super.println();
    }

    /**
     * Prints a boolean value and terminates the line.
     *
     * @param x the boolean value to be printed
     */
    @Override
    public void println(boolean x) {
        var ignored = queue.offer(() -> println0(x));
    }

    /**
     * Prints a character followed by a new line.
     *
     * @param x the character to be printed
     */
    @Override
    public void println(char x) {
        var ignored = queue.offer(() -> println0(x));
    }

    /**
     * Prints an integer value and adds it to the print queue.
     *
     * @param x the integer value to be printed
     */
    @Override
    public void println(int x) {
        var ignored = queue.offer(() -> println0(x));
    }

    /**
     * Prints the specified integer to the standard output stream.
     *
     * @param x the integer value to be printed
     */
    private void println0(int x) {
        super.println(x);
    }

    /**
     * Prints the specified long value.
     *
     * @param x the long value to be printed
     */
    @Override
    public void println(long x) {
        var ignored = queue.offer(() -> println0(x));
    }

    /**
     * Prints a long value and terminates the line.
     *
     * @param x the long to be printed
     */
    private void println0(long x) {
        super.println(x);
    }

    /**
     * Prints a floating-point number and moves to the next line.
     *
     * @param x the floating-point number to be printed
     */
    @Override
    public void println(float x) {
        var ignored = queue.offer(() -> println0(x));
    }

    /**
     * Prints the specified double value and a new line to the output.
     *
     * @param x the double value to be printed
     */
    @Override
    public void println(double x) {
        var ignored = queue.offer(() -> println0(x));
    }

    /**
     * Prints a double value and terminates the line.
     *
     * @param x the value to be printed
     */
    private void println0(double x) {
        super.println(x);
    }

    /**
     * Prints an array of characters, followed by a line break.
     *
     * @param x the array of characters to be printed
     */
    @Override
    public void println(char @NotNull [] x) {
        var ignored = queue.offer(() -> println0(x));
    }

    /**
     * Prints the specified string and appends a new line to the output.
     *
     * @param x the string to be printed
     */
    @Override
    public void println(String x) {
        var ignored = queue.offer(() -> println0(x));
    }

    /**
     * Prints a string to the standard output stream.
     *
     * @param x the string to be printed
     */
    private void println0(String x) {
        super.println(x);
    }

    /**
     * Prints the string representation of the specified object
     * to the standard output stream followed by a new line.
     *
     * @param x the object to be printed
     */
    @Override
    public void println(Object x) {
        var ignored = queue.offer(() -> println0(x));
    }

    /**
     * Prints the specified object followed by a new line.
     *
     * @param x the object to be printed
     */
    private void println0(Object x) {
        super.println(x);
    }

    /**
     * Prints the specified character array followed by a newline.
     *
     * @param x the character array to be printed
     */
    private void println0(char[] x) {
        super.println(x);
    }

    /**
     * Prints the specified float value to the standard output stream.
     *
     * @param x the float value to be printed
     */
    private void println0(float x) {
        super.println(x);
    }

    /**
     * Prints a character to the standard output stream.
     *
     * @param x the character to be printed
     */
    private void println0(char x) {
        super.println(x);
    }

    /**
     * Prints the specified boolean value to the console.
     *
     * @param x the boolean value to be printed
     */
    private void println0(boolean x) {
        super.println(x);
    }

    /**
     * Prints the specified object, without creating a new line, by calling the super class's print method.
     *
     * @param x the object to be printed
     */
    private void print0(Object x) {
        super.print(x);
    }

    /**
     * This method prints the characters from the given char array.
     *
     * @param x the char array containing the characters to print
     */
    private void print0(char[] x) {
        super.print(x);
    }

    /**
     * Prints the given float value.
     *
     * @param x the float value to be printed
     */
    private void print0(float x) {
        super.print(x);
    }

    /**
     * Prints a character to the standard output stream.
     *
     * @param x the character to be printed
     */
    private void print0(char x) {
        super.print(x);
    }

    /**
     * Prints the boolean value "x" by invoking the superclass's print method.
     *
     * @param x the boolean value to be printed
     */
    private void print0(boolean x) {
        super.print(x);
    }
}
