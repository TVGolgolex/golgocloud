package dev.golgolex.golgocloud.terminal;

import dev.golgolex.golgocloud.logger.LogLevel;
import org.fusesource.jansi.Ansi;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;

import java.util.Arrays;

/**
 * A thread class that handles the reading of commands from the cloud terminal.
 */
public final class CloudTerminalThread extends Thread {

    private final String prompt;
    private final CloudTerminal terminal;

    /**
     * A thread class that handles the reading of commands from the cloud terminal.
     */
    public CloudTerminalThread(CloudTerminal terminal) {
        this.terminal = terminal;
        this.prompt = this.terminal.includeColorCodes("&3cloud &2» &1");
        setName("console-reading-thread");
        setContextClassLoader(Thread.currentThread().getContextClassLoader());
    }

    /**
     * The run method handles the reading of commands from the console.
     */
    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                try {
                    try {
                        final var rawLine = terminal.lineReader().readLine(prompt);
                        final var line = rawLine.split(" ");
                        if (line.length > 0) {
                            terminal.commandService().call(line);
                        }
//                        resetConsoleInput();
                    } catch (EndOfFileException ignore) {
                        resetConsoleInput();
                    }
                } catch (UserInterruptException exception) {
                    resetConsoleInput();
                    System.exit(0);
                }
            } catch (Exception e) {
                resetConsoleInput();
                e.printStackTrace();
            }
        }
    }

    /**
     * Resets the console input by clearing the previous input line.
     */
    private void resetConsoleInput() {
        this.terminal.print(LogLevel.OFF, Ansi.ansi().reset().cursorUp(1).eraseLine().toString(), null);
    }
}
