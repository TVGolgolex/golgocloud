package dev.golgolex.golgocloud.plugin.connection;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ServerToServerConnection implements PlayerConnectionHandler<Player> {
    @Override
    public void login(@NotNull Player player) {
        if (player.isTransferred()) {

            return;
        }
    }

    @Override
    public void logout(@NotNull Player player) {

    }
}
