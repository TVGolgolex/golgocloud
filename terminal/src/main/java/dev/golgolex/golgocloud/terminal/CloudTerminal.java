package dev.golgolex.golgocloud.terminal;

import dev.golgolex.golgocloud.logger.LogLevel;
import dev.golgolex.golgocloud.logger.LoggerHandler;
import dev.golgolex.golgocloud.terminal.commands.CommandCompleter;
import dev.golgolex.golgocloud.terminal.commands.CommandService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.fusesource.jansi.Ansi;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * CloudTerminal is a class that provides an interactive terminal interface for command execution.
 * It implements the LoggerHandler interface to handle logging functionality.
 */
@Getter(AccessLevel.PACKAGE)
@Accessors(fluent = true)
public final class CloudTerminal implements LoggerHandler {

    /**
     *
     */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final Terminal terminal;
    private final LineReader lineReader;

    @Getter(AccessLevel.PUBLIC)
    private final CommandService commandService = new CommandService();
    private final CloudTerminalThread terminalThread = new CloudTerminalThread(this);

    /**
     * CloudTerminal is a class that provides an interactive terminal interface for command execution.
     */
    @SneakyThrows
    public CloudTerminal() {
        this.terminal = TerminalBuilder.builder()
                .system(true)
                .encoding(StandardCharsets.UTF_8)
                .dumb(true)
                .build();
        this.lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new CommandCompleter(this.commandService))
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .option(LineReader.Option.AUTO_PARAM_SLASH, false)
                .variable(LineReader.BELL_STYLE, "none")
                .build();
        this.clear();
    }

    /**
     * Starts the terminal thread.
     */
    public void start() {
        this.terminalThread.start();
    }

    /**
     * Clears the terminal screen by sending a clear screen capability to the terminal and updating the terminal display.
     */
    public void clear() {
        this.terminal.puts(InfoCmp.Capability.clear_screen);
        this.terminal.flush();
        this.update();
    }

    /**
     * Update the line reader if it is currently reading.
     */
    public void update() {
        if (this.lineReader.isReading()) {
            this.lineReader.callWidget(LineReader.REDRAW_LINE);
            this.lineReader.callWidget(LineReader.REDISPLAY);
        }
    }

    /**
     * Closes the CloudTerminal by closing the associated terminal.
     */
    @SneakyThrows
    public void close() {
        this.terminal.close();
    }

    /**
     * Prints a message with a blank spacer line before and after the message.
     * */
    public void spacer() {
        this.spacer(" ");
    }

    /**
     * Prints a message with a line break to the terminal.
     *
     * @param message the message to print
     */
    public void spacer(String message) {
        this.terminal.puts(InfoCmp.Capability.carriage_return);
        this.terminal.writer().println(includeColorCodes(message));
        this.terminal.flush();
        this.update();
    }

    /**
     * Prints a log message to the terminal.
     *
     * @param level     the log level
     * @param message   the log message
     * @param throwable the throwable associated with the log message (optional)
     * @param objects   additional objects to include in the log message (optional)
     */
    @Override
    public void print(LogLevel level, String message, Throwable throwable, Object... objects) {
        terminal.puts(InfoCmp.Capability.carriage_return);
        if (level != LogLevel.OFF) {
            terminal.writer().println(includeColorCodes("&1" + dateFormat.format(Calendar.getInstance().getTime()) + " &2| " + level.colorCode() + level.name() + "&2: &1" + message)
                    + Ansi.ansi().a(Ansi.Attribute.RESET).toString());
        } else {
            terminal.writer().write(message);
        }
        terminal.flush();

        this.update();
    }

    /**
     * Replaces color codes in the given context with their corresponding ANSI escape sequences.
     *
     * @param context the text containing color codes to be replaced
     * @return the text with color codes replaced by ANSI escape sequences
     */
    public String includeColorCodes(String context) {
        var screenSession = System.getenv("STY");
        for (var color : CloudTerminalColor.colors) {
            context = context.replace("&" + (color.ordinal() + 1), screenSession != null ? color.fallbackAnsiCode() : color.ansiCode());
        }
        return context;
    }
}


