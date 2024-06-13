package dev.golgolex.golgocloud.common.user.events;

import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.quala.event.registry.events.Event;
import org.jetbrains.annotations.NotNull;

public record CloudPlayerCreateEvent(@NotNull CloudPlayer cloudPlayer) implements Event {
}
