package dev.golgolex.golgocloud.plugin.connection;

import org.jetbrains.annotations.NotNull;

public interface PlayerConnectionHandler<T> {

    void login(@NotNull T player);

    void logout(@NotNull T player);

}
