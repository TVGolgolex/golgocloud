package dev.golgolex.golgocloud.plugin.connection;

import org.jetbrains.annotations.NotNull;

public interface PlayerConnectionHandler<E, E2, E3, T> {

    void login(@NotNull E event, @NotNull T player, Object... sub);

    void finalJoin(@NotNull E3 event, @NotNull T player, Object... sub);

    void logout(@NotNull E2 event, @NotNull T player, Object... sub);

}
