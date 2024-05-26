package dev.golgolex.golgocloud.common.template.packets;

import dev.golgolex.golgocloud.common.template.CloudServiceTemplate;
import dev.golgolex.quala.netty5.protocol.Packet;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CloudServiceTemplatesReplyPacket extends Packet {

    private final List<CloudServiceTemplate> cloudServiceTemplates;

    public CloudServiceTemplatesReplyPacket(List<CloudServiceTemplate> cloudServiceTemplates) {
        this.cloudServiceTemplates = cloudServiceTemplates;
        this.buffer.writeList(this.cloudServiceTemplates, (codecBuffer, cloudServiceTemplate) -> cloudServiceTemplate.writeBuffer(codecBuffer));
    }

    public CloudServiceTemplatesReplyPacket(CodecBuffer buffer) {
        super(buffer);
        this.cloudServiceTemplates = buffer.readList(new ArrayList<>(), () -> {
            var template = new CloudServiceTemplate();
            template.readBuffer(buffer);
            return template;
        });
    }
}
