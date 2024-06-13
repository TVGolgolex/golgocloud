package dev.golgolex.golgocloud.common.user.packets;

import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import org.jetbrains.annotations.NotNull;

public class CloudPlayerUpdatePacket extends AbstractCloudPlayerPacket{
    public CloudPlayerUpdatePacket(@NotNull CloudPlayer cloudPlayer) {
        super(cloudPlayer);
    }

    public CloudPlayerUpdatePacket(CodecBuffer buffer) {
        super(buffer);
    }
}
