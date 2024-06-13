package dev.golgolex.golgocloud.common.user.packets;

import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import org.jetbrains.annotations.NotNull;

public class CloudPlayerCreatePacket extends AbstractCloudPlayerPacket{
    public CloudPlayerCreatePacket(@NotNull CloudPlayer cloudPlayer) {
        super(cloudPlayer);
    }

    public CloudPlayerCreatePacket(CodecBuffer buffer) {
        super(buffer);
    }
}
