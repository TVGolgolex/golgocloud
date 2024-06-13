package dev.golgolex.golgocloud.common.instance.packet;

import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;

public final class CloudInstancesRequestPacket extends Packet {
    public CloudInstancesRequestPacket() {
    }

    public CloudInstancesRequestPacket(CodecBuffer buffer) {
        super(buffer);
    }
}
