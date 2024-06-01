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

@Getter(AccessLevel.PACKAGE)
@Accessors(fluent = true)
public final class CloudTerminal implements LoggerHandler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final Terminal terminal;
    private final LineReader lineReader;

    @Getter(AccessLevel.PUBLIC)
    private final CommandService commandService = new CommandService();
    private final CloudTerminalThread terminalThread = new CloudTerminalThread(this);

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

    public void start() {
        this.terminalThread.start();
    }

    public void clear() {
        this.terminal.puts(InfoCmp.Capability.clear_screen);
        this.terminal.flush();
        this.update();
    }

    public void update() {
        if (this.lineReader.isReading()) {
            this.lineReader.callWidget(LineReader.REDRAW_LINE);
            this.lineReader.callWidget(LineReader.REDISPLAY);
        }
    }

    @SneakyThrows
    public void close() {
        this.terminal.close();
    }

    public void spacer() {
        this.spacer(" ");
    }

    public void spacer(String message) {
        this.terminal.puts(InfoCmp.Capability.carriage_return);
        this.terminal.writer().println(includeColorCodes(message));
        this.terminal.flush();
        this.update();
    }

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

    String includeColorCodes(String context) {
        for (var color : CloudTerminalColor.colors) {
            context = context.replace("&" + (color.ordinal() + 1), color.fallbackAnsiCode());
        }
        return context;
    }
}


