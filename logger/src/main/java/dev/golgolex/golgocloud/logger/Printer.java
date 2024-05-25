/*
 * Copyright (c) Tarek Hosni El Alaoui 2017
 */

package dev.golgolex.golgocloud.logger;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for classes that handle console messages of the cloud.
 */
public interface Printer {

    /**
     * Handle console messages
     *
     * @param input the string that should be handled
     */
    void handleConsole(@NotNull String input);

}
