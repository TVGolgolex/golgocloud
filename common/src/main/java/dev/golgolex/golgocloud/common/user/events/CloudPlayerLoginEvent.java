package dev.golgolex.golgocloud.common.user.events;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.quala.event.events.Event;
import org.jetbrains.annotations.NotNull;

public record CloudPlayerLoginEvent(@NotNull CloudPlayer cloudPlayer, @NotNull CloudService connectedService) implements Event {
}