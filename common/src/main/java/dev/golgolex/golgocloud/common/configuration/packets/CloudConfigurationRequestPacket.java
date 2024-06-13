package dev.golgolex.golgocloud.common.configuration.packets;

import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public class CloudConfigurationRequestPacket extends Packet {

    private final String configuration;

    public CloudConfigurationRequestPacket(@NotNull String configuration) {
        this.configuration = configuration;
        buffer.writeString(this.configuration);
    }

    public CloudConfigurationRequestPacket(CodecBuffer buffer) {
        super(buffer);
        this.configuration = buffer.readString();
    }
}
