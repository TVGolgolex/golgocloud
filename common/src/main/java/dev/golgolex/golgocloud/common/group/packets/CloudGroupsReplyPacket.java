package dev.golgolex.golgocloud.common.group.packets;

import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CloudGroupsReplyPacket extends Packet {

    private final List<CloudGroup> cloudGroups;

    public CloudGroupsReplyPacket(List<CloudGroup> cloudGroups) {
        this.cloudGroups = cloudGroups;
        buffer.writeList(this.cloudGroups, (buffer, cloudGroup) -> cloudGroup.writeBuffer(buffer));
    }

    public CloudGroupsReplyPacket(CodecBuffer buffer) {
        super(buffer);
        this.cloudGroups = buffer.readList(new ArrayList<>(), () -> {
           var cloudGroup = new CloudGroup();
           cloudGroup.readBuffer(buffer);
           return cloudGroup;
        });
    }
}
