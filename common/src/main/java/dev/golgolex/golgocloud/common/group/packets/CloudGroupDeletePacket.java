package dev.golgolex.golgocloud.common.group.packets;

import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import org.jetbrains.annotations.NotNull;

public class CloudGroupDeletePacket extends AbstractCloudGroupPacket{
    public CloudGroupDeletePacket(@NotNull CloudGroup cloudGroup) {
        super(cloudGroup);
    }

    public CloudGroupDeletePacket(CodecBuffer buffer) {
        super(buffer);
    }
}
