package dev.golgolex.golgocloud.terminal;

import dev.golgolex.golgocloud.common.CloudShutdownExecutor;
import dev.golgolex.golgocloud.logger.LogLevel;
import org.fusesource.jansi.Ansi;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;

import java.util.Arrays;


public final class CloudTerminalThread extends Thread {
    private final String prompt;
    private final CloudTerminal terminal;

    public CloudTerminalThread(CloudTerminal terminal) {
        this.terminal = terminal;
        setName("console-reading-thread");
        this.prompt = this.terminal.includeColorCodes("&3CloudSystem&2: &1");
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
                            System.out.println(Arrays.toString(line));
                            terminal.commandService().call(line);
                        }
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

    private void resetConsoleInput() {
        this.terminal.print(LogLevel.OFF, Ansi.ansi().reset().cursorUp(1).eraseLine().toString(), null);
    }
}
