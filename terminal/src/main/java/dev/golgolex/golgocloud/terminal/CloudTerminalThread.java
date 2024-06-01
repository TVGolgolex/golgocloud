package dev.golgolex.golgocloud.terminal;

import dev.golgolex.golgocloud.logger.LogLevel;
import org.fusesource.jansi.Ansi;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;


public final class CloudTerminalThread extends Thread {
    private final String prompt;
    private final CloudTerminal terminal;

    public CloudTerminalThread(CloudTerminal terminal) {
        this.terminal = terminal;
        setName("console-reading-thread");
        this.prompt = "&3cloud &2» &1";
        setContextClassLoader(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                try {
                    try {
                        final var rawLine = terminal.lineReader().readLine(prompt);
                        final var line = rawLine.split(" ");
                        resetConsoleInput();

                        if (line.length > 0) {
                            terminal.commandService().call(line);
                        }
                    } catch (EndOfFileException ignore) {
                        resetConsoleInput();
                    }
                } catch (UserInterruptException exception) {
                    resetConsoleInput();
                }
            } catch (Exception e) {
                resetConsoleInput();
                e.printStackTrace();
            }
        }
    }

    private void resetConsoleInput() {
        this.terminal.print(LogLevel.OFF, Ansi.ansi().reset().cursorUp(1).eraseLine().toString(), null);
    }
}
