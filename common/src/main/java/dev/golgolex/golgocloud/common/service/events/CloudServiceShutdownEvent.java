package dev.golgolex.golgocloud.common.service.events;

/*
 * Copyright 2023-2024 golgocloud contributors
 */

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.quala.event.events.Event;
import org.jetbrains.annotations.NotNull;

public record CloudServiceShutdownEvent(@NotNull CloudService cloudService) implements Event {
}
