package dev.golgolex.golgocloud.plugin.connection;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class ServerToServerConnectionHandler implements PlayerConnectionHandler<PlayerLoginEvent, PlayerQuitEvent, Player> {
    @Override
    public void login(@NotNull PlayerLoginEvent event, @NotNull Player player, Object... sub) {
        if (player.isTransferred()) {

            var cloudPlayer = CloudAPI.instance().cloudPlayerProvider().cloudPlayer(player.getUniqueId());

            if (cloudPlayer == null || cloudPlayer.onlineCredentials() == null) {

                return;
            }

            return;
        }
    }

    @Override
    public void logout(@NotNull PlayerQuitEvent event, @NotNull Player player, Object... sub) {

    }
}
