package dev.golgolex.golgocloud.common.group.packets;

import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import org.jetbrains.annotations.NotNull;

public class CloudGroupUpdatePacket extends AbstractCloudGroupPacket {
    public CloudGroupUpdatePacket(@NotNull CloudGroup cloudGroup) {
        super(cloudGroup);
    }

    public CloudGroupUpdatePacket(CodecBuffer buffer) {
        super(buffer);
    }
}
