package dev.golgolex.golgocloud.base.permission.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.permission.packets.CloudPermissionPoolDeletePacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudPermissionPoolDeletePacketReceiver extends PacketReceiver<CloudPermissionPoolDeletePacket> {
    @Override
    public void receivePacket(CloudPermissionPoolDeletePacket cloudPermissionPoolDeletePacket, NetworkChannel networkChannel) {
        CloudBase.instance().cloudPermissionService().deletePermissionPool(cloudPermissionPoolDeletePacket.cloudPermissionPool());
    }
}
