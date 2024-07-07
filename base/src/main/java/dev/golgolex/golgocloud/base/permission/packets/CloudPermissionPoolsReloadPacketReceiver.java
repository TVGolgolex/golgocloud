package dev.golgolex.golgocloud.base.permission.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.permission.packets.CloudPermissionPoolsReloadPacket;
import dev.golgolex.golgocloud.common.permission.packets.CloudPermissionPoolsReloadReplyPacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudPermissionPoolsReloadPacketReceiver extends PacketReceiver<CloudPermissionPoolsReloadPacket> {
    @Override
    public void receivePacket(CloudPermissionPoolsReloadPacket cloudPermissionPoolsReloadPacket, NetworkChannel networkChannel) {
        respond(new CloudPermissionPoolsReloadReplyPacket(
                CloudBase.instance().cloudPermissionService().permissionPools()
        ), networkChannel);
    }
}
