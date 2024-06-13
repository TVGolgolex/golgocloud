package dev.golgolex.golgocloud.common.instance.packet;

import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;

public final class CloudInstanceReloadPacket extends Packet {

    public CloudInstanceReloadPacket() {
    }

    public CloudInstanceReloadPacket(CodecBuffer buffer) {
        super(buffer);
    }
}
