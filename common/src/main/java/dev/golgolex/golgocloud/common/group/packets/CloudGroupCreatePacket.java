package dev.golgolex.golgocloud.common.group.packets;

import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import org.jetbrains.annotations.NotNull;

public class CloudGroupCreatePacket extends AbstractCloudGroupPacket{
    public CloudGroupCreatePacket(@NotNull CloudGroup cloudGroup) {
        super(cloudGroup);
    }

    public CloudGroupCreatePacket(CodecBuffer buffer) {
        super(buffer);
    }
}
