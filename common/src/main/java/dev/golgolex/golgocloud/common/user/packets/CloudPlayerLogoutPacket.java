package dev.golgolex.golgocloud.common.user.packets;

import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import org.jetbrains.annotations.NotNull;

public class CloudPlayerLogoutPacket extends AbstractCloudPlayerPacket{
    public CloudPlayerLogoutPacket(@NotNull CloudPlayer cloudPlayer) {
        super(cloudPlayer);
    }

    public CloudPlayerLogoutPacket(CodecBuffer buffer) {
        super(buffer);
    }
}
