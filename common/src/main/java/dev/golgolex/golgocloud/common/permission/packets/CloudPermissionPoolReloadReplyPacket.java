package dev.golgolex.golgocloud.common.permission.packets;

import dev.golgolex.golgocloud.common.permission.CloudPermissionPool;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class CloudPermissionPoolReloadReplyPacket extends AbstractCloudPermissionPoolPacket{

    private final CloudPermissionPool permissionPool;

    public CloudPermissionPoolReloadReplyPacket(@NotNull String id, @NotNull UUID uuid, @NotNull CloudPermissionPool permissionPool) {
        super(id, uuid);
        this.permissionPool = permissionPool;
        buffer.writeBufferClass(this.permissionPool);
    }

    public CloudPermissionPoolReloadReplyPacket(CodecBuffer buffer) {
        super(buffer);
        this.permissionPool = buffer.readBufferClass(new CloudPermissionPool());
    }
}
