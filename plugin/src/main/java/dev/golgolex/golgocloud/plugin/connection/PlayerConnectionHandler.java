package dev.golgolex.golgocloud.plugin.connection;

import org.jetbrains.annotations.NotNull;

public interface PlayerConnectionHandler<E, E2, T> {

    void login(@NotNull E event, @NotNull T player, Object... sub);

    void logout(@NotNull E2 event, @NotNull T player, Object... sub);

}
