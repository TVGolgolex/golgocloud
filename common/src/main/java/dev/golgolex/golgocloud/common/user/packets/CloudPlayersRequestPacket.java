package dev.golgolex.golgocloud.common.user.packets;

import dev.golgolex.quala.netty5.protocol.Packet;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;

public class CloudPlayersRequestPacket extends Packet {
    public CloudPlayersRequestPacket() {
    }

    public CloudPlayersRequestPacket(CodecBuffer buffer) {
        super(buffer);
    }
}
