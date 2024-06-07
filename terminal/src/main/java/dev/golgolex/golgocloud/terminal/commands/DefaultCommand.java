package dev.golgolex.golgocloud.terminal.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The DefaultCommand annotation is used to mark a method as the default command for a command handler.
 * The method with this annotation will be invoked when no sub-command is specified.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DefaultCommand {
}
