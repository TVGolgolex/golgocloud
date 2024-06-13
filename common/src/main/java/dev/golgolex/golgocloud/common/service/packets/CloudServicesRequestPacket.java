package dev.golgolex.golgocloud.common.service.packets;

import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;

public class CloudServicesRequestPacket extends Packet {
    public CloudServicesRequestPacket() {
    }

    public CloudServicesRequestPacket(CodecBuffer buffer) {
        super(buffer);
    }
}
