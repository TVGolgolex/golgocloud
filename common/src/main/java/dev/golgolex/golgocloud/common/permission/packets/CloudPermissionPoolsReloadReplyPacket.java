package dev.golgolex.golgocloud.common.permission.packets;

import dev.golgolex.golgocloud.common.permission.CloudPermissionPool;
import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class CloudPermissionPoolsReloadReplyPacket extends Packet {

    private final List<CloudPermissionPool> permissionPools;

    public CloudPermissionPoolsReloadReplyPacket(List<CloudPermissionPool> permissionPools) {
        this.permissionPools = permissionPools;
        buffer.writeList(this.permissionPools, CodecBuffer::writeBufferClass);
    }

    public CloudPermissionPoolsReloadReplyPacket(CodecBuffer buffer) {
        super(buffer);
        this.permissionPools = buffer.readList(new ArrayList<>(), () -> buffer.readBufferClass(new CloudPermissionPool()));
    }
}
