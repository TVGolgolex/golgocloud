package dev.golgolex.golgocloud.common.service.packets;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;

public class CloudServiceUpdatePacket extends AbstractCloudServicePacket {
    public CloudServiceUpdatePacket(CloudService cloudService) {
        super(cloudService);
    }

    public CloudServiceUpdatePacket(CodecBuffer buffer) {
        super(buffer);
    }
}
