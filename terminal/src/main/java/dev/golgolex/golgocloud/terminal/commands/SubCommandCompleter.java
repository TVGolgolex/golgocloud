package dev.golgolex.golgocloud.terminal.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A custom annotation used to define completion patterns for command sub-methods.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommandCompleter {

    /**
     * Retrieves the completion pattern for a method.
     *
     * @return an array of strings representing the completion pattern
     */
    String[] completionPattern();

}
