package dev.golgolex.golgocloud.common.service.packets;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;

public class CloudServiceStartedPacket extends AbstractCloudServicePacket {
    public CloudServiceStartedPacket(CloudService cloudService) {
        super(cloudService);
    }

    public CloudServiceStartedPacket(CodecBuffer buffer) {
        super(buffer);
    }
}
