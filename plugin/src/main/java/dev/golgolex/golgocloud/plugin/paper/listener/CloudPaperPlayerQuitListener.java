package dev.golgolex.golgocloud.plugin.paper.listener;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.plugin.paper.CloudPaperPlugin;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class CloudPaperPlayerQuitListener implements Listener {

    private final CloudAPI cloudAPI;
    private final CloudPaperPlugin cloudPaperPlugin;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (cloudPaperPlugin.playerConnectionHandler() != null) {
            cloudPaperPlugin.playerConnectionHandler().logout(event, event.getPlayer());
        }
    }

}
