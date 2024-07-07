package dev.golgolex.golgocloud.common.permission.packets;

import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;

public final class CloudPermissionPoolsReloadPacket extends Packet {

    public CloudPermissionPoolsReloadPacket() {
    }

    public CloudPermissionPoolsReloadPacket(CodecBuffer buffer) {
        super(buffer);
    }
}
