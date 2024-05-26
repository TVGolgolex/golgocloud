package dev.golgolex.golgocloud.common.instance.packet;

import dev.golgolex.quala.netty5.protocol.Packet;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;

public final class InstanceReloadPacket extends Packet {

    public InstanceReloadPacket() {
    }

    public InstanceReloadPacket(CodecBuffer buffer) {
        super(buffer);
    }
}
