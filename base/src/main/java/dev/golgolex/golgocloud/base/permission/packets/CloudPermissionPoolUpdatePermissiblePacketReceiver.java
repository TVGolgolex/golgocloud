package dev.golgolex.golgocloud.base.permission.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.permission.CloudPermissibleEntity;
import dev.golgolex.golgocloud.common.permission.CloudPermissibleGroup;
import dev.golgolex.golgocloud.common.permission.packets.CloudPermissionPoolUpdatePermissiblePacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudPermissionPoolUpdatePermissiblePacketReceiver extends PacketReceiver<CloudPermissionPoolUpdatePermissiblePacket> {
    @Override
    public void receivePacket(CloudPermissionPoolUpdatePermissiblePacket cloudPermissionPoolUpdatePermissiblePacket, NetworkChannel networkChannel) {
        if (cloudPermissionPoolUpdatePermissiblePacket.cloudPermissible() instanceof CloudPermissibleGroup permissibleGroup) {
            CloudBase.instance().cloudPermissionService().handleUpdatePermissible(cloudPermissionPoolUpdatePermissiblePacket.id(), permissibleGroup);
        }

        if (cloudPermissionPoolUpdatePermissiblePacket.cloudPermissible() instanceof CloudPermissibleEntity permissibleEntity) {
            CloudBase.instance().cloudPermissionService().handleUpdatePermissible(cloudPermissionPoolUpdatePermissiblePacket.id(), permissibleEntity);
        }
    }
}
