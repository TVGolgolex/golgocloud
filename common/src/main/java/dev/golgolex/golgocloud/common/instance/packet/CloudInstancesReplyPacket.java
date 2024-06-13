package dev.golgolex.golgocloud.common.instance.packet;

import dev.golgolex.golgocloud.common.instance.CloudInstance;
import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class CloudInstancesReplyPacket extends Packet {

    private final List<CloudInstance> cloudInstance;

    public CloudInstancesReplyPacket(List<CloudInstance> cloudInstance) {
        this.cloudInstance = cloudInstance;
        this.buffer.writeList(this.cloudInstance, (codecBuffer, it) -> it.writeBuffer(codecBuffer));
    }

    public CloudInstancesReplyPacket(CodecBuffer buffer) {
        super(buffer);
        this.cloudInstance = buffer.readList(new ArrayList<>(), () -> {
           var cloudInstance = new CloudInstance();
           cloudInstance.readBuffer(buffer);
           return cloudInstance;
        });
    }
}
