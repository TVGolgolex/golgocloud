package dev.golgolex.golgocloud.common.user.packets;

import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import org.jetbrains.annotations.NotNull;

public class CloudPlayerLoginPacket extends AbstractCloudPlayerPacket{
    public CloudPlayerLoginPacket(@NotNull CloudPlayer cloudPlayer) {
        super(cloudPlayer);
    }

    public CloudPlayerLoginPacket(CodecBuffer buffer) {
        super(buffer);
    }
}
