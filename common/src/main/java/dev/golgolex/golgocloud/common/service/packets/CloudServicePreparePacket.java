package dev.golgolex.golgocloud.common.service.packets;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;

public class CloudServicePreparePacket extends AbstractCloudServicePacket {
    public CloudServicePreparePacket(CloudService cloudService) {
        super(cloudService);
    }

    public CloudServicePreparePacket(CodecBuffer buffer) {
        super(buffer);
    }
}
