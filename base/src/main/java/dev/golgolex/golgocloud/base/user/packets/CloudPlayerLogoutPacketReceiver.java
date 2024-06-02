package dev.golgolex.golgocloud.base.user.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerLogoutPacket;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public class CloudPlayerLogoutPacketReceiver extends PacketReceiver<CloudPlayerLogoutPacket> {
    @Override
    public void receivePacket(CloudPlayerLogoutPacket packet, NetworkChannel networkChannel) {
        CloudBase.instance().playerProvider().addToLogoutQueue(packet.cloudPlayer());
    }
}
