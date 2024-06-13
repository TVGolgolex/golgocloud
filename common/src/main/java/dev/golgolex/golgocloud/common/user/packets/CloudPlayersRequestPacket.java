package dev.golgolex.golgocloud.common.user.packets;

import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;

public class CloudPlayersRequestPacket extends Packet {
    public CloudPlayersRequestPacket() {
    }

    public CloudPlayersRequestPacket(CodecBuffer buffer) {
        super(buffer);
    }
}
