package dev.golgolex.golgocloud.common.service.packets;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;

public class CloudServiceShutdownPacket extends AbstractCloudServicePacket {
    public CloudServiceShutdownPacket(CloudService cloudService) {
        super(cloudService);
    }

    public CloudServiceShutdownPacket(CodecBuffer buffer) {
        super(buffer);
    }
}
