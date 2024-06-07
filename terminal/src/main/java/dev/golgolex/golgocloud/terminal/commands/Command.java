package dev.golgolex.golgocloud.terminal.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Command annotation used to associate a command with a method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {

    /**
     * Retrieves the command associated with the method.
     *
     * @return the command
     */
    String command();

    /**
     * Returns the description of the method.
     *
     * @return the description of the method as a String
     */
    String description() default "";

    /**
     * Returns the aliases defined for a method.
     *
     * @return an array of strings representing the method aliases
     */
    String[] aliases() default {};

}
