package dev.golgolex.golgocloud.terminal.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The SubCommand annotation is used to mark a method as a sub-command for a command handler.
 * It provides information about the arguments that need to be passed to the method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommand {

    /**
     *
     * @return the arguments passed to the method as an array of strings
     */
    String[] args();

}
