package dev.golgolex.golgocloud.base.permission.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.permission.packets.CloudPermissionPoolCreatePacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudPermissionPoolCreatePacketReceiver extends PacketReceiver<CloudPermissionPoolCreatePacket> {
    @Override
    public void receivePacket(CloudPermissionPoolCreatePacket cloudPermissionPoolCreatePacket, NetworkChannel networkChannel) {
        CloudBase.instance().cloudPermissionService().createPermissionPool(cloudPermissionPoolCreatePacket.cloudPermissionPool());
    }
}
