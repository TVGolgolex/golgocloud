package dev.golgolex.golgocloud.plugin.paper.listener;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.plugin.paper.CloudPaperPlugin;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The AsyncPlayerPreLoginListener class is a listener for the AsyncPlayerPreLoginEvent.
 * It provides event handling for player pre-login actions in an asynchronous manner.
 */
@AllArgsConstructor
public class CloudPaperAsyncPlayerPreLoginListener implements Listener {

    private final CloudAPI cloudAPI;
    private final CloudPaperPlugin cloudPaperPlugin;

    /**
     * Handles the AsyncPlayerPreLoginEvent, which is triggered when a player is about to login.
     * Checks if the server is undergoing maintenance or if no server group could be found.
     * Disallows the player from logging in with an appropriate kick message.
     *
     * @param event The AsyncPlayerPreLoginEvent
     */
    @EventHandler
    public void onAsyncPlayerPreLogin(@NotNull AsyncPlayerPreLoginEvent event) {
        this.cloudAPI.cloudGroupProvider().cloudGroup(this.cloudPaperPlugin.thisGroupName()).ifPresentOrElse(cloudGroup -> {
            if (cloudGroup.maintenance()) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, Component.text("§cThis server group is undergoing maintenance§8."));
            }
        }, () -> event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("§cNo server group could be found§8. §cIncorrect loading could be the cause§8.")));

        this.cloudAPI.cloudServiceProvider().cloudService(this.cloudPaperPlugin.thisServiceId()).ifPresentOrElse(cloudService -> {
            if (!cloudService.ready()) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("§cThis server is not yet ready§8."));
            }
        }, () -> event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("§cNo service could be found§8. §cIncorrect loading could be the cause§8.")));

        Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().equalsIgnoreCase(event.getName())).findFirst().ifPresent(player -> {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("§cYou are already logged in§8."));
        });
    }
}