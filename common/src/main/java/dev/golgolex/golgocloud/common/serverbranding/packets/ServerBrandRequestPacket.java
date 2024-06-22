package dev.golgolex.golgocloud.common.serverbranding.packets;

import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;

public class ServerBrandRequestPacket extends Packet {
    public ServerBrandRequestPacket() {
    }

    public ServerBrandRequestPacket(CodecBuffer buffer) {
        super(buffer);
    }
}
