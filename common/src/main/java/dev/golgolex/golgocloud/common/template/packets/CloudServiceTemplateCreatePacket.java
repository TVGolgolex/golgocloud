package dev.golgolex.golgocloud.common.template.packets;

import dev.golgolex.golgocloud.common.template.CloudServiceTemplate;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;

public class CloudServiceTemplateCreatePacket extends AbstractCloudServiceTemplatePacket{
    public CloudServiceTemplateCreatePacket(CloudServiceTemplate cloudServiceTemplate) {
        super(cloudServiceTemplate);
    }

    public CloudServiceTemplateCreatePacket(CodecBuffer buffer) {
        super(buffer);
    }
}
