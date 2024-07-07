package dev.golgolex.golgocloud.base.permission.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.permission.packets.CloudPermissionPoolReloadPacket;
import dev.golgolex.golgocloud.common.permission.packets.CloudPermissionPoolReloadReplyPacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudPermissionPoolReloadPacketReceiver extends PacketReceiver<CloudPermissionPoolReloadPacket> {
    @Override
    public void receivePacket(CloudPermissionPoolReloadPacket cloudPermissionPoolReloadPacket, NetworkChannel networkChannel) {
        respond(new CloudPermissionPoolReloadReplyPacket(
                cloudPermissionPoolReloadPacket.id(),
                cloudPermissionPoolReloadPacket.uuid(),
                CloudBase.instance().cloudPermissionService().permissionPool(cloudPermissionPoolReloadPacket.id())
        ), networkChannel);
    }
}
