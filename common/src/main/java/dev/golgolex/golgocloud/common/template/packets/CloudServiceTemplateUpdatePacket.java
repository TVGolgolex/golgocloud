package dev.golgolex.golgocloud.common.template.packets;

import dev.golgolex.golgocloud.common.template.CloudServiceTemplate;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;

public class CloudServiceTemplateUpdatePacket extends AbstractCloudServiceTemplatePacket{
    public CloudServiceTemplateUpdatePacket(CloudServiceTemplate cloudServiceTemplate) {
        super(cloudServiceTemplate);
    }

    public CloudServiceTemplateUpdatePacket(CodecBuffer buffer) {
        super(buffer);
    }
}
