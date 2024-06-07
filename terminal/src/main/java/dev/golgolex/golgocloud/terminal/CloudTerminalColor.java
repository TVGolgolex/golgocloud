package dev.golgolex.golgocloud.terminal;

import dev.golgolex.quala.utils.color.ConsoleColor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.fusesource.jansi.Ansi;

/**
 * Enum representing colors for a cloud terminal.
 */
@Accessors(fluent = true)
@Getter
public enum CloudTerminalColor {

    DEFAULT(209, 209, 209, ConsoleColor.DEFAULT.ansiCode()),
    DARK_GRAY(69, 69, 69, ConsoleColor.DARK_GRAY.ansiCode()),
    WHITE(255, 255, 255, ConsoleColor.WHITE.ansiCode()),
    INFO(125, 246, 255, ConsoleColor.AQUA.ansiCode()),
    WARNING(232, 164, 77, ConsoleColor.YELLOW.ansiCode()),
    ERROR( 247, 74, 74, ConsoleColor.RED.ansiCode()),
    PROMPT(130, 234, 255, ConsoleColor.AQUA.ansiCode()),
    SUCCESS(157, 250, 178, ConsoleColor.LIGHT_GREEN.ansiCode());

    private final String ansiCode;
    private final String fallbackAnsiCode;

    /**
     * Represents a color for a cloud terminal.
     * <p>
     * This class provides a way to define a custom color for a cloud terminal.
     */
    CloudTerminalColor(int red, int green, int blue, String fallbackAnsiCode) {
        this.fallbackAnsiCode = fallbackAnsiCode;
        this.ansiCode = Ansi.ansi().a(Ansi.Attribute.RESET).fgRgb(red, green, blue).toString();
    }

    /**
     * Represents an array of colors for a cloud terminal.
     * <p>
     * The {@code colors} variable is an array containing all the color values defined in the {@link CloudTerminalColor} enum.
     * It provides easy access to the available colors for terminal output.
     * </p>
     * <p>
     * This array is defined as {@code public static final}, meaning that it is a constant variable and can be accessed from anywhere.
     * </p>
     *
     * @see CloudTerminalColor
     */
    public static final CloudTerminalColor[] colors = values();
}
