package dev.golgolex.golgocloud.common.template.packets;

import dev.golgolex.golgocloud.common.template.CloudServiceTemplate;
import dev.golgolex.quala.netty5.protocol.Packet;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public abstract class AbstractCloudServiceTemplatePacket extends Packet {

    private final CloudServiceTemplate cloudServiceTemplate;

    public AbstractCloudServiceTemplatePacket(CloudServiceTemplate cloudServiceTemplate) {
        this.cloudServiceTemplate = cloudServiceTemplate;
        this.cloudServiceTemplate.writeBuffer(buffer);
    }

    public AbstractCloudServiceTemplatePacket(CodecBuffer buffer) {
        super(buffer);
        this.cloudServiceTemplate = new CloudServiceTemplate();
        this.cloudServiceTemplate.readBuffer(buffer);
    }
}
