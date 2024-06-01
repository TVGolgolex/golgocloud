package dev.golgolex.golgocloud.terminal;

import dev.golgolex.quala.utils.color.ConsoleColor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.fusesource.jansi.Ansi;

@Getter
public enum CloudTerminalColor {

    DEFAULT(209, 209, 209, ConsoleColor.DEFAULT.ansiCode()),
    DARK_GRAY(69, 69, 69, ConsoleColor.DARK_GRAY.ansiCode()),
    WHITE(255, 255, 255, ConsoleColor.WHITE.ansiCode()),
    INFO(125, 246, 255, ConsoleColor.AQUA.ansiCode()),
    WARNING(232, 164, 77, ConsoleColor.ORANGE.ansiCode()),
    ERROR( 247, 74, 74, ConsoleColor.RED.ansiCode()),
    PROMPT(130, 234, 255, ConsoleColor.AQUA.ansiCode()),
    SUCCESS(157, 191, 250, ConsoleColor.LIGHT_GREEN.ansiCode());

    public static final CloudTerminalColor[] colors = values();

    @Accessors(fluent = true)
    private final String ansiCode;
    @Accessors(fluent = true)
    private final String fallbackAnsiCode;

    CloudTerminalColor(int red, int green, int blue, String fallbackAnsiCode) {
        this.fallbackAnsiCode = fallbackAnsiCode;
        this.ansiCode = Ansi.ansi().a(Ansi.Attribute.RESET).fgRgb(red, green, blue).toString();
    }
}
