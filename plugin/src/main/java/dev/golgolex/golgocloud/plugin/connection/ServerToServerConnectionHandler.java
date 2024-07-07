package dev.golgolex.golgocloud.plugin.connection;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.common.permission.CloudPermissibleEntity;
import dev.golgolex.golgocloud.common.permission.CloudPermissibleGroup;
import dev.golgolex.golgocloud.common.serverbranding.ServerBrandStyle;
import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.golgocloud.common.user.OnlineCredentials;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerLoginPacket;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerLogoutPacket;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerTransferredPacket;
import dev.golgolex.golgocloud.plugin.paper.CloudPaperPlugin;
import dev.golgolex.golgocloud.plugin.paper.event.CloudPaperPlayerLoginEvent;
import dev.golgolex.golgocloud.plugin.paper.permission.PaperPermissionHelper;
import dev.golgolex.quala.common.json.JsonDocument;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;

public class ServerToServerConnectionHandler implements PlayerConnectionHandler<PlayerLoginEvent, PlayerQuitEvent, PlayerJoinEvent, Player> {
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
            if (cloudPlayer != null && cloudPlayer.onlineCredentials() != null) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("§cYou are already connected to the network.§8."));
                return;
            }

            if (cloudPlayer == null) {
                cloudPlayer = new CloudPlayer(
                        event.getPlayer().getUniqueId(),
                        event.getPlayer().getName(),
                        new JsonDocument(),
                        new Timestamp(System.currentTimeMillis()),
                        new Timestamp(System.currentTimeMillis()),
                        new Timestamp(System.currentTimeMillis()),
                        0L,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        CloudAPI.instance().translationAPI().defaultLanguages().getFirst().name(),
                        "",
                        credentials,
                        false
                );
                CloudAPI.instance().cloudPlayerProvider().createCloudPlayer(cloudPlayer);
            }

            var permissionPool = CloudAPI.instance().cloudPermissionService().permissionPool("cloud");
            if (permissionPool != null) {
                var entity = permissionPool.permissibleEntity(player.getUniqueId());
                if (entity == null) {
                    entity = new CloudPermissibleEntity(new JsonDocument(), new ArrayList<>(), player.getUniqueId(), new ArrayList<>());
                    entity.groupEntries().add(new CloudPermissibleEntity.GroupEntry(
                            permissionPool.permissibleGroups().stream().filter(CloudPermissibleGroup::fallbackOption).map(CloudPermissibleGroup::name).findAny().orElse(null),
                            System.currentTimeMillis(),
                            -1L,
                            1
                    ));
                    permissionPool.createPermissible(entity);
                    CloudAPI.instance().cloudPermissionService().updatePermissionPool(permissionPool);
                } else {
                    var finalEntity = entity;
                    entity.groupEntries()
                            .stream()
                            .filter(groupEntry -> groupEntry.untilTimestamp() < System.currentTimeMillis())
                            .findFirst()
                            .ifPresent(groupEntry -> {
                                finalEntity.groupEntries().removeIf(it -> it.name().equalsIgnoreCase(groupEntry.name()));

                                if (finalEntity.groupEntries().isEmpty()) {
                                    finalEntity.groupEntries().add(new CloudPermissibleEntity.GroupEntry(
                                            permissionPool.permissibleGroups().stream().filter(CloudPermissibleGroup::fallbackOption).map(CloudPermissibleGroup::name).findAny().orElse(null),
                                            System.currentTimeMillis(),
                                            -1L,
                                            1
                                    ));
                                }

                                permissionPool.updatePermissible(finalEntity);
                            });
                }
                PaperPermissionHelper.injectPlayer(player, permissionPool);
            }

            cloudPlayer.onlineCredentials(credentials);
            CloudAPI.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudPlayerLoginPacket(cloudPlayer));
        }, () -> event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("§cNo server group could be found§8. §cIncorrect loading could be the cause§8.")));
    }

    @Override
    public void finalJoin(@NotNull PlayerJoinEvent event, @NotNull Player player, Object... sub) {
        var cloudPlayer = CloudAPI.instance().cloudPlayerProvider().cloudPlayer(player.getUniqueId());

        if (cloudPlayer.onlineCredentials() == null) {
            System.out.println("[!] non onlineCredentials");
            return;
        }

        ServerBrandStyle style;
        if (event.getPlayer().getVirtualHost() == null || (CloudAPI.IP_PATTERN.matcher(event.getPlayer().getVirtualHost().getHostString()).matches())) {
            style = CloudAPI.instance().serverBrandingService().anyDefault();
        } else {
            var hostString = event.getPlayer().getVirtualHost().getHostString();
            var split = hostString.split("\\.");
            var domain = split[split.length - 2];
            var tld = split[split.length - 1];
            style = CloudAPI.instance()
                    .serverBrandingService()
                    .loadedBrands()
                    .stream()
                    .filter(serverBrandStyle -> serverBrandStyle.domain().equalsIgnoreCase(domain + "." + tld))
                    .findFirst()
                    .orElse(CloudAPI.instance().serverBrandingService().anyDefault());
        }

        cloudPlayer.branding(style.name());
        Bukkit.getPluginManager().callEvent(new CloudPaperPlayerLoginEvent(event, cloudPlayer));
        CloudAPI.instance().cloudPlayerProvider().updateCloudPlayer(cloudPlayer);
    }

    @Override
    public void logout(@NotNull PlayerQuitEvent event, @NotNull Player player, Object... sub) {
        CloudAPI.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudPlayerLogoutPacket(
                CloudAPI.instance().cloudPlayerProvider().cloudPlayer(player.getUniqueId()),
                CloudPaperPlugin.instance().cloudService().orElse(null)
        ));
    }
}