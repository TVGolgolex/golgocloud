package dev.golgolex.golgocloud.common.group.packets;

import dev.golgolex.golgocloud.common.service.ServiceEnvironment;
import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public class CloudGroupsRequestPacket extends Packet {

    private final ServiceEnvironment serviceEnvironment;

    public CloudGroupsRequestPacket() {
        buffer.writeBoolean(false);
        serviceEnvironment = null;
    }

    public CloudGroupsRequestPacket(@NotNull ServiceEnvironment serviceEnvironment) {
        this.serviceEnvironment = serviceEnvironment;
        buffer.writeBoolean(true);
        buffer.writeEnum(serviceEnvironment);
    }

    public CloudGroupsRequestPacket(CodecBuffer buffer) {
        super(buffer);
        if (buffer.readBoolean()) {
            serviceEnvironment = buffer.readEnum(ServiceEnvironment.class);
        } else {
            serviceEnvironment = null;
        }
    }
}
