package dev.golgolex.golgocloud.base.permission.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.permission.packets.CloudPermissionPoolUpdatePacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudPermissionPoolUpdatePacketReceiver extends PacketReceiver<CloudPermissionPoolUpdatePacket> {
    @Override
    public void receivePacket(CloudPermissionPoolUpdatePacket cloudPermissionPoolUpdatePacket, NetworkChannel networkChannel) {
        CloudBase.instance().cloudPermissionService().updatePermissionPool(cloudPermissionPoolUpdatePacket.cloudPermissionPool());
    }
}
