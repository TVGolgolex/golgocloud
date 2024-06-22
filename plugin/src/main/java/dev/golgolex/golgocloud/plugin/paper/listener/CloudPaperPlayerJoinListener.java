package dev.golgolex.golgocloud.plugin.paper.listener;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.plugin.paper.CloudPaperPlugin;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@AllArgsConstructor
public class CloudPaperPlayerJoinListener implements Listener {

    private final CloudAPI cloudAPI;
    private final CloudPaperPlugin cloudPaperPlugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (cloudPaperPlugin.playerConnectionHandler() != null) {
            cloudPaperPlugin.playerConnectionHandler().finalJoin(event, event.getPlayer());
        }
    }

}
