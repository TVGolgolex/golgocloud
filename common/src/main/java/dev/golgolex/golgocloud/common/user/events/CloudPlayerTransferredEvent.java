package dev.golgolex.golgocloud.common.user.events;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.quala.event.events.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CloudPlayerTransferredEvent(@NotNull CloudPlayer cloudPlayer, @Nullable CloudService oldService,
                                          @NotNull CloudService newService) implements Event {
}
