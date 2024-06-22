package dev.golgolex.golgocloud.plugin.paper.event;

import dev.golgolex.golgocloud.common.user.CloudPlayer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class CloudPaperPlayerLoginEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final PlayerJoinEvent playerJoinEvent;
    private final CloudPlayer cloudPlayer;

    public CloudPaperPlayerLoginEvent(PlayerJoinEvent playerJoinEvent, CloudPlayer cloudPlayer) {
        this.playerJoinEvent = playerJoinEvent;
        this.cloudPlayer = cloudPlayer;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
