package dev.golgolex.golgocloud.common.instance.packet;

import dev.golgolex.quala.netty5.protocol.Packet;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class InstanceAuthPacket extends Packet {

    private final UUID instanceId;
    private final String authKey;

    public InstanceAuthPacket(
            @NotNull UUID instanceId,
            @NotNull String authKey
    ) {
        this.instanceId = instanceId;
        this.authKey = authKey;
        buffer.writeUniqueId(this.instanceId);
        buffer.writeString(this.authKey);
    }

    public InstanceAuthPacket(CodecBuffer buffer) {
        super(buffer);
        this.instanceId = buffer.readUniqueId();
        this.authKey = buffer.readString();
    }
}
