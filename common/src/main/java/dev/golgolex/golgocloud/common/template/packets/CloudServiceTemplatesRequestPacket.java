package dev.golgolex.golgocloud.common.template.packets;

import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;

public class CloudServiceTemplatesRequestPacket extends Packet {

    public CloudServiceTemplatesRequestPacket() {
    }

    public CloudServiceTemplatesRequestPacket(CodecBuffer buffer) {
        super(buffer);
    }
}
