package dev.golgolex.golgocloud.common.service.packets;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.service.ServiceEnvironment;
import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CloudServicesReplyPacket extends Packet {

    private final List<CloudService> cloudService;

    public CloudServicesReplyPacket(List<CloudService> cloudService) {
        this.cloudService = cloudService;
        buffer.writeList(this.cloudService, (codecBuffer, it) -> {
            codecBuffer.writeEnum(it.environment());
            it.writeBuffer(codecBuffer);
        });
    }

    public CloudServicesReplyPacket(CodecBuffer buffer) {
        super(buffer);
        this.cloudService = buffer.readList(new ArrayList<>(), () -> {
            var cloudService = CloudService.constructEnrvionment(buffer.readEnum(ServiceEnvironment.class));
            if (cloudService != null) {
                cloudService.readBuffer(buffer);
            }
            return cloudService;
        });
    }
}
