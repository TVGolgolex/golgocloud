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

import lombok.AllArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A custom OutputStream that redirects its content to a Logger.
 */
@AllArgsConstructor
public class OutputStream extends ByteArrayOutputStream {
    private final String separator; // The separator used to split log entries.
    private final Level level; // The logging level to use for the log entries.
    private final Logger logger; // The Logger instance where log entries will be sent.

    /**
     * Flushes the content of the stream and logs it using the specified Logger.
     * Overrides the flush method of the ByteArrayOutputStream class.
     */
    @Override
    public void flush() {
        // Get the contents of the stream as a string.
        var contents = toString(StandardCharsets.UTF_8);
        // Reset the stream to clear its content.
        super.reset();
        // Check if the content is not empty and not just the separator.
        if (!contents.isEmpty() && !contents.equals(separator)) {
            // Log the content using the specified logging level and logger.
            this.logger.logp(this.level, "", "", contents);
        }
    }
}
