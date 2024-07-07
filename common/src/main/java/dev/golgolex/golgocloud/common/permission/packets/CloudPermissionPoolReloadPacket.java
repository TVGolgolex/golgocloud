package dev.golgolex.golgocloud.common.permission.packets;

import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class CloudPermissionPoolReloadPacket extends AbstractCloudPermissionPoolPacket {
    public CloudPermissionPoolReloadPacket(@NotNull String id, @NotNull UUID uuid) {
        super(id, uuid);
    }

    public CloudPermissionPoolReloadPacket(CodecBuffer buffer) {
        super(buffer);
    }
}
