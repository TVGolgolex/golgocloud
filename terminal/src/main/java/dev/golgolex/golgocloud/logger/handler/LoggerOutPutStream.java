package dev.golgolex.golgocloud.logger.handler;

import dev.golgolex.golgocloud.logger.Logger;
import lombok.AllArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public final class LoggerOutPutStream extends ByteArrayOutputStream {

    private final Logger logger;
    private boolean errorStream;

    @Override
    public void flush() throws IOException {
        super.flush();

        var input = this.toString(StandardCharsets.UTF_8);
        super.reset();

        if (input != null && !input.isEmpty()) {
            input = input.replace("\n", "");
            if (!errorStream) {
                this.logger.info(input);
            } else {
                this.logger.error(input, null);
            }
        }
    }
}
