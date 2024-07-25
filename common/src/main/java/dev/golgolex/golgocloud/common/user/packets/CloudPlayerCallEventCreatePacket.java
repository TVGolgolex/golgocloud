package dev.golgolex.golgocloud.common.user.packets;

import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import org.jetbrains.annotations.NotNull;

public class CloudPlayerCallEventCreatePacket extends AbstractCloudPlayerPacket {

    public CloudPlayerCallEventCreatePacket(@NotNull CloudPlayer cloudPlayer) {
        super(cloudPlayer);
    }

    public CloudPlayerCallEventCreatePacket(CodecBuffer buffer) {
        super(buffer);
    }

}
