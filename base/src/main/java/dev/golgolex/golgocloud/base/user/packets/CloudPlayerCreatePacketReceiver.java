package dev.golgolex.golgocloud.base.user.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerCreatePacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudPlayerCreatePacketReceiver extends PacketReceiver<CloudPlayerCreatePacket> {
    @Override
    public void receivePacket(CloudPlayerCreatePacket packet, NetworkChannel networkChannel) {
        CloudBase.instance().playerProvider().createCloudPlayer(packet.cloudPlayer());
    }
}
