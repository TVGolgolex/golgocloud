package dev.golgolex.golgocloud.plugin.connection;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.golgocloud.common.user.OnlineCredentials;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerLoginPacket;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerTransferredPacket;
import dev.golgolex.golgocloud.plugin.paper.CloudPaperPlugin;
import dev.golgolex.quala.json.document.JsonDocument;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;

public class ServerToServerConnectionHandler implements PlayerConnectionHandler<PlayerLoginEvent, PlayerQuitEvent, Player> {
    @Override
    public void login(@NotNull PlayerLoginEvent event, @NotNull Player player, Object... sub) {
        if (player.isTransferred()) {
            var cloudPlayer = CloudAPI.instance().cloudPlayerProvider().cloudPlayer(player.getUniqueId());
            if (cloudPlayer == null || cloudPlayer.onlineCredentials() == null) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("§cA transfer is only possible via a verified server§8."));
                return;
            }

            CloudAPI.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudPlayerTransferredPacket(
                    cloudPlayer,
                    CloudAPI.instance().cloudServiceProvider().cloudService(CloudPaperPlugin.instance().thisServiceId()).orElseThrow()
            ));
            return;
        }

        CloudAPI.instance().cloudGroupProvider().cloudGroup(CloudPaperPlugin.instance().thisGroupName()).ifPresentOrElse(cloudGroup -> {
            if (!cloudGroup.joinByPort()) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("§cYou cannot connect to this server with the port§8. §cConnect to a verified server§8."));
                return;
            }

            var credentials = new OnlineCredentials(
                    "",
                    CloudPaperPlugin.instance().thisServiceId(),
                    "",
                    new ArrayList<>(),
                    null
            );

            var cloudPlayer = CloudAPI.instance().cloudPlayerProvider().cloudPlayer(player.getUniqueId());

            if (cloudPlayer == null) {
                cloudPlayer = new CloudPlayer(
                        player.getUniqueId(),
                        player.getName(),
                        new JsonDocument(),
                        new Timestamp(System.currentTimeMillis()),
                        new Timestamp(System.currentTimeMillis()),
                        new Timestamp(System.currentTimeMillis()),
                        0L,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        "",
                        "",
                        credentials,
                        false
                );
                CloudAPI.instance().cloudPlayerProvider().createCloudPlayer(cloudPlayer);
            }
            CloudAPI.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudPlayerLoginPacket(cloudPlayer));
        }, () -> event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("§cNo server group could be found§8. §cIncorrect loading could be the cause§8.")));
    }

    @Override
    public void logout(@NotNull PlayerQuitEvent event, @NotNull Player player, Object... sub) {

    }
}
