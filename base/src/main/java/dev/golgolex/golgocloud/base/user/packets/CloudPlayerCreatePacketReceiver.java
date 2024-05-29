package dev.golgolex.golgocloud.base.user.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerCreatePacket;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public class CloudPlayerCreatePacketReceiver extends PacketReceiver<CloudPlayerCreatePacket> {
    @Override
    public void receivePacket(CloudPlayerCreatePacket packet, NetworkChannel networkChannel) {
        CloudBase.instance().playerProvider().createCloudPlayer(packet.cloudPlayer());
    }
}
