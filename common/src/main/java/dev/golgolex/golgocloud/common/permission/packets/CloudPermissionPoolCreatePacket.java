package dev.golgolex.golgocloud.common.permission.packets;

import dev.golgolex.golgocloud.common.permission.CloudPermissionPool;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class CloudPermissionPoolCreatePacket extends AbstractCloudPermissionPoolPacket {

    private final CloudPermissionPool cloudPermissionPool;

    public CloudPermissionPoolCreatePacket(
            @NotNull String id,
            @NotNull UUID uuid,
            @NotNull CloudPermissionPool cloudPermissionPool) {
        super(id, uuid);
        this.cloudPermissionPool = cloudPermissionPool;
        buffer.writeBufferClass(this.cloudPermissionPool);
    }

    public CloudPermissionPoolCreatePacket(CodecBuffer buffer) {
        super(buffer);
        this.cloudPermissionPool = buffer.readBufferClass(new CloudPermissionPool());
    }
}
