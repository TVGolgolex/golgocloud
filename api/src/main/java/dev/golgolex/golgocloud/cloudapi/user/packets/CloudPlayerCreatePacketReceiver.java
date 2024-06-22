package dev.golgolex.golgocloud.cloudapi.user.packets;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerCreatePacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudPlayerCreatePacketReceiver extends PacketReceiver<CloudPlayerCreatePacket> {
    @Override
    public void receivePacket(CloudPlayerCreatePacket packet, NetworkChannel networkChannel) {
        CloudAPI.instance().cloudPlayerProvider().putCache(packet.cloudPlayer());
    }
}
