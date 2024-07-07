package dev.golgolex.golgocloud.common.permission.packets;

import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public abstract class AbstractCloudPermissionPoolPacket extends Packet {

    private final String id;
    private final UUID uuid;

    public AbstractCloudPermissionPoolPacket(@NotNull String id, @NotNull UUID uuid) {
        this.id = id;
        this.uuid = uuid;
        buffer.writeString(this.id);
        buffer.writeUniqueId(this.uuid);
    }

    public AbstractCloudPermissionPoolPacket(CodecBuffer buffer) {
        super(buffer);
        this.id = buffer.readString();
        this.uuid = buffer.readUniqueId();
    }
}
