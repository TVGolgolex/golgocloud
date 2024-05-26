package dev.golgolex.golgocloud.common.template.packets;

import dev.golgolex.golgocloud.common.template.CloudServiceTemplate;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;

public class CloudServiceTemplateDeletePacket extends AbstractCloudServiceTemplatePacket{
    public CloudServiceTemplateDeletePacket(CloudServiceTemplate cloudServiceTemplate) {
        super(cloudServiceTemplate);
    }

    public CloudServiceTemplateDeletePacket(CodecBuffer buffer) {
        super(buffer);
    }
}
